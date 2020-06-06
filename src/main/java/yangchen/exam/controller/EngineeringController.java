package yangchen.exam.controller;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yangchen.exam.Enum.UserTypeEnum;
import yangchen.exam.entity.Administrator;
import yangchen.exam.entity.Teacher;
import yangchen.exam.model.JsonResult;
import yangchen.exam.repo.EngineeringRepo;
import yangchen.exam.repo.ProjectInfoRepo;
import yangchen.exam.repo.ScoreRepo;
import yangchen.exam.service.adminManagement.AdminManagement;
import yangchen.exam.service.excelservice.EngineeringService;
import yangchen.exam.service.excelservice.EngineeringServiceImpl;
import yangchen.exam.service.teacher.TeacherService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/engineering", produces = MediaType.APPLICATION_JSON_VALUE)
public class EngineeringController {

    @Autowired
    private EngineeringServiceImpl engineeringServiceimpl;

    @Autowired
    private EngineeringService engineeringService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AdminManagement adminManagement;

    @Autowired
    private EngineeringRepo engineeringRepo;

    @Autowired
    private ProjectInfoRepo projectInfoRepo;

    @Autowired
    private ScoreRepo scoreRepo;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public JsonResult uploadStudent(@RequestParam MultipartFile file, @RequestParam String teacherName) throws IOException {
        InputStream inputStream = file.getInputStream();
        return engineeringServiceimpl.huExcel(teacherName, inputStream);
    }

    @GetMapping(value = "/thirdData")
    public List<Object> getDachengdu() {
        List<Integer> years;
        years = this.engineeringRepo.getYears();
        List<Double> homeworkscore = new ArrayList<>();
        List<Double> examscore = new ArrayList<>();
        List<Double> excelscore = new ArrayList<>();
        List<Object> info = new ArrayList<>();
//        List<Double> score = new ArrayList<>();
        for (int i = 0; i<years.size(); i++){
//            homeworkscore.add(this.projectInfoRepo.getHomeworkScore(years.get(i)));
            List<Object> message = new ArrayList<>();
            Integer hs = this.projectInfoRepo.getHomeworkScore(years.get(i));
            Integer es = this.scoreRepo.getExamScore(years.get(i));
            Integer xs = this.engineeringRepo.getExcelScore(years.get(i));
            Double hscore = 0.1*hs;
            Double escore = 0.3*es;
            Double xscore = 0.3*xs;
            System.out.println(hscore);
            System.out.println(escore);
            System.out.println(xscore);
            homeworkscore.add(hscore);
            examscore.add(escore);
            excelscore.add(xscore);
            message.add(i+1);
            message.add(years.get(i));
            message.add(homeworkscore.get(i));
            message.add(examscore.get(i));
            message.add(excelscore.get(i));
//            message.add(homeworkscore.get(i));
            info.add(message);
        }
        System.out.println(info);
        return info;
    }
}

