package it.alex89.usermgr.controller;

import it.alex89.usermgr.excp.AlreadyCreatedException;
import it.alex89.usermgr.excp.NotFoundException;
import it.alex89.usermgr.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        return new ErrorResponse("NOT_FOUND", e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyCreatedException.class)
    public ErrorResponse handleAlreadyCreatedException(AlreadyCreatedException e) {
        return new ErrorResponse("CONFLICT", e.getMessage());
    }

}
