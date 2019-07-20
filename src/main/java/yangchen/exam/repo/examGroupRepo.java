package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.ExamGroupNew;

import java.sql.Timestamp;
import java.util.List;


/**
 * @author YC
 * @date 2019/5/6 16:09
 * O(∩_∩)O)
 */
public interface examGroupRepo extends JpaRepository<ExamGroupNew, Integer> {

    /**
     * 查找已经结束的考试
     *
     * @param timestamp
     * @return
     */
    List<ExamGroupNew> findByEndTimeBefore(Timestamp timestamp);

    /**
     * 查询未开始的考试
     *
     * @param timestamp
     * @return
     */
    List<ExamGroupNew> findByBeginTimeAfter(Timestamp timestamp);
}
