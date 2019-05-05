package yangchen.exam.service.question;

import yangchen.exam.entity.Question;
import yangchen.exam.model.Category;

import java.util.List;

/**
 * @author yc
 */
public interface QuestionService {


    /**
     * 创建题目
     *
     * @param question
     * @return
     */
    Question createQuestion(Question question);

    /**
     * 修改题目
     *
     * @param question
     * @return
     */
    Question updateQuestion(Question question);

    /**
     * 删除题目，连同测试用例
     *
     * @param id
     * @return
     */
    Boolean deleteQuestion(Integer id);


    /**
     * 通过id查找题目
     *
     * @param id
     * @return
     */
    Question findQuestionById(Integer id);


    /**
     * 通过条件筛选题目
     *
     * @param category
     * @return
     */
    List<Question> findQuestionByCategory(String category);


    /**
     * 返回全部题目
     *
     * @return
     */
    List<Question> findQuestionAll();


}
