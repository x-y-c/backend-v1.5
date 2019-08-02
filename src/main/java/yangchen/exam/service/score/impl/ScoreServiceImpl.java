package yangchen.exam.service.score.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.ExamInfo;
import yangchen.exam.entity.Score;
import yangchen.exam.model.ScoreAdmin;
import yangchen.exam.model.ScoreDetail;
import yangchen.exam.repo.ScoreRepo;
import yangchen.exam.repo.StudentRepo;
import yangchen.exam.service.examInfo.ExamInfoService;
import yangchen.exam.service.score.ScoreService;

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
    private ExamInfoService examInfoService;

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
