package yangchen.exam.service.examination;

import yangchen.exam.entity.Examination;

import java.util.List;

/**
 * @author YC
 * @date 2019/5/6 16:32
 * O(∩_∩)O)
 */
public interface ExaminationService {

    Examination addExamination(Examination examination);

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
}
