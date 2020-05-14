package yangchen.exam.service.project.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import yangchen.exam.Enum.StageEnum;
import yangchen.exam.entity.*;
import yangchen.exam.model.*;
import yangchen.exam.repo.*;
import yangchen.exam.service.project.ProjectService;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.util.DecodeQuestionDetails;
import yangchen.exam.util.HtmlUtil;
import yangchen.exam.util.IpUtil;
import yangchen.exam.util.ZipUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private static Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Value("${image.nginx.url.path}")
    private String imgNginxUrl;

    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private ProjectGroupRepo projectGroupRepo;

    @Autowired
    private ProjectPaperRepo projectPaperRepo;

    @Autowired
    private ProjectInfoRepo projectInfoRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ProjectSubmitRepo projectSubmitRepo;

    @Override
    public ProjectGroup createProject(ProjectParam projectParam) {
        ProjectGroup projectGroup = new ProjectGroup();
        ProjectPaper projectPaper = new ProjectPaper();

        //开始时间
        Timestamp beginTime = projectParam.getBeginTime();
        long bTime = beginTime.getTime();
        //结束时间
        Timestamp endTime = new Timestamp(bTime + projectParam.getTtl() * 1000 * 60 * 60 * 24);
        //考试班级
        List<String> gradeList = projectParam.getGrades();
        //教师id
        Integer teacherId = teacherRepo.findByTeacherName(projectParam.getTeacherName()).getId();
        //学生list
        List<StudentNew> studentList = new ArrayList<>();
        for (String grade : gradeList) {
            List<StudentNew> classMate = studentRepo.findByStudentGradeAndTeacherId(grade,teacherId);
            studentList.addAll(classMate);
        }

        /*=================================================================*/
        projectGroup.setStartTime(beginTime);
        projectGroup.setEndTime(endTime);
        projectGroup.setProjectTtl(projectParam.getTtl());
        projectGroup.setTeacherId(teacherId);
        projectGroup.setProjectName(projectParam.getHomeworkName());

        String gradeListString = gradeList.toString();
        gradeListString = gradeListString.replace("[","");
        gradeListString = gradeListString.replace("]","");

        projectGroup.setClassName(gradeListString);
        ProjectGroup project = projectGroupRepo.save(projectGroup);

        /*=================================================================*/
        List<Integer> questionList = projectParam.getHomework();

        String questionNameString = questionList.toString();
        questionNameString = questionNameString.replace("[","");
        questionNameString = questionNameString.replace("]","");

        projectPaper.setQuestionList(questionNameString);
        projectPaper.setQuestionSize(questionList.size());
        ProjectPaper paper = projectPaperRepo.save(projectPaper);

        /*=================================================================*/
        for(StudentNew studentNew:studentList){
            ProjectInfo projectInfo = new ProjectInfo();
            projectInfo.setProjectGroupId(project.getId());
            projectInfo.setStudentId(studentNew.getStudentId());
            projectInfo.setScore(0);
            projectInfo.setProjectPaperId(paper.getId());
            projectInfo.setFinished(Boolean.FALSE);
            projectInfoRepo.save(projectInfo);
        }

        return project;
    }

    @Override
    public Page<ProjectGroup> getProjectPage(Integer page, Integer pageLimit, String teacherName) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, pageLimit, sort);
        Integer teacherId = teacherRepo.findByTeacherName(teacherName).getId();
        Page<ProjectGroup> projectGroupPage = projectGroupRepo.findByTeacherId(pageable, teacherId);
        return projectGroupPage;
    }

    @Override
    public void deleteProject(Integer homeworkId) {

        List<Integer> projectPapers = projectInfoRepo.searchProjectPaper(homeworkId);
        projectGroupRepo.deleteProjectGroupById(homeworkId);
        projectInfoRepo.deleteProjectInfoByProjectGroupId(homeworkId);
        projectPaperRepo.deleteProjectPaperByIdIn(projectPapers);

    }

    @Override
    public ProjectDetails getProjectDetails(Integer homeworkGroupId) {
        ProjectDetails projectDetails = new ProjectDetails();
        ProjectGroup projectGroup = projectGroupRepo.findById(homeworkGroupId).get();

        Integer projectPaperId = projectInfoRepo.getProjectPaperIdByProjectGroupId(homeworkGroupId);

        List<QuestionNew> questionList = questionService.getProjectPaper(projectPaperId);

        projectDetails.setQuestionList(questionList);
        projectDetails.setBeginTime(new Timestamp(projectGroup.getStartTime().getTime()));
        projectDetails.setClassName(projectGroup.getClassName());
        projectDetails.setProjectName(projectGroup.getProjectName());
        projectDetails.setExamTime(projectGroup.getProjectTtl());

        return projectDetails;
    }

    @Override
    public List<ProjectScoreModel> getScoreByTeacher(Integer homeworkGroupId) {
        List<ProjectScoreModel> ProjectScoreModelList = new ArrayList<>();
        List<ProjectInfo> ProjectInfoList = projectInfoRepo.findByProjectGroupId(homeworkGroupId);
        for(ProjectInfo projectInfo:ProjectInfoList){
            ProjectScoreModel projectScoreModel = new ProjectScoreModel();
            projectScoreModel.setFinished(projectInfo.getFinished());
            projectScoreModel.setScore(projectInfo.getScore());
            Integer studentId = projectInfo.getStudentId();
            projectScoreModel.setStudentId(studentId);

            StudentNew student = studentRepo.findByStudentId(studentId);
            projectScoreModel.setStudentName(student.getStudentName());
            projectScoreModel.setStudentGrade(student.getStudentGrade());
            ProjectScoreModelList.add(projectScoreModel);
        }
        return ProjectScoreModelList;
    }

    @Override
    public void exportScore(HttpServletResponse response, Integer homeworkGroupId) throws IOException {
        List<ExcelScoreModel> excelScoreModels = new ArrayList<>();
        ProjectGroup projectGroup = projectGroupRepo.findById(homeworkGroupId).get();
        List<ProjectInfo> projectInfoList = projectInfoRepo.findByProjectGroupId(homeworkGroupId);
        String examGroupName = projectGroup.getProjectName();
        for (int i = 0; i < projectInfoList.size(); i++) {
            StudentNew student = studentRepo.findByStudentId(projectInfoList.get(i).getStudentId());

            excelScoreModels.add(ExcelScoreModel.builder()
                    .id(i + 1)
                    .name(student.getStudentName())
                    .grade(student.getStudentGrade())
                    .score(Double.valueOf(projectInfoList.get(i).getScore()))
                    .studentID(projectInfoList.get(i).getStudentId())
                    .build());
        }
        excelScore(response,excelScoreModels,examGroupName);
    }

    @Override
    public void exportSubmitAll(HttpServletResponse response, Integer homeworkGroupId) throws IOException {

        ProjectGroup projectGroup = projectGroupRepo.findById(homeworkGroupId).get();
        List<ProjectInfo> projectInfoList = projectInfoRepo.findByProjectGroupId(homeworkGroupId);

        String projectName = projectGroup.getProjectName();

        List<ExcelSubmitModel> result = new ArrayList<>();
        for(ProjectInfo projectInfo:projectInfoList){
            StudentNew student = studentRepo.findByStudentId(projectInfo.getStudentId());

            List<QuestionNew> questionNewList = questionService.getProjectPaperByProjectInfoId(projectInfo.getId());

            List<ExcelSubmitModel> submitInformation = getSubmitInformation(questionNewList, projectInfo, student);
            submitInformation.forEach(excelSubmitModel -> {
                result.add(excelSubmitModel);
            });
        }
        excelSubmitAll(response,result,projectName);

    }

    @Override
    public void exportSubmit(HttpServletResponse response, Integer homeworkGroupId) throws IOException {
        HashMap<String, ByteArrayOutputStream> excel = new HashMap<>();
        ProjectGroup projectGroup = projectGroupRepo.findById(homeworkGroupId).get();
        List<ProjectInfo> projectInfoList = projectInfoRepo.findByProjectGroupId(homeworkGroupId);

        String projectName = projectGroup.getProjectName();

        for(ProjectInfo projectInfo:projectInfoList){
            StudentNew student = studentRepo.findByStudentId(projectInfo.getStudentId());
            List<QuestionNew> questionNewList = questionService.getProjectPaperByProjectInfoId(projectInfo.getId());
            List<ExcelSubmitModel> result = getSubmitInformation(questionNewList,projectInfo,student);

            ByteArrayOutputStream byteArrayInputStream = new ByteArrayOutputStream();
            ExcelWriter writer = ExcelUtil.getWriter();
            writer.addHeaderAlias("studentNumber", "学号");
            writer.addHeaderAlias("studentName", "姓名");
            writer.addHeaderAlias("examPaperId", "试卷编号");
            writer.addHeaderAlias("questionBh", "题目编号");
            writer.addHeaderAlias("questionName", "题目名称");
            writer.addHeaderAlias("stage", "阶段");
            writer.addHeaderAlias("questionDesc", "题目描述");
            writer.addHeaderAlias("src", "学生代码");
            writer.addHeaderAlias("codeLines","代码行数");
            writer.addHeaderAlias("score", "成绩");

            result.add(ExcelSubmitModel.builder().score(Double.valueOf(projectInfo.getScore())).build());
            //LOGGER.info("submit" + result.toString());
            List<ExcelSubmitModel> rows = CollUtil.newArrayList(result);
            writer.write(rows, true);
            writer.flush(byteArrayInputStream, true);
            writer.close();
            excel.put(projectName + "_" + student.getStudentId() + "_" + student.getStudentName() + ".xls", byteArrayInputStream);

            byteArrayInputStream.close();
        }
        ByteArrayOutputStream zip = ZipUtil.zip(excel, new File("d://test8.zip"));

        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(projectName + "_学生提交记录.zip", "utf-8"));
        response.setContentType("application/zip");
        response.getOutputStream().write(zip.toByteArray());
    }

    @Override
    public ProjectSubmit addSubmit(ProjectSubmit projectSubmit) {
        projectSubmit.setSubmitTime(new Timestamp(System.currentTimeMillis()));
        return projectSubmitRepo.save(projectSubmit);
    }

    public void excelScore(HttpServletResponse response,List<ExcelScoreModel> excelScoreModels,String examGroupName) throws IOException{

        List<ExcelScoreModel> rows = CollUtil.newArrayList(excelScoreModels);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("id", "序号");
        writer.addHeaderAlias("grade", "班级");
        writer.addHeaderAlias("studentID", "学号");
        writer.addHeaderAlias("name", "姓名");
        writer.addHeaderAlias("score", "成绩");
        writer.write(rows, true);

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String value = "attachment;filename=" + URLEncoder.encode(examGroupName + ".xls", "UTF-8");
        response.setHeader("Content-Disposition", value);
        ServletOutputStream outputStream = response.getOutputStream();

        writer.flush(outputStream, true);
        writer.close();
        IoUtil.close(outputStream);
    }

    public void excelSubmitAll(HttpServletResponse response,List<ExcelSubmitModel> result ,String examDesc) throws IOException {

        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("studentNumber", "学号");
        writer.addHeaderAlias("studentName", "姓名");
        writer.addHeaderAlias("examPaperId", "试卷编号");
        writer.addHeaderAlias("questionBh", "题目编号");
        writer.addHeaderAlias("questionName", "题目名称");
        writer.addHeaderAlias("stage", "阶段");
        writer.addHeaderAlias("questionDesc", "题目描述");
        writer.addHeaderAlias("src", "学生代码");
        writer.addHeaderAlias("codeLines","代码行数");
        writer.addHeaderAlias("score", "成绩");
        //LOGGER.info("submitAll" + result.toString());
//        System.out.println(result.toString());
        List<ExcelSubmitModel> rows = CollUtil.newArrayList(result);
        writer.write(rows, true);

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String value = "attachment;filename=" + URLEncoder.encode(examDesc + "_学生提交记录导出（总）" + ".xls", "UTF-8");
        response.setHeader("Content-Disposition", value);
        ServletOutputStream outputStream = response.getOutputStream();

        writer.flush(outputStream, true);
        writer.close();
        IoUtil.close(outputStream);
    }

    private List<ExcelSubmitModel> getSubmitInformation(List<QuestionNew> questionNewList, ProjectInfo projectInfo, StudentNew student){
        List<ExcelSubmitModel> excelSubmitModelList = new ArrayList<>();
        for(int index=0; index<questionNewList.size(); index++){
            QuestionNew question = questionNewList.get(index);
            ProjectSubmit lastSubmit = projectSubmitRepo.getLastSubmit(projectInfo.getStudentId(), projectInfo.getProjectPaperId(), index);
            if (lastSubmit == null) {
                excelSubmitModelList.add(
                        setExcelSubmitModel(
                                question.getId(),
                                "",
                                0,
                                question.getQuestionDetails(),
                                question.getQuestionName(),
                                0.0,
                                question.getStage(),
                                projectInfo.getProjectPaperId(),
                                projectInfo.getStudentId(),
                                student.getStudentName()
                        ));
            }
            else{
                excelSubmitModelList.add(
                        setExcelSubmitModel(
                                question.getId(),
                                lastSubmit.getSrc(),
                                lastSubmit.getCodeLines(),
                                question.getQuestionDetails(),
                                question.getQuestionName(),
                                lastSubmit.getScore()/questionNewList.size(),
                                question.getStage(),
                                projectInfo.getProjectPaperId(),
                                projectInfo.getStudentId(),
                                student.getStudentName()));
            }

        }
        return excelSubmitModelList;
    }
    private ExcelSubmitModel setExcelSubmitModel(Integer id, String src, Integer codeLines, String questionDetails, String questionName, double score, String stage, Integer examPaperId, Integer studentId, String studentName){
        ExcelSubmitModel excelSubmitModel  = ExcelSubmitModel.builder()
                .questionBh(String.valueOf(id))
                .src(src)
                .codeLines(codeLines)
                .questionDesc(HtmlUtil.delHtmlTag(questionDetails))
                .questionName(questionName)
                .score(Double.valueOf(score))
                .stage(StageEnum.getStageName(stage))
                .examPaperId(examPaperId)
                .studentNumber(studentId)
                .studentName(studentName)
                .build();
        return excelSubmitModel;
    }


    @Override
    public List<ExaminationDetail> getProjectDetail(Integer studentId){
        List<ProjectInfo> projectList = projectInfoRepo.findByStudentIdOrderByIdDesc(studentId);

        List<Integer> projectGroupIdList = new ArrayList<>(projectList.size());
        List<Integer> projectIndfos = new ArrayList<>(projectList.size());
        for(ProjectInfo projectInfo:projectList){
            projectGroupIdList.add(projectInfo.getProjectGroupId());
            projectIndfos.add(projectInfo.getId());
        }

        List<ProjectGroup> projectGroups = projectGroupRepo.getProjectGroupById(projectGroupIdList);
        List<ExaminationDetail> projectDetails = new ArrayList<>(projectGroups.size());

        for (int i=0;i<projectGroups.size();i++) {
            ExaminationDetail examinationDetail = new ExaminationDetail();
            examinationDetail.setDesc(projectGroups.get(i).getProjectName());
            examinationDetail.setEnd(new Timestamp(projectGroups.get(i).getEndTime().getTime()));
            examinationDetail.setStart(new Timestamp(projectGroups.get(i).getStartTime().getTime()));
            examinationDetail.setTtl(Long.valueOf(projectGroups.get(i).getProjectTtl()));
            examinationDetail.setId(projectIndfos.get(i));
            projectDetails.add(examinationDetail);
        }
        return projectDetails;
    }

    @Override
    public JsonResult getProjectPaper(Integer projectInfoId, Integer studentId) {

        ProjectInfo projectInfo = projectInfoRepo.findById(projectInfoId).get();
        if (!projectInfo.getStudentId().equals(studentId)) {
            return JsonResult.errorResult(ResultCode.NO_PERMISSION, "没有权限查看该试卷", null);
        }
        else if(projectInfo.getFinished() == Boolean.TRUE) {
            return JsonResult.succResult(QuestionResult.builder().used(1).questionInfo(null).build());
        }
        else {
            List<QuestionDetail> questionDetailList = new ArrayList<>();
            String questionList = projectPaperRepo.getQuestions(projectInfo.getProjectPaperId());
            List<String> ql= Arrays.asList(questionList .split(",")).stream().map(s -> (s.trim())).collect(Collectors.toList());
            for(int i=0; i<ql.size(); i++){
                QuestionNew questionById = questionService.findQuestionById(Integer.valueOf(ql.get(i)));
                if (questionById != null) {
                    QuestionDetail questionDetail = new QuestionDetail();
                    questionDetail.setQuestion(DecodeQuestionDetails.getRightImage(imgNginxUrl, questionById.getQuestionDetails()));
                    questionDetail.setTitle(questionById.getQuestionName());
                    questionDetail.setCustomBh(questionById.getCustomBh());
                    questionDetail.setId(String.valueOf(questionById.getId()));
                    if ("100001".equals(questionById.getIsProgramBlank())) {
                        Gson gson = new Gson();
                        SourceCode sourceCode = gson.fromJson(questionById.getSourceCode(), SourceCode.class);
                        questionDetail.setSrc(sourceCode.getKey().get(0).getCode());
                    } else {
                        questionDetail.setSrc("");
                    }
                    ProjectSubmit lastSubmit = projectSubmitRepo.getLastSubmit(studentId, projectInfo.getProjectPaperId(), i);
                    if (lastSubmit!=null){
                        questionDetail.setScore(lastSubmit.getScore());
                        questionDetail.setCodeHistory(lastSubmit.getSrc());
                    }
                    questionDetailList.add(questionDetail);
                }
            }
            return JsonResult.succResult(QuestionResult.builder().used(0).questionInfo(questionDetailList).build());
        }
    }

    @Override
    public Boolean projectSubmitExamPaper(Integer projectInfoId, Integer studentId, Integer sign) {
        ProjectInfo projectInfo = projectInfoRepo.findById(projectInfoId).get();
        projectInfo.setFinished(Boolean.TRUE);
        Integer finalScore = computeScore(projectInfo.getProjectGroupId(),studentId);
        if(sign==1) {
            LOGGER.info("学生[{}]交卷，试卷编号[{}]，备注:主动交卷，成绩 [{}] 分", studentId, projectInfoId, finalScore);
        }
        else{
            LOGGER.info("学生[{}]交卷，试卷编号[{}]，备注:被动交卷，成绩 [{}] 分", studentId, projectInfoId, finalScore);
        }

        projectInfo.setScore(finalScore);
        projectInfo = projectInfoRepo.save(projectInfo);
        return projectInfo!=null;
    }

    private Integer computeScore(Integer projectGroupId,Integer studentId){
        Integer projectPaperId = projectInfoRepo.findByProjectGroupIdAndStudentId(projectGroupId, studentId).getProjectPaperId();
        Integer questionSize = projectPaperRepo.findById(projectPaperId).get().getQuestionSize();
        double sumScore = 0.0;
        for(int index = 0; index < questionSize; index++){
            ProjectSubmit lastSubmit = projectSubmitRepo.getLastSubmit(studentId, projectPaperId, index);
            if (lastSubmit!=null){
                Integer score = lastSubmit.getScore();
                sumScore += score;
            }
        }
        double finalScore = sumScore / questionSize;

        return (int) finalScore;
    }

    @Override
    public long getEndTime(Integer projectInfoId) {
        long time = projectGroupRepo.findById(projectInfoRepo.findById(projectInfoId).get().getProjectGroupId()).get().getEndTime().getTime();
        long nowTime = new Timestamp(System.currentTimeMillis()).getTime();
        long t = (time - nowTime)/1000;
        return t >0 ? t : 0;
    }

    @Override
    public long getStartTime(Integer projectInfoId) {
        long time = projectGroupRepo.findById(projectInfoRepo.findById(projectInfoId).get().getProjectGroupId()).get().getStartTime().getTime();
        long nowTime = new Timestamp(System.currentTimeMillis()).getTime();
        long t = (time - nowTime)/1000;
        return t >0 ? t : 0;
    }

    @Override
    public Page<ScoreDetail> getScorePage(Integer pageNum, Integer pageLimit, Integer studentId) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum - 1, pageLimit, sort);
        int start = (int) pageable.getOffset();
        List<ProjectInfo> list = projectInfoRepo.findByStudentIdOrderByIdDesc(studentId);
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        List<ProjectInfo> projectInfoList = list.subList(start, end);
        List<ScoreDetail> result = new ArrayList<>(projectInfoList.size());
        projectInfoList.forEach(projectInfo -> {
            ScoreDetail scoreDetail = new ScoreDetail();
            scoreDetail.setExamGroupId(projectInfo.getProjectGroupId());
            scoreDetail.setScore(Double.valueOf(projectInfo.getScore()));
            scoreDetail.setExamName(projectGroupRepo.findById(projectInfo.getProjectGroupId()).get().getProjectName());
            result.add(scoreDetail);
        });
        return new PageImpl<ScoreDetail>(result, pageable, list.size());
    }

    @Override
    public JsonResult isExamStart(Integer examGroupId) {
        ProjectGroup projectGroup = projectGroupRepo.findById(examGroupId).get();
        Timestamp beginTime = new Timestamp(projectGroup.getStartTime().getTime());
        Timestamp endTime = new Timestamp(projectGroup.getEndTime().getTime());
        if (new Timestamp(System.currentTimeMillis()).before(beginTime)){
            return JsonResult.succResult(null);
        }
        else if(endTime.before(new Timestamp(System.currentTimeMillis()))) {
            return JsonResult.errorResult(ResultCode.OVER_ENDTIME, "考试已经结束", null);
        }
        else {
            return JsonResult.errorResult(ResultCode.OVER_ENDTIME, "考试已经开始", null);
        }
    }

    @Override
    @Transactional
    public void updateProjectGroup(Integer examGroupId, String examDesc, Integer examTime, Timestamp beginTime) {
        Timestamp endTime = new Timestamp(beginTime.getTime() + examTime * 60 * 1000);
        projectGroupRepo.updateExamGroup(examDesc,examTime,beginTime,endTime,examGroupId);
    }

}
