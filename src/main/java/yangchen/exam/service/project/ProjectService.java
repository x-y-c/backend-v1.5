package yangchen.exam.service.project;


import org.springframework.data.domain.Page;
import yangchen.exam.entity.ProjectGroup;
import yangchen.exam.model.ProjectParam;

public interface ProjectService {

   ProjectGroup createProject(ProjectParam projectParam);

   Page<ProjectGroup> getProjectPage(Integer page, Integer pageLimit, String teacherName);
}
