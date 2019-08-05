package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yangchen.exam.entity.Submit;

import java.util.List;

/**
 * @author YC
 * @date 2019/5/14 4:47
 * O(∩_∩)O)
 */
public interface SubmitRepo extends JpaRepository<Submit, Integer> {

    List<Submit> findByExaminationId(Integer examinationId);

    @Query(value = "select * from submit where examination_id = ?1 and question_id = ?2 order by id desc limit 0,1", nativeQuery = true)
    Submit getLastSubmit(Integer examinationId, String questionId);
}
