package yangchen.exam.service.examination;

import yangchen.exam.entity.ExamGroup;

import java.util.List;

/**
 * @author YC
 * @date 2019/5/6 16:11
 * O(∩_∩)O)
 */
public interface ExamGroupService {
    ExamGroup addExamGroup(ExamGroup examGroup);

    List<ExamGroup> getAllExamGroup(Integer id);
}
