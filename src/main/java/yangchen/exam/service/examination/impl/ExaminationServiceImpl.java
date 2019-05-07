package yangchen.exam.service.examination.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.*;
import yangchen.exam.repo.examinationRepo;
import yangchen.exam.service.examInfo.ExamInfoService;
import yangchen.exam.service.examination.ExamGroupService;
import yangchen.exam.service.examination.ExaminationService;
import yangchen.exam.service.question.QuestionService;
import yangchen.exam.service.student.studentService;
import yangchen.exam.util.RandomUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author YC
 * @date 2019/5/5 15:35
 * O(∩_∩)O)
 */
@Service
public class ExaminationServiceImpl implements ExaminationService {

    private static Logger LOGGER = LoggerFactory.getLogger(ExaminationServiceImpl.class);
    @Autowired
    private QuestionService questionService;

    @Autowired
    private studentService studentService;

    @Autowired
    private ExamGroupService examGroupService;

    @Autowired
    private examinationRepo examinationRepo;

    @Autowired
    private ExamInfoService examInfoService;


    @Override
    public Examination createExamInfo(String category) {

        List<Question> questionByCategory = questionService.findQuestionByCategory(category);
        StringBuilder titles = new StringBuilder();

        //这里其实只创建了一个人的考题，5道，用随机数工具保证不重复；
        Set random = RandomUtil.getRandom(0, questionByCategory.size() - 1, 5);
        for (Object s : random) {
            Question question = questionByCategory.get(Integer.valueOf(String.valueOf(s)));
            LOGGER.info(question.getQuestionName() + question.getCategory());
            titles.append(question.getId());
            titles.append(",");
        }

        Examination examination = new Examination();
        examination.setTitleType("小测验");
        examination.setActive(Boolean.TRUE);
        examination.setUsed(Boolean.FALSE);
        examination.setTitleId(titles.toString());

        examinationRepo.save(examination);

        return examination;


    }

    @Override
    public Examination createExamInfo(String category, Integer number) {
        List<Question> questionList = questionService.findQuestionByCategory(category);
        Set random = RandomUtil.getRandom(0, questionList.size() - 1, number);
        StringBuilder titles = new StringBuilder();
        for (Object s : random) {
            Question question = questionList.get(Integer.valueOf(String.valueOf(s)));
            LOGGER.info(question.getQuestionName() + "\n" + question.getCategory());
            titles.append(question.getId());
            titles.append(",");
        }
        Examination examination = new Examination();
        examination.setTitleType("小测验");
        examination.setActive(Boolean.TRUE);
        examination.setUsed(Boolean.FALSE);
        examination.setTitleId(titles.toString());

        examinationRepo.save(examination);

        return examination;

    }

    @Override
    public void createExam(String category, Integer number, List<String> grade, Timestamp startTime, Timestamp endTime,
                           Long ttl, String desc) {
        List<Question> questionListByCategory = questionService.findQuestionByCategory(category);
        List<Student> studentList = new ArrayList<>();


        //通过循环获取需要添加的全部学生；
        for (String studentGrade : grade) {
            studentList.addAll(studentService.getStudentListByGrade(studentGrade));
        }

        //这里调用 createExamInfo方法保存试卷题目到数据库
        for (Student student : studentList) {
            Examination examination = createExamInfo(category, number);


            //这里是试卷的分配情况，即该id的试卷分配给了谁
            ExamInfo examInfo = new ExamInfo();
            examInfo.setStudentName(student.getName());
            examInfo.setStudentNumber(student.getStudentId());
            examInfoService.addExamInfo(examInfo);


            //这里保存考试组的信息，即本次考试的班级，开始结束时间等信息；
            ExamGroup examGroup = new ExamGroup();
            examGroup.setBeginTime(startTime);
            examGroup.setEndTime(endTime);
            examGroup.setClassName(grade.toString());
            examGroup.setExamTime(ttl);
            examGroup.setDesc("软工专业期中考试");

            examGroupService.addExamGroup(examGroup);
        }


    }


    public Examination addExamination(Examination examination) {
        return examinationRepo.save(examination);
    }

    @Override
    public List<Examination> getUnUsedExamination() {
        return null;
    }

    @Override
    public List<Examination> getUsedExamination() {
        return null;
    }
}
