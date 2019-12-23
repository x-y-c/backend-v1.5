package yangchen.exam.service.score;

import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import yangchen.exam.entity.ExamGroupNew;
import yangchen.exam.entity.ExamInfo;
import yangchen.exam.entity.ExamPaper;
import yangchen.exam.repo.ExamGroupRepo;
import yangchen.exam.repo.ExamInfoRepo;
import yangchen.exam.repo.ExamPaperRepo;
import yangchen.exam.service.examination.ExamGroupService;
import yangchen.exam.service.examination.ExaminationService;
import yangchen.exam.service.examination.impl.ExaminationServiceImpl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class QuartzService {
    /* *
     * corn = "* * * * * * ?"
     * 分别对应 秒 分 时 日 月 年
     * */
    @Scheduled(cron = "0 0 0 * * ?")
    public void timerToNow(){
        prepare();
        System.out.println("now time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    private static Logger LOGGER = LoggerFactory.getLogger(QuartzService.class);

    @Autowired
    private ExamPaperRepo examPaperRepo;

    @Autowired
    private ExaminationServiceImpl examinationServiceImpl;

    @Autowired
    private ExamGroupRepo examGroupRepo;

    @Autowired
    private ExamInfoRepo examInfoRepo;


    public static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    public static Date getnowEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }


    // 计算所有学生的成绩
    // 流程： 获得当前时间 -> 获得在当前时间之后的 examGroupId -> examInfo -> examinationId,studentId -> 调用computeScore方法更新成绩
    public void prepare(){
        List<ExamGroupNew> examGroupList = examGroupRepo.findAll();
        List<ExamGroupNew> examGroupNews = new ArrayList<>();
        long todayBeginTime = getStartTime().getTime();
        long todayEndTime = getnowEndTime().getTime();

        for(ExamGroupNew examGroup:examGroupList){
            long examEndTime = examGroup.getEndTime().getTime();
            if( todayBeginTime < examEndTime && todayEndTime > examEndTime){
                examGroupNews.add(examGroup);
            }
        }

        if(examGroupNews.size()>0){
            for(ExamGroupNew examGroup:examGroupNews){
                List<ExamInfo> examInfoList = examInfoRepo.findByExamGroupId(examGroup.getId());
                for(ExamInfo examInfo:examInfoList){
                    Date endDate = new Date(examGroup.getEndTime().getTime());
                    ExamPaper examPaper = examPaperRepo.findById(examInfo.getExaminationId()).get();
                    Integer studentId = examInfo.getStudentNumber();
                    System.out.println(endDate);
                    System.out.println(examPaper);
                    System.out.println(studentId);
                    submitTest(endDate,examPaper,studentId);
                }
            }
        }else{
            LOGGER.info("当天[{}]没有需要统一交卷的考试",getStartTime());
        }
    }

    //在指定的日期运行一次定时任务
    /*如果executeDate日期在今天之前，则启动定时器后，立即运行一次定时任务run方法*/
    /*如果executeDate日期在今天之后，则启动定时器后，会在指定的将来日期运行一次任务run方法*/
    public void submitTest(Date executeDate,ExamPaper examPaper,Integer studentId) {

//    public void submitTest(Date excuteTime) throws ParseException {
//        String sdate = "2019-12-15 18:00:00";
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = sf.parse(sdate);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                System.out.println("开始计算成绩...");
                Integer finalScore = examinationServiceImpl.computeScore(examPaper,studentId);
                System.out.println(examPaper.getId()+" "+studentId+" "+finalScore);
                LOGGER.info("当前时间=[{}],examEndTime=[{}],examPaper.getId()= [{}],studentId= [{}],finalScore= [{}]",
                        new Date(),executeDate,examPaper.getId(),studentId,finalScore);
                timer.cancel();
            }
        }, executeDate);
    }
}
