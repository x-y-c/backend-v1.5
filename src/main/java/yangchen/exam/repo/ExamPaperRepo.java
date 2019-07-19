package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.ExamPaper;

import java.util.List;

public interface ExamPaperRepo extends JpaRepository<ExamPaper, Integer> {
    List<ExamPaper> findByFinished(Boolean finished);
}
