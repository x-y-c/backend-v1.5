package yangchen.exam.controller;


import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yangchen.exam.model.ExaminationDetail;
import yangchen.exam.model.JsonResult;

import java.util.List;

@Api(value = "AndroidController")
@RestController
@RequestMapping(value = "/android", produces = MediaType.APPLICATION_JSON_VALUE)
public class AndroidController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public JsonResult test(@RequestParam String httptest) {
          return JsonResult.succResult("connect success!");
    }

}
