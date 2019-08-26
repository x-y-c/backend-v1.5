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

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "ip_addr")
    private String ipAddress;

    @Column(name = "browser")
    private String browser;

    @Column(name = "login_time")
    private Timestamp loginTime;

    @Column(name = "exam_group_id")
    private Integer examGroupId;

    @Column(name = "exam_group_desc")
    private String examGroupDesc;




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IpAddr ipAddr = (IpAddr) o;
        return ipAddress != null ? ipAddress.equals(ipAddr.ipAddress) : ipAddr.ipAddress == null;
    }


    @Override
    public int hashCode() {
        return ipAddress != null ? ipAddress.hashCode() : 0;
    }

    @PrePersist
    public void init() {
        this.loginTime = new Timestamp(System.currentTimeMillis());
    }

}
