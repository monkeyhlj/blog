package com.monkey.handler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;

@Slf4j
@ControllerAdvice //拦截标有controller注解的控制器
public class ControllerExceptionHandler {


//    private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(Exception.class) //拦截的只要是exception级别的信息
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e){ //获取异常的url与exception信息
//        logger.error("Request URL : {},Exception : {}",request.getRequestURL(),e);
        log.error("Request URL : {},Exception : {}",request.getRequestURL(),e);

        if(AnnotationUtils.findAnnotation(e.getClass(),ResponseStatus.class) != null){
            try {
                throw e;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception",e);
        mv.setViewName("error/error");//跳转到错误页面
        return mv;
    }

}
