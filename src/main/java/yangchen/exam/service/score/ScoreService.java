package yangchen.exam.service.score;


import yangchen.exam.entity.Score;
import yangchen.exam.model.ExcelSubmitModel;
import yangchen.exam.model.ScoreAdmin;
import yangchen.exam.model.ScoreDetail;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

//examId
//examDesc
// totalScore
//score
public interface ScoreService {
    Score saveScore(Score score);

    List<Score> findByExaminationAndStudentId(Integer examination, Integer studentId);

    Score saveOrUpdate(Score score);


    void exportScore(HttpServletResponse response, Integer examGroupId) throws IOException;

    List<ExcelSubmitModel> exportSubmit(HttpServletResponse response, Integer examGroupId) throws IOException;

    List<ExcelSubmitModel> exportSubmitAll(HttpServletResponse response, Integer examGroupId) throws IOException;

    /**
     * 通过学号查询成绩
     *
     * @param studentId
     * @return
     */
    List<ScoreDetail> getScoreDetailByStudentId(Integer studentId);


    /**
     * 教师端查询成绩，是一对多的关系，老师可以看到一组考试中所有人的成绩；
     *
     * @param examinationId
     * @return
     */
    List<ScoreAdmin> getScoreAdminByExamGroupId(Integer examinationId);


    Score findByExaminationIdAndIndex(Integer examination,Integer number);

}
