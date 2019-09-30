package yangchen.exam.service.submit.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.StudentNew;
import yangchen.exam.entity.Submit;
import yangchen.exam.entity.SubmitPractice;
import yangchen.exam.entity.Teacher;
import yangchen.exam.model.SubmitPracticeModel;
import yangchen.exam.repo.*;
import yangchen.exam.service.submit.SubmitService;
import yangchen.exam.util.PageUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YC
 * @date 2019/5/14 4:46
 * O(∩_∩)O)
 */
@Service
public class SubmitServiceImpl implements SubmitService {

    @Autowired
    private SubmitRepo submitRepo;

    @Autowired
    private SubmitPracticeRepo submitPracticeRepo;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private ScoreRepo scoreRepo;
    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private TeachClassInfoRepo teachClassInfoRepo;

    @Override
    public Submit addSubmit(Submit submit) {

        submit.setSubmitTime(new Timestamp(System.currentTimeMillis()));
        return submitRepo.save(submit);
    }


    @Override
    public Page<SubmitPracticeModel> getSubmitPracticeList(String teacherName, Integer pageNum, Integer pageLimit) {
        Teacher teacher = teacherRepo.findByTeacherName(teacherName);

        List<Integer> studentNumberList = studentRepo.getStudentNumberByTeacherId(teacher.getId());
        List<SubmitPractice> submitPractices = submitPracticeRepo.findByStudentIdInOrderByIdDesc(studentNumberList);
        return convertListToPage(submitPractices, pageNum, pageLimit);
    }

    @Override
    public Page<SubmitPracticeModel> getSubmitPracticeListCondition(String condition, String value, String teacherName, Integer pageNum, Integer pageLimit) {
        Page<SubmitPracticeModel> models = submitPracticeModelPageByCondition(condition, value, teacherName, pageNum, pageLimit);
        return models;
    }


    private Page<SubmitPracticeModel> submitPracticeModelPageByCondition(String condition, String value, String teacherName, Integer pageNum, Integer pageLimit) {
        Teacher teacher = teacherRepo.findByTeacherName(teacherName);
        List<Integer> studentNumberList = studentRepo.getStudentNumberByTeacherId(teacher.getId());
        List<SubmitPractice> submitPractices = null;
        switch (condition) {
            case "学号":
                if (studentNumberList.contains(Integer.valueOf(value))) {
                    submitPractices = submitPracticeRepo.findByStudentIdOrderByIdDesc(Integer.valueOf(value));
                    Page<SubmitPracticeModel> submitPracticeModels = convertListToPage(submitPractices, pageNum, pageLimit);
                    return submitPracticeModels;
                } else {
                    submitPractices = submitPracticeRepo.findByStudentIdOrderByIdDesc(-1);
                    Page<SubmitPracticeModel> submitPracticeModels = convertListToPage(submitPractices, pageNum, pageLimit);
                    return submitPracticeModels;
                }
            case "题号":
                submitPractices = submitPracticeRepo.findByQuestionIdAndStudentIdInOrderByIdDesc(value, studentNumberList);
                return convertListToPage(submitPractices, pageNum, pageLimit);
            case "姓名":
                StudentNew studentNew = studentRepo.findByStudentName(value);
                if (studentNumberList.contains(studentNew.getStudentId())) {
                    submitPractices = submitPracticeRepo.findByStudentIdOrderByIdDesc(studentNew.getStudentId());
                } else {
                    submitPractices = submitPracticeRepo.findByStudentIdOrderByIdDesc(-1);
                }
                return convertListToPage(submitPractices, pageNum, pageLimit);
            default:
                submitPractices = submitPracticeRepo.findAll();
                return convertListToPage(submitPractices, pageNum, pageLimit);

        }
    }

    private Page<SubmitPracticeModel> convertListToPage(List<SubmitPractice> submitPractices, Integer pageNum, Integer pageLimit) {
        List<SubmitPracticeModel> submitPracticeModelList = new ArrayList<>();
        submitPractices.forEach(submitPractice -> {
            SubmitPracticeModel submitPracticeModel = new SubmitPracticeModel();
            submitPracticeModel.setQuestionId(Integer.valueOf(submitPractice.getQuestionId()));
            submitPracticeModel.setScore(submitPractice.getScore());
            submitPracticeModel.setSrc(submitPractice.getSrc());
            submitPracticeModel.setStudentId(submitPractice.getStudentId());
            submitPracticeModel.setStudentName(studentRepo.findByStudentId(submitPractice.getStudentId()).getStudentName());
            submitPracticeModel.setSubmitTime(submitPractice.getSubmitTime());
            submitPracticeModelList.add(submitPracticeModel);
        });
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNum - 1, pageLimit, sort);
        Page<SubmitPracticeModel> submitPracticeModelPage = PageUtil.listConvertToPage(submitPracticeModelList, pageable);
        return submitPracticeModelPage;

    }
}
