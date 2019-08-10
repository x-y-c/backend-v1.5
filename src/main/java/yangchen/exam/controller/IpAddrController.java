package yangchen.exam.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yangchen.exam.repo.IpAddrRepo;

@RestController
@RequestMapping(value = "/ip",produces = MediaType.APPLICATION_JSON_VALUE)
public class IpAddrController {

    @Autowired
    private IpAddrRepo ipAddrRepo;



}
