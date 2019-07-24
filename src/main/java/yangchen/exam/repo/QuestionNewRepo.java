package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.QuestionNew;

public interface QuestionNewRepo extends JpaRepository<QuestionNew,Integer> {

}
