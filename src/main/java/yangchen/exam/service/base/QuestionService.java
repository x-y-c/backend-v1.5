package yangchen.exam.service.base;

import yangchen.exam.entity.Question;
import yangchen.exam.model.Category;

import java.util.List;

/**
 * @author yc
 */
public interface QuestionService {


    //创建题目
    Question createQuestion(Question question);

    //修改题目
    Question updateQuestion(Question question);

    //删除题目 通过 id删除题目
    Boolean deleteQuestion(Integer id);


    //查找题目
    Question findQuestionById(Integer id);


    //通过条件筛选题目 返回list对象
    List<Question> findQuesionByCategory(Category category);

}
