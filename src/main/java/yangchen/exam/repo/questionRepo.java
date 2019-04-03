package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.Question;
import yangchen.exam.model.Category;

import java.util.List;
import java.util.Optional;

public interface questionRepo extends JpaRepository<Question, Integer> {

    Optional<Question> findById(Integer id);

    List<Question> findByCategory(Category category);
}
