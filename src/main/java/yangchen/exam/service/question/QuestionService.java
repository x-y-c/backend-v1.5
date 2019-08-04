package yangchen.exam.service.question;

import org.springframework.data.domain.Page;
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.model.QuestionInfo;

import java.io.IOException;
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
     * @param questionBh
     * @return
     */
    void deleteQuestion(String questionBh);


    /**
     * 保存questionNew 信息，前端把图片以base64的形式传给后台，后台需要将base64--->image ，
     * 然后将pre_question_details 字段修改保存到 question_details
     *
     * @param questionNew
     * @return
     */
    QuestionNew saveQuestionWithImgDecode(QuestionNew questionNew) throws IOException;


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

    Page<QuestionNew> getPageQuestion(Integer pageNo, Integer pageSize);
}
