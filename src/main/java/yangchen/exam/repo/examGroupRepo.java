package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.ExamGroup;

import java.sql.Timestamp;
import java.util.List;


/**
 * @author YC
 * @date 2019/5/6 16:09
 * O(∩_∩)O)
 */
public interface examGroupRepo extends JpaRepository<ExamGroup, Integer> {

    /**
     * 查找已经结束的考试
     *
     * @param timestamp
     * @return
     */
    List<ExamGroup> findByEndTimeBefore(Timestamp timestamp);

    /**
     * 查询未开始的考试
     *
     * @param timestamp
     * @return
     */
    List<ExamGroup> findByBeginTimeAfter(Timestamp timestamp);
}
