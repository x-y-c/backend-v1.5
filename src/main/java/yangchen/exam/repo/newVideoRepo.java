package yangchen.exam.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import yangchen.exam.entity.Videourl;

public interface newVideoRepo extends JpaRepository<Videourl,Integer> {
    @Transactional
    @Modifying
    @Query(value = "insert into videourl(url) values(?1)",nativeQuery = true)
    void insertUrl( String url);
}
