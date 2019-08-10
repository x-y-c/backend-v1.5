package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.IpAddr;

public interface IpAddrRepo extends JpaRepository<IpAddr,Integer> {



}
