//package yangchen.exam.util;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import yangchen.exam.entity.QuestionNew;
//import yangchen.exam.repo.QuestionRepo;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
//public class HtmlUtilTest {
//
//
//    @Autowired
//    private QuestionRepo questionRepo;
//
//
//
//    @Test
//    public void test() {
//        QuestionNew hao034 = questionRepo.findByQuestionBh("02359c590cf243d49db06f4b6149a608");
//        System.out.println(hao034.getQuestionDetails());
//    }
//
//    @Test
//    public void test2(){
//        String code = "<p><span style=\"font-family:宋体\"><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">输入三角形的三边长</span></span></span><span style=\"font-size:14px;font-family:宋体,simsun\">a</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">、</span></span><span style=\"font-size:14px;font-family:宋体,simsun\">b</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">、</span></span><span style=\"font-size:14px;font-family:宋体,simsun\">c(</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">边长可以是小数，双精度浮点数</span></span><span style=\"font-size:14px;font-family:宋体,simsun\">)</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">，求三角形面积</span></span><span style=\"font-size:14px;font-family:宋体,simsun\">area</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">，并输出，保留两位小数。如果输入的三边构不成三角形，应给出</span></span><span style=\"font-size:14px;font-family:宋体,simsun\">“data&nbsp;error”</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">的信息提示。注：根据</span></span><span style=\"font-size:14px;font-family:宋体,simsun\">“</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">海伦－秦九韶</span></span><span style=\"font-size:14px;font-family:宋体,simsun\">”</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">公式，</span></span><span style=\"font-size:14px;font-family:宋体,simsun\">area</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">＝sqrt(</span></span><span style=\"font-size:14px;font-family:宋体,simsun\">p(p-a)(p-b)(p-c))</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">，其中</span></span><span style=\"font-size:14px;font-family:宋体,simsun\">p</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">＝</span></span><span style=\"font-size:14px;font-family:宋体,simsun\">(a+b+c)/2，sqrt为求根号的函数</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">。编程可用素材：</span></span><span style=\"font-size:14px;font-family:宋体,simsun\">printf(&quot;\\nplease&nbsp;input&nbsp;triange&nbsp;sides:&quot;)...</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">、</span></span><span style=\"font-size:14px;font-family:宋体,simsun\">printf(&quot;Output:\\ndata&nbsp;error\\n&quot;)...</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">、</span></span><span style=\"font-size:14px\"><span style=\"font-size:14px;font-family:宋体,simsun\">printf(&quot;</span><span style=\"font-size:16px;font-family:宋体,simsun\">Output:</span><span style=\"font-size:14px;font-family:宋体,simsun\">\\narea=...\\n&quot;...</span></span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">。</span></span><br /><span style=\"font-family:宋体\"><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">　　程序的运行效果应类似地如图</span></span></span><span style=\"font-size:14px;font-family:宋体,simsun\">1</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">和图</span></span><span style=\"font-size:14px;font-family:宋体,simsun\">2</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">所示，图</span></span><span style=\"font-size:14px;font-family:宋体,simsun\">1</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">中的</span></span><span style=\"font-size:14px;font-family:宋体,simsun\">3,4,5</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">和图</span></span><span style=\"font-size:14px;font-family:宋体,simsun\">2</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">中的</span></span><span style=\"font-size:14px;font-family:宋体,simsun\">3,4,8</span><span style=\"font-size:14px\"><span style=\"font-family:宋体,simsun\">是从键盘输入的内容</span></span></p><p><span style=\"font-size:14px\"><span style=\"font-family:楷体_gb2312\"><img style=\"height:247px;width:389px\" src=\"/ckupload/20130429030523_047.JPG\" /></span></span></p><p><span style=\"font-size:14px\"><span style=\"font-family:楷体_gb2312\"><img style=\"height:247px;width:389px\" src=\"/ckupload/20130429030531_048.JPG\" /></span></span></p>";
//        System.out.println(UrlImageUtil.setImagesDomain("http://119.3.217.233:2048",code));
//    }
//
//}