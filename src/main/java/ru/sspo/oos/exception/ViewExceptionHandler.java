package ru.sspo.oos.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class ViewExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleNotFound(ResourceNotFoundException ex) {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("message", ex.getMessage());
        mv.addObject("status", 404);
        mv.addObject("error", "Не найдено");
        return mv;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneric(Exception ex) {
        log.error("View error", ex);
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("message", "К сожалению, произошла ошибка");
        mv.addObject("status", 500);
        mv.addObject("error", ex.getMessage());
        return mv;
    }
}

