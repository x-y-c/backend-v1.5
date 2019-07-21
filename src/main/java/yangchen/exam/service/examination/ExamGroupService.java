package yangchen.exam.service.examination;

import yangchen.exam.entity.ExamGroupNew;

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
}
