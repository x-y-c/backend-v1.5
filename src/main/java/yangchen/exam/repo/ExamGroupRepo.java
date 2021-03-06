package yangchen.exam.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import yangchen.exam.entity.ExamGroupNew;

import java.sql.Timestamp;
import java.util.List;


/**
 * @author YC
 * @date 2019/5/6 16:09
 * O(∩_∩)O)
 */
public interface ExamGroupRepo extends JpaRepository<ExamGroupNew, Integer> {

    /**
     * 查找已经结束的考试
     *
     * @param timestamp
     * @return
     */
    List<ExamGroupNew> findByEndTimeBefore(Timestamp timestamp);
    List<ExamGroupNew> findByEndTimeBeforeAndExamTeacher(Timestamp timestamp,String teacherName);

    /**
     * 查询未开始的考试
     *
     * @param timestamp
     * @return
     */
    List<ExamGroupNew> findByBeginTimeAfter(Timestamp timestamp);
    List<ExamGroupNew> findByBeginTimeAfterAndExamTeacher(Timestamp timestamp,String teacherName);


    @Query(value = "select * from  exam_group_new order by id desc ", nativeQuery = true)
    List<ExamGroupNew> getAllExamGroupDesc();

    @Query(value = "select * from  exam_group_new where exam_teacher = ?1 order by id desc ", nativeQuery = true)
    List<ExamGroupNew> getAllExamGroupAndExamTeacherDesc(String teacherName);

    List<ExamGroupNew> findByExamTeacher(String teacherName);

    Page<ExamGroupNew> findByExamTeacher(Pageable pageable,String teacher);

    @Transactional
    @Modifying
    void deleteExamGroupNewById(Integer id);

    @Modifying
    @Query(value = "update exam_group_new set exam_description=?1,exam_time=?2,begin_time=?3 ,end_time=?4 where id=?5", nativeQuery = true)
    void updateExamGroup(String examDesc, Integer examTime, Timestamp beginTime, Timestamp endTime, Integer id);
}
