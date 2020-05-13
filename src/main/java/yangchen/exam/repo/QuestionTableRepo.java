package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yangchen.exam.entity.StudentNew;

import java.util.List;

public interface QuestionTableRepo extends JpaRepository<StudentNew,Integer> {

    @Query(value = "select score.student_id,student_name,score,question_id from score join student_new join question_new where score.student_id = student_new.student_id and student_new.student_id like CONCAT(?1,'%') and question_new.questionBh = score.question_id and question_new.difficulty=?2 and question_new.stage=?3",nativeQuery = true)
    List<Object> showSearch(String studentId, String stage, String diff);
}
