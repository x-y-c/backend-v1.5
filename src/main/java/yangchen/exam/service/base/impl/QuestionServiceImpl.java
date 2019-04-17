package yangchen.exam.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.Question;
import yangchen.exam.model.Category;
import yangchen.exam.repo.questionRepo;
import yangchen.exam.service.base.QuestionService;

import java.util.List;
import java.util.Optional;

/**
 * @author yc
 */
@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private questionRepo questionRepo;


    @Override
    public Question createQuestion(Question question) {
        Question save = questionRepo.save(question);
        return save;
    }

    @Override
    public Question updateQuestion(Question question) {
        Question save = questionRepo.save(question);
        return save;
    }


    //如果删除成功，则该id对应的对象不存在
    @Override
    public Boolean deleteQuestion(Integer id) {
        questionRepo.deleteById(id);
        return findQuestionById(id) == null;
    }

    @Override
    public Question findQuestionById(Integer id) {
        Optional<Question> result = questionRepo.findById(id);
        return result.get();


    }

    @Override
    public List<Question> findQuestionByCategory(Category category) {
        return questionRepo.findByCategory(category);
    }

    @Override
    public List<Question> findQuestionAll() {
        return questionRepo.findAll();
    }


}
