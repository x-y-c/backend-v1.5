package yangchen.exam.util;

import java.util.HashSet;
import java.util.Set;

/**
 * @author YC
 * @date 2019/5/5 17:08
 * O(∩_∩)O)
 */
public class RandomUtil {

    public static int getRandom(int min, int max) {
        return (int) (min + Math.random() * (max - min + 1));
    }


    public static Set getRandom(int min, int max, int count) {
        Set<Integer> result = new HashSet<>();
        while (result.size() < count) {
            int randomNumber = (int) (min + Math.random() * (max - min + 1));
            result.add(randomNumber);
        }
        return result;
    }
}
