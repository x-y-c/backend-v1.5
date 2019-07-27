package yangchen.exam.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author YC
 * @date 2019/6/7 15:54
 * O(∩_∩)O)
 */
@Data
@Getter
@Setter
public class TwoTuple<A, B> {
    public  A first;
    public  B second;

    public TwoTuple(A a, B b) {
        this.first = a;
        this.second = b;
    }


}
