package com.rightpair.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public ModelAndView handleNoResourceFoundException(NoResourceFoundException exception) {
        return new ModelAndView("/errors/404", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleExceptions(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ModelAndView("/errors/500", HttpStatus.INTERNAL_SERVER_ERROR)
                .addObject("message", exception.getMessage());
    }
}
