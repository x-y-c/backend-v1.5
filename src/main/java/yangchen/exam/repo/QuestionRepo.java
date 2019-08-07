package yangchen.exam.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import yangchen.exam.entity.QuestionNew;

import java.util.List;
import java.util.Optional;

public interface QuestionRepo extends JpaRepository<QuestionNew, Integer> {

    Optional<QuestionNew> findById(Integer id);

    List<QuestionNew> findByStageAndDifficulty(String stage, String difficulty);

    List<QuestionNew> findByStageAndDifficultyAndQuestionType(String stage, String difficulty, String questionType);

    QuestionNew findByQuestionBh(String questionBh);

    Page<QuestionNew> findByStage(String stage, Pageable pageable);

    List<QuestionNew> findByStage(String stage);


    @Transactional
    @Modifying
    void deleteQuestionNewByQuestionBh(String questionBh);
}
