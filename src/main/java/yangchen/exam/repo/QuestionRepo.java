package yangchen.exam.repo;

import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.QuestionNew;

import java.util.List;

public interface QuestionRepo extends JpaRepository<QuestionNew, Integer> {

    List<QuestionNew> findByActivedIsTrue();

    Page<QuestionNew> findAll(Pageable pageable);

    List<QuestionNew> findByStageAndDifficultyAndQuestionTypeAndActivedIsTrue(String stage, String difficulty, String questionType);

    QuestionNew findByQuestionBh(String questionBh);

    Page<QuestionNew> findByStage(String stage, Pageable pageable);

    List<QuestionNew> findByStageAndActivedIsTrue(String stage);
    List<QuestionNew> findByStageAndQuestionTypeAndActivedIsTrue(String stage,String questionType);

    Page<QuestionNew> findById(Integer id, Pageable pageable);

    Page<QuestionNew> findByCustomBhLike(String customBh, Pageable pageable);

    Page<QuestionNew> findByQuestionNameLike(String questionName, Pageable pageable);

    List<QuestionNew> findByQuestionTypeAndActivedIsTrue(String questionType);


//    List<QuestionNew> findByQuestionNameLike(String questionName);

//    @Transactional
//    @Modifying
//    void deleteQuestionNewByQuestionBh(String questionBh);


}
