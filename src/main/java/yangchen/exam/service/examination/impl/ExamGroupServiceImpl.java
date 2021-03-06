package yangchen.exam.service.examination.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yangchen.exam.entity.ExamGroupNew;
import yangchen.exam.entity.Teacher;
import yangchen.exam.repo.ExamGroupRepo;
import yangchen.exam.repo.ExamInfoRepo;
import yangchen.exam.repo.ExamPaperRepo;
import yangchen.exam.repo.TeacherRepo;
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

    @Autowired
    private TeacherRepo teacherRepo;

    @Override
    public ExamGroupNew addExamGroup(ExamGroupNew examGroup) {
        return examGroupRepo.save(examGroup);
    }

    @Override
    public List<ExamGroupNew> getAllExamGroup(String teacherName, Integer id) {
        Teacher teacher = teacherRepo.findByTeacherName(teacherName);
        if (id == null) {
            return examGroupRepo.findByExamTeacher(teacher.getId().toString());
        }
        switch (id) {
            case 0:
                return examGroupRepo.getAllExamGroupAndExamTeacherDesc(teacher.getId().toString());
            case 1:
                return examGroupRepo.findByEndTimeBeforeAndExamTeacher(new Timestamp(System.currentTimeMillis()), teacher.getId().toString());
            case 2:
                return examGroupRepo.findByBeginTimeAfterAndExamTeacher(new Timestamp(System.currentTimeMillis()), teacher.getId().toString());
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
        Sort sort = new Sort(Sort.Direction.DESC, "id");

        Pageable pageable = PageRequest.of(currentPage, pageSize, sort);

        Page<ExamGroupNew> all = examGroupRepo.findAll(pageable);
        return all;
    }


    public Page<ExamGroupNew> getPageExamGroupByTeacher(int currentPage, int pageSize, String teacher) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(currentPage, pageSize, sort);
        Teacher teacher1 = teacherRepo.findByTeacherName(teacher);
        Page<ExamGroupNew> examTeacher = examGroupRepo.findByExamTeacher(pageable, String.valueOf(teacher1.getId()));
        return examTeacher;
    }


    @Override
    public void deleteExamInfo(Integer id) {

        List<Integer> examPapers = examInfoRepo.searchExamPaper(id);
        examInfoRepo.deleteExamInfoByExamGroupId(id);
        examGroupRepo.deleteExamGroupNewById(id);
        examPaperRepo.deleteExamPaperByIdIn(examPapers);

    }

    @Override
    @Transactional
    public void updateExamInfo(Integer id, String examDesc, Integer examTime, Timestamp beginTime) {
        Timestamp endTime = new Timestamp(beginTime.getTime() + examTime * 60 * 1000);
        examGroupRepo.updateExamGroup(examDesc, examTime, beginTime, endTime, id);
    }


}

