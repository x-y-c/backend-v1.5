package yangchen.exam.service.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.Question;
import yangchen.exam.repo.questionRepo;

@Service
public class QuestionBaseServiceImpl implements QuestionBaseService {

    @Autowired
    private questionRepo questionRepo;

    @Cacheable(value = "question")
    @Override
    public Question getQuestionById(Integer questionId) {
        return questionRepo.findById(questionId).get();

    }
}