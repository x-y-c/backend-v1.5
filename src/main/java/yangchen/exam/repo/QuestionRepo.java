package yangchen.exam.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import yangchen.exam.entity.QuestionNew;

import java.util.List;

public interface QuestionRepo extends JpaRepository<QuestionNew, Integer> {

    List<QuestionNew> findByActivedIsTrue();

    Page<QuestionNew> findByActivedIsTrue(Pageable pageable);

    List<QuestionNew> findByStageAndDifficultyAndQuestionTypeAndActivedIsTrue(String stage, String difficulty, String questionType);

    QuestionNew findByQuestionBh(String questionBh);

    Page<QuestionNew> findByStageAndActivedIsTrue(String stage, Pageable pageable);

    List<QuestionNew> findByStageAndActivedIsTrue(String stage);

    Page<QuestionNew> findByIdAndActivedIsTrue(Integer id, Pageable pageable);

    Page<QuestionNew> findByCustomBhAndActivedIsTrue(String customBh, Pageable pageable);

    Page<QuestionNew> findByQuestionNameAndActivedIsTrue(String questionName, Pageable pageable);


//    @Transactional
//    @Modifying
//    void deleteQuestionNewByQuestionBh(String questionBh);


}
