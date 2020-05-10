package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="project_info")
public class ProjectInfo {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "student_id")
    private Integer studentId;

    @Column(name = "project_group_id")
    private Integer projectGroupId;

    @Column(name = "project_paper_id")
    private Integer projectPaperId;

    @Column(name = "score")
    private Integer score;

    @Column(name = "finished")
    private Boolean finished;

}
