package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.ModuleStatus;

public interface ModuleStatusRepo extends JpaRepository<ModuleStatus, Integer> {
    ModuleStatus findByModule(String module);
}
