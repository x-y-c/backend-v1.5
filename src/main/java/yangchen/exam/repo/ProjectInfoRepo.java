package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.ProjectInfo;

public interface ProjectInfoRepo extends JpaRepository<ProjectInfo,Integer> {
}
