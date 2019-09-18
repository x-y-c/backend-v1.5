package yangchen.exam.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import yangchen.exam.entity.StudentNew;

import java.util.List;

public interface StudentRepo extends JpaRepository<StudentNew, Integer> {

    StudentNew findByStudentId(Integer studentId);

    List<StudentNew> findByStudentGrade(String grade);


    List<StudentNew>findByStudentGradeAndTeacherId(String grade,Integer teacherId);


    @Query(value = "select distinct student_grade from student_new", nativeQuery = true)
    List<String> getGrade();

    @Query(value = "select student_grade from student_new where student_id=?1", nativeQuery = true)
    String getStudentGrade(Integer studentId);


    Page<StudentNew> findByStudentGrade(String grade, Pageable pageable);


    Page<StudentNew> findByStudentGradeIn(List<String> grades, Pageable pageable);


    @Query(value = "select student_id from student_new where student_grade in ?1", nativeQuery = true)
    List<Integer> getStudentIdByGrade(List<String> grades);

    @Transactional
    @Modifying
    void deleteStudentNewById(Integer id);


    Page<StudentNew> findByTeacherId(Integer id, Pageable pageable);

    Page<StudentNew> findByTeacherIdAndStudentGrade(Integer teacherId, String grade, Pageable pageable);

    @Query(value = "select distinct  student_grade from student_new where teacher_id=?1", nativeQuery = true)
    List<String> getGradeByTeacherId(Integer teacherId);


    @Query(value = "select student_id from student_new where teacher_id=?1",nativeQuery = true)
    List<Integer> getStudentNumberByTeacherId(Integer teacherId);

}
