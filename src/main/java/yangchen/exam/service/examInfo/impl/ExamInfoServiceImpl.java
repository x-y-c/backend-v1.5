package yangchen.exam.service.examInfo.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.Question;
import yangchen.exam.entity.Student;
import yangchen.exam.service.examInfo.ExamInfoService;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.service.student.studentService;
import yangchen.exam.util.RandomUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author YC
 * @date 2019/5/5 15:35
 * O(∩_∩)O)
 */
@Service
public class ExamInfoServiceImpl implements ExamInfoService {

    private static Logger LOGGER = LoggerFactory.getLogger(ExamInfoServiceImpl.class);
    @Autowired
    private QuestionService questionService;

    @Autowired
    private studentService studentService;


    @Override
    public void createExamInfo(String category) {

        List<Question> questionByCategory = questionService.findQuestionByCategory(category);

        //这里其实只创建了一个人的考题，5道，用随机数工具保证不重复；
        Set random = RandomUtil.getRandom(0, questionByCategory.size() - 1, 5);
        for (Object s : random) {
            Question question = questionByCategory.get(Integer.valueOf(String.valueOf(s)));
            LOGGER.info(question.getQuestionName() + question.getCategory());
        }


    }

    @Override
    public void createExamInfo(String category, Integer number) {
        List<Question> questionList = questionService.findQuestionByCategory(category);
        Set random = RandomUtil.getRandom(0, questionList.size() - 1, number);
        for (Object s : random) {
            Question question = questionList.get(Integer.valueOf(String.valueOf(s)));
            LOGGER.info(question.getQuestionName() + "\n" + question.getCategory());
        }

    }

    @Override
    public void createExam(String category, Integer number, List<String> grade, Timestamp startTime, Timestamp endTime,
                           Long ttl, String desc) {
        List<Question> questionListByCategory = questionService.findQuestionByCategory(category);
        List<Student> studentList = new ArrayList<>();


        //通过循环获取需要添加的全部学生；
        for (String studentGrade : grade){
           studentList.addAll(studentService.getStudentListByGrade(studentGrade));
        }



    }
}
