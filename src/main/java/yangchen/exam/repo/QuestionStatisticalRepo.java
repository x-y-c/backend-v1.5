package yangchen.exam.repo;

import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yangchen.exam.entity.QuestionNew;

import java.util.List;
//import yangchen.exam.entity.questionStatistical;

public interface QuestionStatisticalRepo extends JpaRepository<QuestionNew,Integer> {

    @Query(value = "select count(*) from question_new where stage=?1 and difficulty=?2",nativeQuery = true)
    Integer countnum(String stage, String diff);

//    @Query(value = "select submit.student_id, student_name, Score, QuestionBh from Questions join student_new join submit where submit.student_id = student_new.student_id and Questions.QuestionBh = submit.question_id and student_new.student_id like CONCAT(?1,'%') and Questions.stage=?2 and Questions.difficulty=?3",nativeQuery = true)
//    List<Object> showSearch(String studentId, String stage, String diff);
}
