package yangchen.exam.service.submit.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.Submit;
import yangchen.exam.repo.SubmitRepo;
import yangchen.exam.service.submit.SubmitService;

/**
 * @author YC
 * @date 2019/5/14 4:46
 * O(∩_∩)O)
 */
@Service
public class SubmitServiceImpl implements SubmitService {

    @Autowired
    private SubmitRepo submitRepo;

    @Override
    public Submit addSubmit(Submit submit) {
        return submitRepo.save(submit);
    }
}