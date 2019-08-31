package yangchen.exam.controller;


import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import yangchen.exam.entity.ExamGroupNew;
import yangchen.exam.entity.IpAddr;
import yangchen.exam.model.JsonResult;
import yangchen.exam.repo.ExamGroupRepo;
import yangchen.exam.repo.IpAddrRepo;
import yangchen.exam.service.ipAddr.IpAddrService;

import java.sql.Timestamp;
import java.util.*;

@Api(value = "IpAddrController")
@RestController
@RequestMapping(value = "/ip", produces = MediaType.APPLICATION_JSON_VALUE)
public class IpAddrController {

    @Autowired
    private IpAddrService ipAddrService;

    @Autowired
    private IpAddrRepo ipAddrRepo;


    @Autowired
    private ExamGroupRepo examGroupRepo;

    @RequestMapping(value = "/address", method = RequestMethod.GET)
    public JsonResult getExamGroup(Integer examGroupId) {
        List<IpAddr> ipAddrList = new ArrayList<>();
        HashMap<Integer, Set<String>> result = new HashMap<>();
        result = ipAddrService.searchIp(examGroupId);
        ExamGroupNew examGroupNew = examGroupRepo.findById(examGroupId).get();
        Timestamp endTime = examGroupNew.getEndTime();
        //这里的result是获取到的全部的有问题的学生id 和其ip记录  key-value
        //下一步迭代这个result 把他所有的信息都取出来查询
        Iterator maplist = result.entrySet().iterator();
        while (maplist.hasNext()) {
            Map.Entry<Integer, Set<String>> entry = (Map.Entry<Integer, Set<String>>) maplist.next();
            Integer studentNumber = entry.getKey();
            ipAddrList.addAll(ipAddrRepo.
                    findByExamGroupIdAndStudentIdAndLoginTimeBefore(examGroupId, studentNumber, endTime));
        }
        return JsonResult.succResult(ipAddrList);
    }


    @RequestMapping(value = "/addressY", method = RequestMethod.GET)
    public JsonResult getExamGroupY(Integer examGroupId) {
        return JsonResult.succResult(ipAddrService.searchIps(examGroupId));

    }
}
