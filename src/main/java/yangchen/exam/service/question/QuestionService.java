package yangchen.exam.service.question;

import yangchen.exam.entity.QuestionNew;
import yangchen.exam.model.QuestionInfo;

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
    QuestionNew createQuestion(QuestionNew question);

    /**
     * 修改题目
     *
     * @param question
     * @return
     */
    QuestionNew updateQuestion(QuestionNew question);

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
    QuestionNew findQuestionById(Integer id);


    /**
     * 返回全部题目
     *
     * @return
     */
    List<QuestionNew> findQuestionAll();


    QuestionNew getQuestionBy(Integer examinationId, Integer index);


    List<QuestionInfo> getQuestionNamesByExamPage(Integer examPageId);


    QuestionNew findByQuestionBh(String questionBh);
}
