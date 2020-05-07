package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yangchen.exam.entity.StudentNew;

import java.util.List;

public interface QuestionTableRepo extends JpaRepository<StudentNew,Integer> {

    @Query(value = "select submit.student_id,student_name,Score,QuestionBh from Questions join student_new join submit where submit.student_id = student_new.student_id and student_new.student_id like CONCAT(?1,'%') and Questions.QuestionBh = submit.question_id and Questions.difficulty=?2 and Questions.stage=?3",nativeQuery = true)
    List<Object> showSearch(String studentId, String stage, String diff);
}
