package yangchen.exam.service.question;

import yangchen.exam.entity.QuestionNew;

public interface QuestionBaseService {
    QuestionNew getQuestionById(Integer questionId);
}
