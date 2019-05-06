package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "examation")
@Data
public class Examination {
//试卷表
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "title_id")
    private String titleId;

    @Column(name = "title_type")
    private String titleType;

    @Column(name = "used")
    private Boolean used;



}
