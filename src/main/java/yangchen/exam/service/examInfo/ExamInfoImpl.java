package yangchen.exam.service.examInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.Question;
import yangchen.exam.service.question.QuestionService;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author YC
 * @date 2019/5/5 15:35
 * O(∩_∩)O)
 */
@Service
public class ExamInfoImpl implements ExamInfo {

    @Autowired
    private QuestionService questionService;


    @Override
    public void createExamInfo(String category) {

        List<Question> questionByCategory = questionService.findQuestionByCategory(category);




    }

    @Override
    public void createExamInfo(String category, Integer number) {

    }

    @Override
    public void createExam(String category, Integer number, String grade, Timestamp startTime, Timestamp endTime, Long ttl) {

    }
}
