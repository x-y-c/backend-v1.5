package yangchen.exam.service.project;


import org.springframework.data.domain.Page;
import yangchen.exam.entity.ProjectGroup;
import yangchen.exam.model.ExaminationDetail;
import yangchen.exam.model.ProjectDetails;
import yangchen.exam.model.ProjectParam;

import java.util.List;

public interface ProjectService {

   ProjectGroup createProject(ProjectParam projectParam);

   Page<ProjectGroup> getProjectPage(Integer page, Integer pageLimit, String teacherName);

   void deleteProject(Integer homeworkId);

   ProjectDetails getProjectDetails(Integer homeworkGroupId);


}
