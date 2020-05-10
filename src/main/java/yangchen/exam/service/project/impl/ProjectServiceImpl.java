package yangchen.exam.service.project.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yangchen.exam.entity.ProjectGroup;
import yangchen.exam.entity.ProjectInfo;
import yangchen.exam.entity.ProjectPaper;
import yangchen.exam.entity.StudentNew;
import yangchen.exam.model.JsonResult;
import yangchen.exam.model.ProjectParam;
import yangchen.exam.repo.*;
import yangchen.exam.service.project.ProjectService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private ProjectGroupRepo projectGroupRepo;

    @Autowired
    private ProjectPaperRepo projectPaperRepo;

    @Autowired
    private ProjectInfoRepo projectInfoRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Override
    public ProjectGroup createProject(ProjectParam projectParam) {
        ProjectGroup projectGroup = new ProjectGroup();
        ProjectPaper projectPaper = new ProjectPaper();

        //开始时间
        Timestamp beginTime = projectParam.getBeginTime();
        long bTime = beginTime.getTime();
        //结束时间
        Timestamp endTime = new Timestamp(bTime + projectParam.getTtl() * 1000 * 60 * 24);
        //考试班级
        List<String> gradeList = projectParam.getGrades();
        //教师id
        Integer teacherId = teacherRepo.findByTeacherName(projectParam.getTeacherName()).getId();
        //学生list
        List<StudentNew> studentList = new ArrayList<>();
        for (String grade : gradeList) {
            List<StudentNew> classMate = studentRepo.findByStudentGradeAndTeacherId(grade,teacherId);
            studentList.addAll(classMate);
        }

        /*=================================================================*/
        projectGroup.setStartTime(beginTime);
        projectGroup.setEndTime(endTime);
        projectGroup.setProjectTtl(projectParam.getTtl());
        projectGroup.setTeacherId(teacherId);
        projectGroup.setProjectName(projectParam.getHomeworkName());

        String gradeListString = gradeList.toString();
        gradeListString = gradeListString.replace("[","");
        gradeListString = gradeListString.replace("]","");

        projectGroup.setClassName(gradeListString);
        ProjectGroup project = projectGroupRepo.save(projectGroup);

        /*=================================================================*/
        List<Integer> questionList = projectParam.getHomework();

        String questionNameString = questionList.toString();
        questionNameString = questionNameString.replace("[","");
        questionNameString = questionNameString.replace("]","");

        projectPaper.setQuestionList(questionNameString);
        projectPaper.setQuestionSize(questionList.size());
        ProjectPaper paper = projectPaperRepo.save(projectPaper);

        /*=================================================================*/
        for(StudentNew studentNew:studentList){
            ProjectInfo projectInfo = new ProjectInfo();
            projectInfo.setProjectGroupId(project.getId());
            projectInfo.setStudentId(studentNew.getStudentId());
            projectInfo.setScore(0);
            projectInfo.setProjectPaperId(paper.getId());
            projectInfo.setFinished(Boolean.FALSE);
            projectInfoRepo.save(projectInfo);
        }

        return project;
    }
}
