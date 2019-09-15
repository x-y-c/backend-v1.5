package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yangchen.exam.entity.SubmitPractice;

public interface SubmitPracticeRepo extends JpaRepository<SubmitPractice, Integer> {
    @Query(value = "select * from submit_practice where question_id=?1 and student_id=?2 order by id desc limit 0,1", nativeQuery = true)
    SubmitPractice getSubmitLast(String questionId, Integer studentId);

}
