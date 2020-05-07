package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yangchen.exam.entity.SubmitPractice;

public interface StudentStatisticalRepo extends JpaRepository<SubmitPractice,Integer> {

    @Query(value = "select count(*) from question_new join submit_practice where student_id = ?1 and stage = ?2 and submit_practice.question_id = question_new.id",nativeQuery = true)
    Integer countstage(Integer student_id, String stage);
}
