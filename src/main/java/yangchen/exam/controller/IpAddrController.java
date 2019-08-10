package yangchen.exam.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import yangchen.exam.entity.IpAddr;
import yangchen.exam.model.JsonResult;
import yangchen.exam.repo.IpAddrRepo;
import yangchen.exam.service.ipAddr.IpAddrService;

import java.util.*;

@RestController
@RequestMapping(value = "/ip",produces = MediaType.APPLICATION_JSON_VALUE)
public class IpAddrController {

    @Autowired
    private IpAddrService ipAddrService;

    @Autowired
    private IpAddrRepo ipAddrRepo;

    @RequestMapping(value = "/address", method = RequestMethod.GET)
    public JsonResult getExamGroup(Integer examGroupId) {
//        List<IpAddr> ipAddrList = new ArrayList<>();
//        HashMap<Integer, Set<String>> result = new HashMap<>();
//        result = ipAddrService.searchIp(examGroupId);
//        //这里的result是获取到的全部的有问题的学生id 和其ip记录  key-value
//        //下一步迭代这个result 把他所有的信息都取出来查询
//        Iterator maplist = result.entrySet().iterator();
//        while (maplist.hasNext()) {
//            Map.Entry<Integer, Set<String>> entry = (Map.Entry<Integer, Set<String>>) maplist.next();
//            Integer studentNumber = entry.getKey();
//            ipAddrList = ipAddrRepo.findByExamGroupIdAndStudentId(examGroupId,studentNumber);
//        }
//        return JsonResult.succResult(ipAddrList);
//    }
        return JsonResult.succResult(ipAddrService.searchIps(examGroupId));

    }
}
