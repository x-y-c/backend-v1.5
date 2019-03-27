package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.ExamInfo;

public interface examInfoRepo extends JpaRepository<ExamInfo,Integer> {

}
