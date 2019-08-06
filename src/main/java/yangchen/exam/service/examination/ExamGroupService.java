package yangchen.exam.service.examination;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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

    List<ExamGroupNew> getAllExamGroup(Integer id);

    List<ExamGroupNew> getExamGroup(Integer examGroupId);

    Page<ExamGroupNew> getPageExamGroup(int currentPage, int pageSize);

    void deleteExamInfo(Integer id);

    void updateExamInfo(Integer id,String examDesc, Integer examTime, Timestamp beginTime);

}
