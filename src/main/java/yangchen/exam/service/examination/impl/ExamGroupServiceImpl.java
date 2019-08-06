package yangchen.exam.service.examination.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.ExamGroupNew;
import yangchen.exam.repo.ExamGroupRepo;
import yangchen.exam.repo.ExamInfoRepo;
import yangchen.exam.repo.ExamPaperRepo;
import yangchen.exam.service.examination.ExamGroupService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YC
 * @date 2019/5/6 16:20
 * O(∩_∩)O)
 */
@Service
public class ExamGroupServiceImpl implements ExamGroupService {


    @Autowired
    private ExamGroupRepo examGroupRepo;

    @Autowired
    private ExamInfoRepo examInfoRepo;

    @Autowired
    private ExamPaperRepo examPaperRepo;

    @Override
    public ExamGroupNew addExamGroup(ExamGroupNew examGroup) {
        return examGroupRepo.save(examGroup);
    }

    @Override
    public List<ExamGroupNew> getAllExamGroup(Integer id) {
        if (id == null) {
            return examGroupRepo.findAll();
        }
        switch (id) {
            case 0:
                return examGroupRepo.getAllExamGroupDesc();
            case 1:
                return examGroupRepo.findByEndTimeBefore(new Timestamp(System.currentTimeMillis()));
            case 2:
                return examGroupRepo.findByBeginTimeAfter(new Timestamp(System.currentTimeMillis()));
            default:
                return examGroupRepo.findAll();
        }




    }

    @Override
    public List<ExamGroupNew> getExamGroup(Integer examGroupId) {
        List<ExamGroupNew> result = new ArrayList<>();
        result.add(examGroupRepo.findById(examGroupId).get());
        return result;

    }

    @Override
    public Page<ExamGroupNew> getPageExamGroup(int currentPage, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC,"id");

        Pageable pageable = PageRequest.of(currentPage,pageSize,sort);

        Page<ExamGroupNew> all = examGroupRepo.findAll(pageable);
        return all;
    }

    @Override
    public void deleteExamInfo(Integer id) {

        List<Integer> examPapers = examInfoRepo.searchExamPaper(id);
        examInfoRepo.deleteExamInfoByExamGroupId(id);
        examGroupRepo.deleteExamGroupNewById(id);
        examPaperRepo.deleteExamPaperByIdIn(examPapers);

    }

    @Override
    public ExamGroupNew updateExamInfo(Integer id,String examDesc, Integer examTime, Timestamp beginTime) {
        ExamGroupNew examGroupNew = examGroupRepo.updateExamGroup(examDesc,examTime,beginTime,id);
        return examGroupNew;
    }


}

