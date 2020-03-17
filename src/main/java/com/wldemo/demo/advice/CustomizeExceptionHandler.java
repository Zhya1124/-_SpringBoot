package com.wldemo.demo.advice;

import com.wldemo.demo.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e, Model model) {
        if(e instanceof CustomizeException){
            model.addAttribute("message", e.getMessage());
        }else{//无法识别的异常
            model.addAttribute("message","服务要冒烟了，要不然你等会试试！！！");
        }

        return new ModelAndView("error");
    }

}
