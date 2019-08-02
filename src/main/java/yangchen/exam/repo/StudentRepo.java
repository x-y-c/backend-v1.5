package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yangchen.exam.entity.StudentNew;

import java.util.List;

public interface StudentRepo extends JpaRepository<StudentNew, Integer> {

    StudentNew findByStudentId(Integer studentId);

    List<StudentNew> findByStudentGrade(String grade);


    @Query(value = "select distinct student_grade from student_new",nativeQuery = true)
    List<String> getGrade();


}
