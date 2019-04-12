package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yangchen.exam.model.Category;

import javax.persistence.*;

/**
 * @author YC
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "question")
public class Question {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "question_name")
    private String questionName;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private Category category;

    @Column(name = "code")
    private String code;
}


