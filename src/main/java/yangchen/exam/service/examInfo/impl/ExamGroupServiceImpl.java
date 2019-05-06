package yangchen.exam.service.examInfo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.ExamGroup;
import yangchen.exam.repo.examGroupRepo;
import yangchen.exam.service.examInfo.ExamGroupService;

/**
 * @author YC
 * @date 2019/5/6 16:20
 * O(∩_∩)O)
 */
@Service
public class ExamGroupServiceImpl implements ExamGroupService {


    @Autowired
    private examGroupRepo examGroupRepo;

    @Override
    public ExamGroup addExamGroup(ExamGroup examGroup) {
        return examGroupRepo.save(examGroup);
    }

}

