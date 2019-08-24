package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yangchen.exam.entity.TeachClassInfo;

import java.util.List;

public interface TeachClassInfoRepo extends JpaRepository<TeachClassInfo, Integer> {


    @Query(value = "select class_name from teach_class_info where teacher_id =?1", nativeQuery = true)
    List<String> getClassNameByTeacherId(Integer teacherId);


    List<TeachClassInfo> findByTeacherId(Integer teacherId);

    void deleteByTeacherId(Integer teacherId);


    @Query(value = "select distinct teacher_id from teach_class_info ", nativeQuery = true)
    List<Integer> getTeacherId();


}
