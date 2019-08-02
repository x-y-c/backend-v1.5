package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.Authority;

public interface AuthorityRepo extends JpaRepository<Authority,Integer> {
}
