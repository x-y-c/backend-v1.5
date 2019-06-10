package yangchen.exam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    /**
     * 自定义标题
     */
    @Column(name = "questionTitle")
    private String questionTitle;

    /**
     * 题目
     */
    @Column(name = "question_name")
    private String questionName;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 类型
     */
    @Column(name = "category")
    private String category;


    /**
     * 知识点
     */
    @Column(name = "knowledge")
    private String knowledge;

    /**
     * 难度
     */
    @Column(name = "difficulty")
    private String difficulty;

    /**
     * 代码
     */
    @Column(name = "code")
    private String code;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
}


