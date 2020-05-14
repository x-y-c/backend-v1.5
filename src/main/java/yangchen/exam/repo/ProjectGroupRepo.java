package yangchen.exam.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import yangchen.exam.entity.ProjectGroup;

import java.sql.Timestamp;
import java.util.List;

public interface ProjectGroupRepo extends JpaRepository<ProjectGroup,Integer> {


    Page<ProjectGroup> findByTeacherId(Pageable pageable, Integer teacherId);

    @Transactional
    @Modifying
    void deleteProjectGroupById(Integer id);

    @Query(value = "select * from project_group where id in ?1 order by id desc", nativeQuery = true)
    List<ProjectGroup> getProjectGroupById(List<Integer> projectGroupId);

    @Modifying
    @Query(value = "update project_group set project_name=?1,project_ttl=?2,start_time=?3 ,end_time=?4 where id=?5", nativeQuery = true)
    void updateExamGroup(String examDesc, Integer examTime, Timestamp beginTime, Timestamp endTime, Integer id);


}
