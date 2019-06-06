package yangchen.exam.service.examInfo;

/**
 * @author YC
 * @date 2019/5/6 21:23
 * O(∩_∩)O)
 */

import yangchen.exam.entity.ExamInfo;

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
    List<ExamInfo> getExamInfoByStudentId(Long studentId);

    //已结束
    List<ExamInfo> getEndedExamInfo(Long studentId, Timestamp timestamp);

    //未开始
    List<ExamInfo> getUnstartExamInfo(Long studentId, Timestamp timestamp);

    //进行中
    List<ExamInfo> getIngExamInfo(Long studentId, Timestamp timestamp);

    //已结束
    List<ExamInfo> getFinishedExamInfo(Long studentId);

    ExamInfo getExamInfoByExaminationId(Integer examinationId);


    /**
     * 通过examGroup查询信息；
     *
     * @param examGroupId
     * @return
     */
    List<ExamInfo> getExamInfoByExamGroup(Integer examGroupId);
}
