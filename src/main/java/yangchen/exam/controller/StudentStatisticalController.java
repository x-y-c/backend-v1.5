package yangchen.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yangchen.exam.repo.StudentStatisticalRepo;
import yangchen.exam.service.submit.StudentStatisticalService;

import java.util.List;

@RestController
@RequestMapping("/studentStatistical")
public class StudentStatisticalController {
    @Autowired
    private StudentStatisticalRepo studentStatisticalRepo;

    @Autowired
    private StudentStatisticalService studentStatisticalService;

    @RequestMapping("/countstage/{userId}")
    public List<Object> countstage(@PathVariable("userId")Integer userId) {
        System.out.println(userId);
        List<Object> info = this.studentStatisticalService.makeChart(userId);
        //System.out.println(info);
        return info;
    }
}
