package yangchen.exam.service.score;


import yangchen.exam.entity.Score;
import yangchen.exam.model.ScoreAdmin;
import yangchen.exam.model.ScoreDetail;

import java.util.List;

//examId
//examDesc
// totalScore
//score
public interface ScoreService {
    Score saveScore(Score score);

    List<Score> findByExaminationAndStudentId(Integer examination, Long studentId);

    Score saveOrUpdate(Score score);


    /**
     * 通过学号查询成绩
     *
     * @param studentId
     * @return
     */
    List<ScoreDetail> getScoreDetailByStudentId(Long studentId);


    /**
     * 教师端查询成绩，是一对多的关系，老师可以看到一组考试中所有人的成绩；
     *
     * @param examinationId
     * @return
     */
    List<ScoreAdmin> getScoreAdminByExamGroupId(Integer examinationId);


}
