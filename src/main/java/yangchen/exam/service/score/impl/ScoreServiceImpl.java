package yangchen.exam.service.score.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.Enum.StageEnum;
import yangchen.exam.entity.*;
import yangchen.exam.model.ExcelScoreModel;
import yangchen.exam.model.ExcelSubmitModel;
import yangchen.exam.model.ScoreAdmin;
import yangchen.exam.model.ScoreDetail;
import yangchen.exam.repo.*;
import yangchen.exam.service.examInfo.ExamInfoService;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.service.score.ScoreService;
import yangchen.exam.util.HtmlUtil;
import yangchen.exam.util.ZipUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author YC
 * @date 2019/6/2 21:15
 * O(∩_∩)O)
 */
@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreRepo scoreRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private ExamGroupRepo examGroupRepo;

    @Autowired
    private ExamInfoService examInfoService;

    @Autowired
    private SubmitRepo submitRepo;
    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private QuestionService questionService;

    public static Logger LOGGER = LoggerFactory.getLogger(ScoreServiceImpl.class);

    @Override
    public Score saveScore(Score score) {
        return scoreRepo.save(score);
    }

    @Override
    public List<Score> findByExaminationAndStudentId(Integer examination, Integer studentId) {
        return scoreRepo.findByStudentIdAndExaminationId(studentId, examination);
    }


    public Score saveOrUpdate(Score score) {
        Score score1 = scoreRepo.findByStudentIdAndExaminationIdAndIndexAndQuestionId(score.getStudentId(),
                score.getExaminationId(), score.getIndex(), score.getQuestionId());

        if (score1 != null) {
            score1.setScore(score.getScore());
            return scoreRepo.save(score1);
        } else {
            return scoreRepo.save(score);
        }

    }

    /**
     * 导出学生成绩信息Excel
     *
     * @param response    浏览器response
     * @param examGroupId
     * @throws IOException
     */
    @Override
    public void exportScore(HttpServletResponse response, Integer examGroupId) throws IOException {
        List<ExcelScoreModel> excelScoreModels = new ArrayList<>();
        List<ExamInfo> examInfoByExamGroup = examInfoService.getExamInfoByExamGroup(examGroupId);
        String examGroupName = examGroupRepo.findById(examGroupId).get().getExamDesc();
        for (int i = 0; i < examInfoByExamGroup.size(); i++) {
            excelScoreModels.add(ExcelScoreModel.builder()
                    .id(i + 1)
                    .name(examInfoByExamGroup.get(i).getStudentName())
                    .grade(studentRepo.getStudentGrade(examInfoByExamGroup.get(i).getStudentNumber()))
                    .score(Double.valueOf(examInfoByExamGroup.get(i).getExaminationScore()))
                    .studentID(examInfoByExamGroup.get(i).getStudentNumber())
                    .build());
        }

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


    /*
    导出每个学生的提交zip
     */
    @Override
    public List<ExcelSubmitModel> exportSubmit(HttpServletResponse response, Integer examGroupId) throws IOException {
        /**
         * examnationId --> examInfo
         * examGroupId-->submit
         * studentId,questionId-->score
         * question-->question_new
         */
        HashMap<String, ByteArrayOutputStream> excel = new HashMap<>();
        String examDesc = "";

        List<ExamInfo> examInfoByExamGroup = examInfoService.getExamInfoByExamGroup(examGroupId);

        for (ExamInfo examInfo : examInfoByExamGroup) {
            List<ExcelSubmitModel> result = new ArrayList<>();
            Integer examinationId = examInfo.getExaminationId();
            /**
             * 通过试卷id获取 题目编号
             */
            List<String> questionBhList = questionService.getQuestionBhList(examinationId);

            List<Submit> submitList = new ArrayList<>();
            for (String questionBh : questionBhList) {
                Submit lastSubmit = submitRepo.getLastSubmit(examinationId, questionBh);
                if (lastSubmit == null) {

                    submitList.add(Submit.builder().questionId(questionBh).studentId(Long.valueOf(examInfo.getStudentNumber())).src("未提交").build());
                } else {
                    submitList.add(lastSubmit);
                }
            }
            for (Submit submit : submitList) {
                QuestionNew question = questionRepo.findByQuestionBh(submit.getQuestionId());
                Score score = scoreRepo.findByStudentIdAndQuestionId(examInfo.getStudentNumber(), question.getQuestionBh());
                if (score == null) {
                    score = new Score();
                    score.setScore(0);
                }

                result.add(ExcelSubmitModel.builder()
                        .questionBh(String.valueOf(question.getId()))
                        .src(submit.getSrc())
                        .questionDesc(HtmlUtil.delHtmlTag(question.getQuestionDetails()))
                        .questionName(question.getQuestionName())
                        .score(Double.valueOf(score.getScore()) / 5)
                        .stage(StageEnum.getStageName(question.getStage()))
                        .examPaperId(examinationId)
                        .studentNumber(examInfo.getStudentNumber())
                        .studentName(examInfo.getStudentName())
                        .build());

                ExamGroupNew examGroupNew = examGroupRepo.findById(examInfo.getExamGroupId()).get();

                examDesc = examGroupNew.getExamDesc();
            }


            /**
             * private String questionBh;
             *     private String questionName;
             *     private String stage;
             *     private String questionDesc;
             *     private String src;
             *     private Double score;
             */

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
            writer.addHeaderAlias("score", "成绩");

            result.add(ExcelSubmitModel.builder().score(Double.valueOf(examInfo.getExaminationScore())).build());
            LOGGER.info("submit" + result.toString());
            List<ExcelSubmitModel> rows = CollUtil.newArrayList(result);
            writer.write(rows, true);
            writer.flush(byteArrayInputStream, true);
            writer.close();
            excel.put(examDesc + "_" + examInfo.getStudentNumber() + "_" + examInfo.getStudentName() + ".xls", byteArrayInputStream);

            System.out.println(byteArrayInputStream.size());
            byteArrayInputStream.close();
        }
        ByteArrayOutputStream zip = ZipUtil.zip(excel, new File("d://test8.zip"));

        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(examDesc + "_学生提交记录.zip", "utf-8"));
        response.setContentType("application/zip");
        response.getOutputStream().write(zip.toByteArray());


//        response.setContentType("application/vnd.ms-excel;charset=utf-8");
//        String value = "attachment;filename=" + URLEncoder.encode(examDesc + "_学生提交记录导出（总）" + ".xls", "UTF-8");
//        response.setHeader("Content-Disposition", value);
//        ServletOutputStream outputStream = response.getOutputStream();
//
//        writer.flush(outputStream, true);
//        writer.close();

        return null;


    }


    @Override
    public List<ExcelSubmitModel> exportSubmitAll(HttpServletResponse response, Integer examGroupId) throws IOException {
        /**
         * examnationId --> examInfo
         * examGroupId-->submit
         * studentId,questionId-->score
         * question-->question_new
         */

        String examDesc = "";

        List<ExcelSubmitModel> result = new ArrayList<>();
        List<ExamInfo> examInfoByExamGroup = examInfoService.getExamInfoByExamGroup(examGroupId);

        for (ExamInfo examInfo : examInfoByExamGroup) {
            Integer examinationId = examInfo.getExaminationId();
            /**
             * 通过试卷id获取 题目编号
             */
            List<String> questionBhList = questionService.getQuestionBhList(examinationId);

            List<Submit> submitList = new ArrayList<>();
            for (String questionBh : questionBhList) {
                Submit lastSubmit = submitRepo.getLastSubmit(examinationId, questionBh);
                if (lastSubmit == null) {

                    submitList.add(Submit.builder().questionId(questionBh).studentId(Long.valueOf(examInfo.getStudentNumber())).src("未提交").build());
                } else {
                    submitList.add(lastSubmit);
                }
            }
            for (Submit submit : submitList) {
                QuestionNew question = questionRepo.findByQuestionBh(submit.getQuestionId());
                Score score = scoreRepo.findByStudentIdAndQuestionId(examInfo.getStudentNumber(), question.getQuestionBh());
                if (score == null) {
                    score = new Score();
                    score.setScore(0);
                }

                result.add(ExcelSubmitModel.builder()
                        .questionBh(String.valueOf(question.getId()))
                        .src(submit.getSrc())
                        .questionDesc(HtmlUtil.delHtmlTag(question.getQuestionDetails()))
                        .questionName(question.getQuestionName())
                        .score(Double.valueOf(score.getScore()) / 5)
                        .stage(StageEnum.getStageName(question.getStage()))
                        .examPaperId(examinationId)
                        .studentNumber(examInfo.getStudentNumber())
                        .studentName(examInfo.getStudentName())
                        .build());

                ExamGroupNew examGroupNew = examGroupRepo.findById(examInfo.getExamGroupId()).get();

                examDesc = examGroupNew.getExamDesc();
            }

            /**
             * private String questionBh;
             *     private String questionName;
             *     private String stage;
             *     private String questionDesc;
             *     private String src;
             *     private Double score;
             */


        }
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("studentNumber", "学号");
        writer.addHeaderAlias("studentName", "姓名");
        writer.addHeaderAlias("examPaperId", "试卷编号");
        writer.addHeaderAlias("questionBh", "题目编号");
        writer.addHeaderAlias("questionName", "题目名称");
        writer.addHeaderAlias("stage", "阶段");
        writer.addHeaderAlias("questionDesc", "题目描述");
        writer.addHeaderAlias("src", "学生代码");
        writer.addHeaderAlias("score", "成绩");
        LOGGER.info("submitAll" + result.toString());
        System.out.println(result.toString());
        List<ExcelSubmitModel> rows = CollUtil.newArrayList(result);
        writer.write(rows, true);

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String value = "attachment;filename=" + URLEncoder.encode(examDesc + "_学生提交记录导出（总）" + ".xls", "UTF-8");
        response.setHeader("Content-Disposition", value);
        ServletOutputStream outputStream = response.getOutputStream();

        writer.flush(outputStream, true);
        writer.close();
        IoUtil.close(outputStream);
        return result;
    }


    @Override
    public List<ScoreDetail> getScoreDetailByStudentId(Integer studentId) {
        List<ExamInfo> examInfoByStudentId = examInfoService.getExamInfoByStudentId(studentId);
        List<ScoreDetail> result = new ArrayList<>(examInfoByStudentId.size());
        examInfoByStudentId.forEach(examInfo -> {
            if (examInfo.getExaminationScore() != null) {
                ScoreDetail scoreDetail = new ScoreDetail();
                scoreDetail.setScore(Double.valueOf(examInfo.getExaminationScore()));
                scoreDetail.setExamName(examGroupRepo.findById(examInfo.getExamGroupId()).get().getExamDesc());
                result.add(scoreDetail);
            }
        });
        return result;
    }

    @Override
    public List<ScoreAdmin> getScoreAdminByExamGroupId(Integer examGroupId) {
        List<ExamInfo> examInfoByExamGroup = examInfoService.getExamInfoByExamGroup(examGroupId);
        List<ScoreAdmin> result = new ArrayList<>(examInfoByExamGroup.size());
        examInfoByExamGroup.forEach(examInfo -> {
            ScoreAdmin scoreAdmin = new ScoreAdmin();
            scoreAdmin.setExamDesc(examGroupRepo.findById(examInfo.getExamGroupId()).get().getExamDesc());
            scoreAdmin.setScore(examInfo.getExaminationScore());
            scoreAdmin.setStudentName(examInfo.getStudentName());
            scoreAdmin.setStudentId(examInfo.getStudentNumber());
            result.add(scoreAdmin);
        });
        return result;
    }


}
