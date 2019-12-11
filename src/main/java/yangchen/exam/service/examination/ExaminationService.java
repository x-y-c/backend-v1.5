package yangchen.exam.service.examination;

import yangchen.exam.entity.ExamGroupNew;
import yangchen.exam.entity.ExamInfo;
import yangchen.exam.entity.ExamPaper;
import yangchen.exam.model.*;

import java.util.List;

/**
 * @author YC
 * @date 2019/5/5 10:19
 * O(∩_∩)O)
 */

/**
 * 试卷信息类
 */
public interface ExaminationService {

    //全部考试
    List<ExaminationDetail> examInfoDetail(Integer studentId);

    //结束的考试
    List<ExaminationDetail> getEndedExamination(Integer studentId);

//    //已完成的考试
//    List<ExaminationDetail> getFinishedExamination(Integer studentId);

    //未考试
    List<ExaminationDetail> getUnstartedExamination(Integer studentId);

    List<ExaminationDetail> getIngExamination(Integer studentId);


    JsonResult createExam(ExamParam examParam);


    List<ExamPageInfo> getExamPageInfo(Integer examGroupId);

    ExamPaper getExampaperByExamPaper(Integer examPaperId);


    List<ExamPageInfo> getExamPageInfoFast(Integer examGroupId);

    /**
     * 获取已经作答过的考卷
     *
     * @return
     */
    List<ExamPaper> getUnUsedExamination();

    /**
     * 获取未作答的考卷
     *
     * @return
     */
    List<ExamPaper> getUsedExamination();


    /**
     * 通过试卷id获取试卷内容
     *
     * @param id
     * @return
     */
    List<QuestionDetail> getQuestionInfo(Integer id);

    /**
     * 通过试卷id获取试卷内容，并给返回值
     *
     * @param id
     * @return
     */
    JsonResult getQuestionInfoResult(Integer studentId, Integer id);


    ExamPaper getExaminationById(Integer id);


    Boolean submitTest(Integer id, Integer studentId,Integer sign);
}
