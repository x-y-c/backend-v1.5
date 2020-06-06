package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yangchen.exam.entity.Engineering;

import java.util.List;

public interface EngineeringRepo extends JpaRepository<Engineering,Integer> {
    Engineering findByStudentId(Integer studentId);

    @Query(value = "select distinct student_year from jt_c_exam.excelEngineering",nativeQuery = true)
    List<Integer> getYears();

    @Query(value = "select avg(score) from excelEngineering where student_id like CONCAT(?1,'%')",nativeQuery = true)
    Integer getExcelScore(Integer student_year);
}
