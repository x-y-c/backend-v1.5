package yangchen.exam.service.examInfo;

/**
 * @author YC
 * @date 2019/5/6 21:23
 * O(∩_∩)O)
 */

import yangchen.exam.entity.ExamInfo;
import yangchen.exam.model.ExaminationDetail;

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
    List<ExamInfo> getExamInfoByStudentId(Long studentId);
}
