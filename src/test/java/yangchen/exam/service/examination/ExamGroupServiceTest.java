//package yangchen.exam.service.examination;
//
//import org.apache.commons.text.StringEscapeUtils;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//@SpringBootTest
//@RunWith(SpringJUnit4ClassRunner.class)
//public class ExamGroupServiceTest {
//    @Autowired
//    ExamGroupService examGroupService;
//
//    @Test
//    public void test(){
//        examGroupService.deleteExamInfo(19);
//    }
//
//
//    @Test
//    public void test2(){
//
//      System.out.println(  StringEscapeUtils.unescapeJson("{ \"key\": [{\"filename\":\"main.c\",\"code\":\"#include <stdio.h>\\n#include <string.h>\\n\\nint n, val[110], dp[50000];\\n\\nint main() {\\n        int i, j, sum;\\n\\t\\tprintf(\\\"Please input n: \\\");\\n\\t\\tscanf(\\\"%d\\\", &n);\\n\\t\\tprintf(\\\"Output:\\\\n\\\");\\n        while (n)\\n\\t\\t{\\n                sum = 0;\\n\\t\\t\\t\\tprintf(\\\"Input Date:\\\\n\\\");\\n                for (i = 0; i < n; i++)\\n\\t\\t\\t\\t{\\n                        scanf(\\\"%d\\\", &val[i]);\\n                        sum += val[i];\\n                }\\n                memset(dp, 0, sizeof(dp));\\n                dp[0] = 1;\\n                for (i = 0; i < n; i++)\\n                        for (j = sum / 2; j >= val[i]; j--)\\n                                if (dp[j - val[i]] != 0)\\n                                        dp[j] = 1;\\n\\t\\t\\t\\tprintf(\\\"The best ans is: \\\");\\n                for (i = sum / 2; i >= 0; i--)\\n                        if (dp[i] != 0)\\n\\t\\t\\t\\t\\t\\t{\\n                                printf(\\\"%d %d\\\\n\\\", i, sum - i);\\n                                break;\\n                        }\\n\\t\\tprintf(\\\"Continue Input n: \\\");\\n\\t\\tscanf(\\\"%d\\\", &n);\\n        }\\n        return 0;\\n}\"}]}").toString());
//
//    }
//
//
//
//
//}