package yangchen.exam.service.question.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.repo.QuestionRepo;
import yangchen.exam.service.question.QuestionBaseService;

@Service
public class QuestionBaseServiceImpl implements QuestionBaseService {

    @Autowired
    private QuestionRepo questionRepo;

    @Cacheable(value = "question")
    @Override
    public QuestionNew getQuestionById(Integer questionId) {
        return questionRepo.findById(questionId).get();

    }
}
