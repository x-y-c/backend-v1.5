package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.IpAddr;

import java.util.List;

public interface IpAddrRepo extends JpaRepository<IpAddr, Integer> {

    List<IpAddr> findByExamGroupId(Integer examGroupId);


}
