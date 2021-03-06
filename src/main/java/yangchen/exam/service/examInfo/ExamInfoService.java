package yangchen.exam.service.examInfo;

/**
 * @author YC
 * @date 2019/5/6 21:23
 * O(∩_∩)O)
 */

import yangchen.exam.entity.ExamInfo;
import yangchen.exam.model.ExamInfoResult;

import java.sql.Timestamp;
import java.util.List;

/**
 * 试卷信息，如试卷分配给谁等信息；
 */
public interface ExamInfoService {

    /**
     * 添加试卷的分配信息
     *
     * @param examInfo
     * @return
     */
    ExamInfo addExamInfo(ExamInfo examInfo);


    /**
     * 通过学号查询试卷的分配情况
     *
     * @param studentId
     * @return
     */
    //全部
    List<ExamInfo> getExamInfoByStudentId(Integer studentId);

    //已结束
    List<ExamInfo> getEndedExamInfo(Integer studentId, Timestamp timestamp);

    //未开始
    List<ExamInfo> getUnstartExamInfo(Integer studentId, Timestamp timestamp);

    //进行中
    List<ExamInfo> getIngExamInfo(Integer studentId, Timestamp timestamp);

//    //已结束
//    List<ExamInfo> getFinishedExamInfo(Integer studentId);

    ExamInfoResult getExamInfoResultByExaminationId(Integer examinationId);

    ExamInfo getExamInfoByExaminationId(Integer examinationId);


    List<ExamInfo>getExamInfoByExamGroupId(Integer examGroupId);


    /**
     * 通过examGroup查询信息；
     *
     * @param examGroupId
     * @return
     */
    List<ExamInfo> getExamInfoByExamGroup(Integer examGroupId);

    Integer getTtl(Integer examinationId);
}
