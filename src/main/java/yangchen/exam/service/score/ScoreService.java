package yangchen.exam.service.score;


import yangchen.exam.entity.Score;

import java.util.List;

//examId
//examDesc
// totalScore
//score
public interface ScoreService {
    Score saveScore(Score score);

    List<Score> findByExaminationAndStudentId(Integer examination, Long studentId);
    Score saveOrUpdate(Score score);
}
