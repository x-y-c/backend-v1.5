package yangchen.exam.service.examination.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.Examination;
import yangchen.exam.repo.examinationRepo;
import yangchen.exam.service.examination.ExaminationService;

import java.util.List;

/**
 * @author YC
 * @date 2019/5/6 16:50
 * O(∩_∩)O)
 */

@Service
public class ExaminationServiceImpl implements ExaminationService {
    @Autowired
    private examinationRepo examinationRepo;

    @Override
    public Examination addExamination(Examination examination) {
        return examinationRepo.save(examination);
    }

    @Override
    public List<Examination> getUnUsedExamination() {
        return examinationRepo.findByUsed(Boolean.FALSE);
    }

    @Override
    public List<Examination> getUsedExamination() {
        return examinationRepo.findByUsed(Boolean.TRUE);
    }
}
