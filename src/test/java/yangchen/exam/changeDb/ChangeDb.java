package yangchen.exam.changeDb;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yangchen.exam.entity.Questions;
import yangchen.exam.repo.QuestionNew;
import yangchen.exam.repo.QuestionNewRepo;
import yangchen.exam.repo.QuestionsRepo;

import java.util.List;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ChangeDb {
    @Autowired
    private QuestionsRepo questionsRepo;
    @Autowired
    private QuestionNewRepo questionNewRepo;


    @Test
    public void test() {

        List<Questions> all = questionsRepo.findAll();
        for (Questions questions : all) {
            if (!questions.getChecked().equals(String.valueOf(100002))) {
                QuestionNew questionNew = new QuestionNew();
                questionNew.setAddTime(questions.getAddTime());
                questionNew.setAnswer(questions.getAnswer());
                questionNew.setCustomBh(questions.getCustomBh());
                questionNew.setDifficulty(questions.getDifficulty());
                questionNew.setEndTag(questions.getEndTag());
                questionNew.setQuestionBh(questions.getQuestionBh());
                questionNew.setQuestionDescription(questions.getDescription());
                questionNew.setQuestionName(questions.getName());
                questionNew.setQuestionType(Integer.valueOf(questions.getQuestionType()));
                questionNew.setSourceCode(questions.getSourceCode());
                questionNew.setStage(questions.getStage());
                questionNew.setStartTag(questions.getStartTag());
                questionNewRepo.save(questionNew);
            }
        }

    }
}
