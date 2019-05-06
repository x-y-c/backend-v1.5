package yangchen.exam.service.examInfo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.ExamInfo;
import yangchen.exam.repo.examInfoRepo;
import yangchen.exam.service.examInfo.ExamInfoService;

/**
 * @author YC
 * @date 2019/5/6 21:37
 * O(∩_∩)O)
 */
@Service
public class ExamInfoServiceImpl implements ExamInfoService {

    @Autowired
    private examInfoRepo examInfoRepo;


    @Override
    public ExamInfo addExamInfo(ExamInfo examInfo) {
        return examInfoRepo.save(examInfo);
    }

    @Override
    public ExamInfo getExamInfoByStudentId(Long studentId) {
        return null;
    }
}
