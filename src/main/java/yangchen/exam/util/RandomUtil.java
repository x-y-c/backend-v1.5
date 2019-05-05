package yangchen.exam.util;

/**
 * @author YC
 * @date 2019/5/5 17:08
 * O(∩_∩)O)
 */
public class RandomUtil {

    public static int getRandom(int min,int max){
        return (int)(min+Math.random()*(max-min+1));
    }
}
