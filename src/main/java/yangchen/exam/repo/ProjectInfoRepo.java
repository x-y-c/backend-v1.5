package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import yangchen.exam.entity.ProjectInfo;

import java.util.List;

public interface ProjectInfoRepo extends JpaRepository<ProjectInfo,Integer> {

    @Transactional
    @Modifying
    void deleteProjectInfoByProjectGroupId(Integer homeworkId);

    @Query(value = "select project_paper_id from project_info where project_group_id=?",nativeQuery = true)
    List<Integer> searchProjectPaper(Integer homeworkId);

    @Query(value = "select project_paper_id from project_info where project_group_id=?1 order by id asc limit 0,1", nativeQuery = true)
    Integer getProjectPaperIdByProjectGroupId(Integer homeworkInfo);

    List<ProjectInfo> findByProjectGroupId(Integer homeworkGroupId);

    List<ProjectInfo> findByStudentIdOrderByIdDesc(Integer studentId);

    ProjectInfo findByProjectGroupIdAndStudentId(Integer projectGroupId,Integer studentId);

}
