package com.project.ip.controller;

import com.project.ip.constants.Constants;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController
{
    @RequestMapping(Constants.EndPoints.ERROR)
    public String getError()
    {
        return Constants.EndPoints.ERROR;
    }

    @PostMapping
    public String postError()
    {
        return Constants.EndPoints.ERROR;
    }
}