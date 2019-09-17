package yangchen.exam.service.examination;


import org.springframework.data.domain.Page;
import yangchen.exam.entity.ExamGroupNew;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author YC
 * @date 2019/5/6 16:11
 * O(∩_∩)O)
 */
public interface ExamGroupService {
    ExamGroupNew addExamGroup(ExamGroupNew examGroup);

    List<ExamGroupNew> getAllExamGroup(String teacherName,Integer id);

    List<ExamGroupNew> getExamGroup(Integer examGroupId);

    Page<ExamGroupNew> getPageExamGroup(int currentPage, int pageSize);

    Page<ExamGroupNew> getPageExamGroupByTeacher(int currentPage, int pageSize, String teacher);

    void deleteExamInfo(Integer id);

    void updateExamInfo(Integer id, String examDesc, Integer examTime, Timestamp beginTime);

}
