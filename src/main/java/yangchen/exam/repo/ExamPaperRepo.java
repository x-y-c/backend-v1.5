package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.ExamPaper;

public interface ExamPaperRepo extends JpaRepository<ExamPaper, Integer> {
}
