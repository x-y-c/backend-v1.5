package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.QuestionNew;

import java.util.List;
import java.util.Optional;

public interface questionRepo extends JpaRepository<QuestionNew, Integer> {

    Optional<QuestionNew> findById(Integer id);


    List<QuestionNew> findByStageAndDifficulty(Integer stage, Integer difficulty);

    QuestionNew findByQuestionBh(String questionBh);
}
