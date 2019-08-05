package yangchen.exam.service.score.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.ExamInfo;
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.entity.Score;
import yangchen.exam.entity.Submit;
import yangchen.exam.model.ExcelScoreModel;
import yangchen.exam.model.ExcelSubmitModel;
import yangchen.exam.model.ScoreAdmin;
import yangchen.exam.model.ScoreDetail;
import yangchen.exam.repo.*;
import yangchen.exam.service.examInfo.ExamInfoService;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.service.score.ScoreService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
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

    @Override
    public Score saveScore(Score score) {
        return scoreRepo.save(score);
    }

    @Override
    public List<Score> findByExaminationAndStudentId(Integer examination, Integer studentId) {
        return scoreRepo.findByStudentIdAndExaminationId(studentId, examination);
    }


    public Score saveOrUpdate(Score score) {
        Score score1 = scoreRepo.findByStudentIdAndExaminationIdAndIndex(score.getStudentId(),
                score.getExaminationId(), score.getIndex());

        if (score1 != null) {
            score1.setScore(score.getScore());
            return scoreRepo.save(score1);
        } else {
            return scoreRepo.save(score);
        }

    }

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
        writer.addHeaderAlias("name", "姓名");
        writer.addHeaderAlias("grade", "班级");
        writer.addHeaderAlias("score", "成绩");
        writer.addHeaderAlias("studentID", "学号");
        writer.write(rows, true);

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String value = "attachment;filename=" + URLEncoder.encode(examGroupName + ".xls", "UTF-8");
        response.setHeader("Content-Disposition", value);
        ServletOutputStream outputStream = response.getOutputStream();

        writer.flush(outputStream, true);
        writer.close();
        IoUtil.close(outputStream);

    }


    //todo
    @Override
    public List<ExcelSubmitModel> exportSubmit(Integer examGroupId) throws IOException {
        /**
         * examnationId --> examInfo
         * examGroupId-->submit
         * studentId,questionId-->score
         * question-->question_new
         */

        List<ExcelSubmitModel> result = new ArrayList<>();
        List<ExamInfo> examInfoByExamGroup = examInfoService.getExamInfoByExamGroup(examGroupId);

        for (ExamInfo examInfo : examInfoByExamGroup) {
            Integer examinationId = examInfo.getExaminationId();
            /**
             * 通过试卷id获取 题目编号
             */
            List<String> questionBhList = questionService.getQuestionBhList(examinationId);


//            List<Submit> submitList = submitRepo.findByExaminationId(examinationId);
            List<Submit> submitList = new ArrayList<>();
            for (String questionBh : questionBhList) {
                Submit lastSubmit = submitRepo.getLastSubmit(examinationId, questionBh);
                if (lastSubmit == null) {

                    submitList.add(Submit.builder().questionId(questionBh).studentId(Long.valueOf(examInfo.getStudentNumber())).build());
                } else {
                    submitList.add(lastSubmit);
                }
            }
            for (Submit submit : submitList) {
                QuestionNew question = questionRepo.findByQuestionBh(submit.getQuestionId());
                Score score = scoreRepo.findByStudentIdAndQuestionId(examInfo.getStudentNumber(), question.getQuestionBh());
                if (score == null) {
                    score.setScore(0);
                }

                result.add(ExcelSubmitModel.builder()
                        .questionBh(submit.getQuestionId())
                        .src(submit.getSrc())
                        .questionDesc(question.getQuestionDescription())
                        .questionName(question.getQuestionName())
                        .score(Double.valueOf(score.getScore()))
                        .build());

            }

        }
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
                scoreDetail.setExamName(examInfo.getDesc());
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
            scoreAdmin.setExamDesc(examInfo.getDesc());
            scoreAdmin.setScore(examInfo.getExaminationScore());
            scoreAdmin.setStudentName(examInfo.getStudentName());
            scoreAdmin.setStudentId(examInfo.getStudentNumber());
            result.add(scoreAdmin);
        });
        return result;
    }


}
