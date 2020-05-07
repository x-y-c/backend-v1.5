package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import yangchen.exam.entity.Second_indicator;

import java.util.List;

public interface Second_indicatorRepo extends JpaRepository<Second_indicator,Float> {

    //@Query(value = "select * from second_indicator where second_indicator.firstid =1.1", nativeQuery = true)
    @Query(value = "select * from second_indicator where ABS(firstid-?)< 1e-5;",nativeQuery = true)
    Second_indicator findByfirstId(Float firstid);

    @Transactional
    @Modifying
    @Query("delete from Second_indicator where ABS(firstid-?1)< 1e-5")
    void deleteByfirstId(@Param("firstid") Float firstid);
    //Second_indicator deleteByfirstId(@Param("firstid") Float firstid);
}
