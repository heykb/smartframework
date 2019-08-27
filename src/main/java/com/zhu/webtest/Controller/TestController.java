package com.zhu.webtest.Controller;

import com.zhu.annotation.Action;
import com.zhu.annotation.Controller;
import com.zhu.annotation.Inject;

import com.zhu.common.*;

import com.zhu.webtest.Service.TestService;

@Controller
public class TestController {

    @Inject
    private TestService testService;

    @Action(value = "/hello",method = RequestMethod.GET)
    public Data hello(Param param){
        testService.test();
        return new Data("hello world");
    }
    @Action(value = "/",method = RequestMethod.GET)
    public Data helo(Param param){
        testService.test();
        return new Data("你好");
    }

}
