package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "student")
public class student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "name")
    private String name;

    @Column(name = "major")
    private String major;

    @Column(name = "grade")
    private String grade;

    @Column(name = "created_time")
    private Timestamp createdTime;

    @Column(name = "enabled")
    private Boolean enabled;

    @PrePersist
    private void init() {
        createdTime = new Timestamp(System.currentTimeMillis());
    }

}
