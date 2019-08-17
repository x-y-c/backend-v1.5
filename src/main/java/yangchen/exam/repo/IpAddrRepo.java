package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.IpAddr;

import java.sql.Timestamp;
import java.util.List;

public interface IpAddrRepo extends JpaRepository<IpAddr, Integer> {

    List<IpAddr> findByExamGroupIdAndLoginTimeBefore(Integer examGroupId, Timestamp endTime);

    List<IpAddr> findByExamGroupIdAndStudentIdAndLoginTimeBefore(Integer examGroupId, Integer studentNumber,
                                                                 Timestamp endTime);


}
