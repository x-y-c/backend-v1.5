package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.ExamInfo;

import java.util.List;

public interface examInfoRepo extends JpaRepository<ExamInfo, Integer> {

    List<ExamInfo> findByStudentNumber(Long studentId);

}
