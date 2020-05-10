package yangchen.exam.service.project;


import yangchen.exam.entity.ProjectGroup;
import yangchen.exam.model.ProjectParam;

public interface ProjectService {

   ProjectGroup createProject(ProjectParam projectParam);
}
