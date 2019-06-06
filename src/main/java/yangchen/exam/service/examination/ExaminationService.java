package yangchen.exam.service.examination;

import yangchen.exam.entity.Examination;
import yangchen.exam.model.ExamCreatedParam;
import yangchen.exam.model.ExaminationDetail;
import yangchen.exam.model.QuestionDetail;
import yangchen.exam.model.QuestionResult;

import java.sql.Timestamp;
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

    /**
     * 根据阶段创建题目，默认为5道题；
     *
     * @param category
     */
    Examination createExamInfo(String category);

    //全部考试
    List<ExaminationDetail> examInfoDetail(Long studentId);

    //结束的考试
    List<ExaminationDetail> getEndedExamination(Long studentId);

    //已完成的考试
    List<ExaminationDetail> getFinishedExamination(Long studentId);

    //未考试
    List<ExaminationDetail> getUnstartedExamination(Long studentId);

    List<ExaminationDetail> getIngExamination(Long studentId);

    /**
     * 根据阶段和题目数创建题目，
     *
     * @param category
     * @param number
     */
    Examination createExamInfo(String category, Integer number);


    /**
     * @param category  阶段 example ： 阶段3
     * @param number    题数 example： 5
     * @param grades    班级 example: 软工1501，软工1502
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param ttl       持续时间
     * @param desc      考卷描述
     */
    void createExam(String category, Integer number, List<String> grades, Timestamp startTime, Timestamp endTime,
                    Long ttl, String desc);


    void createExam(ExamCreatedParam examCreatedParam);


    /**
     * 获取已经作答过的考卷
     *
     * @return
     */
    List<Examination> getUnUsedExamination();

    /**
     * 获取未作答的考卷
     *
     * @return
     */
    List<Examination> getUsedExamination();


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
    QuestionResult getQuestionInfoResult(Integer id);


    Examination getExaminationById(Integer id);


    Boolean submitTest(Integer id, Long studentId);
}
