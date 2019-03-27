package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.Authority;

public interface authorityRepo extends JpaRepository<Authority,Integer> {
}
