package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import yangchen.exam.entity.Examination;

import java.util.List;

public interface examinationRepo extends JpaRepository<Examination, Integer> {
    //通过titleType（试卷阶段查找试卷），返回可能有多个，放在list中；
    List<Examination> findByTitleTypeIsIn(String titleType);

    //安装分配状态进行查询；
    List<Examination> findByUsed(Boolean used);
}
