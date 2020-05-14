package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yangchen.exam.entity.ProjectSubmit;

public interface ProjectSubmitRepo extends JpaRepository<ProjectSubmit,Integer> {

    @Query(value = "select * from project_submit where student_id = ?1 and project_paper_id = ?2 and question_index = ?3 order by id desc limit 0,1", nativeQuery = true)
    ProjectSubmit getLastSubmit(Integer studentId, Integer examPaperId,Integer questionIndex);
}
