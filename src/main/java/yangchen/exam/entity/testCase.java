package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "test_case")
@Data
public class testCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "qid")
    private Integer qid;

    @Column(name = "input")
    private String input;
    
    @Column(name = "output")
    private String output;

    @Column(name = "score")
    private Integer score;
}
