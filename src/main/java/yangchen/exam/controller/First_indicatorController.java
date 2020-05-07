package yangchen.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yangchen.exam.entity.First_indicator;
import yangchen.exam.repo.First_indicatorRepo;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/first_indicator")
public class First_indicatorController {
    @Autowired
    private First_indicatorRepo first_indicatorRepo;

    @GetMapping("/findAll")
    public List<First_indicator> findAll(){
        return first_indicatorRepo.findAll();
    }

    @PostMapping("/save")
    public String save(@RequestBody First_indicator first_indicator){
        First_indicator result = first_indicatorRepo.save(first_indicator);
        if(result != null){
            return "success";
        }else {
            return "error";
        }
    }
    @GetMapping("/findById/{firstid}")
    public  First_indicator findById(@PathVariable("firstid") Integer firstid){
        return first_indicatorRepo.findById(firstid).get();
    }

    @PutMapping("/update")
    public String update(@RequestBody First_indicator first_indicator){
        First_indicator result = first_indicatorRepo.save(first_indicator);
        if(result != null){
            return "success";
        }else {
            return "error";
        }
    }

    @DeleteMapping("/deleteById/{firstid}")
    public void deleteById(@PathVariable("firstid") Integer firstid){
        first_indicatorRepo.deleteById(firstid);
    }

}
