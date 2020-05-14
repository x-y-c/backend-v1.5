package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import yangchen.exam.entity.ProjectPaper;

import java.util.List;

public interface ProjectPaperRepo extends JpaRepository<ProjectPaper,Integer>{


    @Transactional
    @Modifying
    void deleteProjectPaperByIdIn(List<Integer> id);

    @Query(value = "select question_list from project_paper where id =?1", nativeQuery = true)
    String getQuestions(Integer projectPaperId);
}
