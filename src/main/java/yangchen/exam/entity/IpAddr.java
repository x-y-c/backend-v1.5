package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ip_addr")
public class IpAddr {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "ip_addr")
    private String ipAddr;

    @Column(name = "browser")
    private String browser;

    @Column(name = "login_time")
    private Timestamp loginTime;

    @PrePersist
    public void init() {
        this.loginTime = new Timestamp(System.currentTimeMillis());
    }

}
