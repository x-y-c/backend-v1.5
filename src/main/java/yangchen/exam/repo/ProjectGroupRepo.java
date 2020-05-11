package yangchen.exam.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.ProjectGroup;

public interface ProjectGroupRepo extends JpaRepository<ProjectGroup,Integer> {


    Page<ProjectGroup> findByTeacherId(Pageable pageable, Integer teacherId);


}
