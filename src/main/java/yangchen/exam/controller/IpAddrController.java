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

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/ip",produces = MediaType.APPLICATION_JSON_VALUE)
public class IpAddrController {

    @Autowired
    private IpAddrService ipAddrService;

    @RequestMapping(value = "/address", method = RequestMethod.GET)
    public JsonResult getExamGroup(Integer examGroupId) {
        HashMap<Integer, List<String>> result = new HashMap<>();
        result = ipAddrService.searchIp(examGroupId);


        return JsonResult.succResult(null);
    }

}
