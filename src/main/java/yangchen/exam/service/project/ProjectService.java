package yangchen.exam.service.project;


import org.springframework.data.domain.Page;
import yangchen.exam.entity.ProjectGroup;
import yangchen.exam.entity.ProjectSubmit;
import yangchen.exam.model.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ProjectService {

   ProjectGroup createProject(ProjectParam projectParam);

   Page<ProjectGroup> getProjectPage(Integer page, Integer pageLimit, String teacherName);

   void deleteProject(Integer homeworkId);

   ProjectDetails getProjectDetails(Integer homeworkGroupId);

   List<ProjectScoreModel> getScoreByTeacher(Integer homeworkGroupId);

   //导出成绩
   void exportScore(HttpServletResponse response, Integer homeworkGroupId) throws IOException;

   //导出全部提交记录
   void exportSubmitAll(HttpServletResponse response, Integer homeworkGroupId) throws IOException;

   //导出个人提交记录
   void exportSubmit(HttpServletResponse response, Integer homeworkGroupId) throws IOException;

   ProjectSubmit addSubmit(ProjectSubmit projectSubmit);

   List<ExaminationDetail> getProjectDetail(Integer studentId);

   JsonResult getProjectPaper(Integer projectInfoId, Integer studentId);

   Boolean projectSubmitExamPaper(Integer projectInfoId, Integer studentId, Integer sign);

   long getEndTime(Integer examInfoId);
}
