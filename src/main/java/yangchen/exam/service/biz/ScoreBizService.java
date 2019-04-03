package yangchen.exam.service.biz;


import yangchen.exam.model.ScoreInfo;

import java.util.List;

//examId
//examDesc
// totalScore
//score
public interface ScoreBizService {
    List<ScoreInfo> getGradeStudentScore(String grade, Integer examId);

    ScoreInfo getScore(Long studentId, Integer examId);

}
