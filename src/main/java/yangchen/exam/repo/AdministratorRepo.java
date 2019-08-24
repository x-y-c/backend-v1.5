package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.Administrator;

public interface AdministratorRepo extends JpaRepository<Administrator, Integer> {

}
