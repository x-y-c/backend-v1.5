package yangchen.exam.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "videourl")
@Data
public class Videourl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String url;
}
