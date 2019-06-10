package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yangchen.exam.entity.Student;

import java.util.List;

public interface studentRepo extends JpaRepository<Student, Integer> {

    Student findByStudentId(Long studentId);

    List<Student> findByGrade(String grade);

    List<Student> findByMajor(String major);

    @Query(value = "select distinct grade from student",nativeQuery = true)
    List<String> getGrade();


}
