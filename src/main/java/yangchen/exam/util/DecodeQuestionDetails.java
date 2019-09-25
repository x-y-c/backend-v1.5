package yangchen.exam.util;

import java.util.List;


public class DecodeQuestionDetails {

    //现将questionDetails的html文本中的image标签取出来
    //
    public static String getRightImage(String domainStr,String questionDetails) {

        questionDetails  = UrlImageUtil.setImagesDomain(domainStr, questionDetails);

        return questionDetails;
    }
}
