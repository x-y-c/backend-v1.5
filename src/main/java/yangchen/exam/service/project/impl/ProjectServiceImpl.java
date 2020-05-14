package yangchen.exam.service.project.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import yangchen.exam.Enum.StageEnum;
import yangchen.exam.entity.*;
import yangchen.exam.model.*;
import yangchen.exam.repo.*;
import yangchen.exam.service.project.ProjectService;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.util.HtmlUtil;
import yangchen.exam.util.ZipUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

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
}
