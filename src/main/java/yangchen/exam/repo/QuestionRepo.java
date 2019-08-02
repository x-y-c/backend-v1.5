package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import yangchen.exam.entity.QuestionNew;

import java.util.List;
import java.util.Optional;

public interface QuestionRepo extends JpaRepository<QuestionNew, Integer> {

    Optional<QuestionNew> findById(Integer id);

    List<QuestionNew> findByStageAndDifficulty(String stage, String difficulty);

    QuestionNew findByQuestionBh(String questionBh);


    @Transactional
    @Modifying
    void deleteQuestionNewByQuestionBh(String questionBh);
}
