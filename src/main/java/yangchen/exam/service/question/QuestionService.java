package yangchen.exam.service.question;

import org.springframework.data.domain.Page;
import yangchen.exam.entity.QuestionNew;
import yangchen.exam.model.*;
import yangchen.exam.entity.QuestionLog;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     */
    //QuestionNew saveQuestionWithImgDecode(QuestionUpdate questionUpdate) throws IOException;
    QuestionNew saveQuestionWithImgDecodeNew(QuestionUpdate questionUpdate) throws IOException;


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

    List<String> getQuestionBhList(Integer examPaperId);

    QuestionNew findByQuestionBh(String questionBh);

    Page<QuestionNew> getPageQuestion(Integer pageNo, Integer pageSize);

    Page<QuestionNew> getStageQuestionPage(String value, Integer pageNo, Integer pageSize);

    Page<QuestionNew> getIdQuestionPage(String value, Integer pageNo, Integer pageSize);

    Page<QuestionNew> getTitleQuestionPage(String value, Integer pageNo, Integer pageSize);

    Page<QuestionNew> getCustomBhQuestionPage(String value, Integer pageNo, Integer pageSize);

    List<QuestionNew> searchStage(String stage);


    QuestionPractice getQuestionPracticeInfo();

    QuestionDetail getPracticeItem(String questionBh,Integer studentId);

    String getPracticeFront(String questionBh);

    String getPracticeNext(String questionBh);

    QuestionLog addQuestionLog(QuestionUpdate questionNew,String flag);
    QuestionLog addQuestionLog(QuestionNew questionNew,String flag);

    List<QuestionLogModel> getQuestionLog();

    String getAnswer(String questionBh);

    List<QuestionNew> getQuestion(String questionType);


    HashMap<String,String> getQuestionInput2Output(String questionBh);

    List<String> getQuestionInput(String questionBh);
    List<String> getQuestionOutput(String questionBh);


    Boolean isChangeSrc(QuestionNew question,String src);


    List<ClassModel> getQuestionsAndId(String stage);

    List<QuestionNew> getProjectPaper(Integer projectPaperId);

    List<QuestionNew> getProjectPaperByProjectInfoId(Integer examInfoId);

}
