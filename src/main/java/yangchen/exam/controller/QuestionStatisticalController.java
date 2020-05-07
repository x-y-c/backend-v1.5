package yangchen.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
//import yangchen.exam.entity.questionStatistical;
import yangchen.exam.repo.QuestionStatisticalRepo;
import yangchen.exam.service.question.QuestionCustomService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/questionstatistical")
public class QuestionStatisticalController {
    @Autowired
    private QuestionStatisticalRepo questionStatisticalRepo;

    @Autowired
    private QuestionCustomService questionCustomService;
//    @GetMapping("/findAll")
//    public List<QuestionNew> findAll() {
//        return questionStatisticalRepo.findAll();
//    }

    @RequestMapping("/countnum/{value}")
    public List<Object> countNum(@PathVariable("value")String value) {
        //System.out.println(value);
        List<Object> info = this.questionCustomService.makeChart(value);
        //System.out.println(info);
        return info;
    }

    @RequestMapping("/showsearch/{value}")
    public List<Object> showSearch(@PathVariable("value")String value) {
        System.out.println(value);
        List<Object> info = this.questionCustomService.getTable(value);
        return info;
    }

}
