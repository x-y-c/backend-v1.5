package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yangchen.exam.entity.Score;

import java.util.List;

/**
 * @author YC
 * @date 2019/6/1 15:53
 * O(∩_∩)O)
 */
public interface ScoreRepo extends JpaRepository<Score, Integer> {
    /**
     * 通过学号和考试标号查成绩；
     *
     * @param studentId
     * @param examinationId
     * @return
     */
    List<Score> findByStudentIdAndExaminationId(Integer studentId, Integer examinationId);

    Score findByStudentIdAndExaminationIdAndIndexAndQuestionId(Integer studentId, Integer examinationId, Integer index,String questionBh);

    //Score findByStudentIdAndQuestionId(Integer studentId,String questionId);

    Score findByExaminationIdAndIndex(Integer examinationId,Integer number);


    List<Score> findByStudentIdAndQuestionIdAndExaminationId(Integer studentId,String questionId,Integer examinationId);

    @Query(value = "select avg(score) from score where student_id like CONCAT(?1,'%')",nativeQuery = true)
    Integer getExamScore(Integer student_year);
}
