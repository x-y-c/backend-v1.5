package yangchen.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yangchen.exam.entity.Second_indicator;
import yangchen.exam.repo.Second_indicatorRepo;

import java.util.List;

@RestController
@RequestMapping("/second_indicator")
public class Second_indicatorController {
    @Autowired
    private Second_indicatorRepo second_indicatorRepo;
    @GetMapping("/findAll")
    public List<Second_indicator>findAll() {
        return second_indicatorRepo.findAll();
    }

    @PostMapping("/save")
    public String save(@RequestBody Second_indicator second_indicator){
        Second_indicator result = second_indicatorRepo.save(second_indicator);
        if(result != null){
            return "success";
        }else {
            return "error";
        }
    }

    @GetMapping("/findById/{firstid}")
    public Second_indicator findByfirstId(@PathVariable("firstid") Float firstid){
        Second_indicator info = this.second_indicatorRepo.findByfirstId(firstid);
        System.out.println(info);
        return info;
    }
//    public Float findByfirstId(@PathVariable("firstid") Float firstid){
//        return second_indicatorRepo.findByfirstId(firstid).get();
//    }

    @PutMapping("/update")
    public String update(@RequestBody Second_indicator second_indicator){
        Second_indicator result = second_indicatorRepo.save(second_indicator);
        if(result != null){
            return "success";
        }else {
            return "error";
        }
    }

    @DeleteMapping("/deleteById/{firstid}")
    public void deleteByfirstId(@PathVariable("firstid") Float firstid){
        System.out.println("111");
        //Second_indicator result = this.second_indicatorRepo.deleteByfirstId(firstid);
        second_indicatorRepo.deleteByfirstId(firstid);
    }

}
