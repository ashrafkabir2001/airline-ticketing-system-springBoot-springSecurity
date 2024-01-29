package com.ats.advice;


import com.ats.exception.BadRequestException;
import com.ats.exception.ObjectNotFoundException;
import com.ats.exception.UserAlreadyExistsException;
import com.ats.message.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionAdvice {
    private final ErrorMessage errorMessage;

    @Autowired
    public ExceptionAdvice(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> handleBadClientRequest(BadRequestException exception){
        errorMessage.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<ErrorMessage> handleHibernateException(RuntimeException exp){
//        errorMessage.setMessage(exp.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorMessage);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleException(MethodArgumentNotValidException exception){
        Map<String,String> errorMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->{
            errorMap.put(error.getField(),error.getDefaultMessage());
        });
        return errorMap;
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorMessage> handleObjectNotFoundException(RuntimeException exception){
        errorMessage.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleUserAlreadyExistsException(RuntimeException exception){
        errorMessage.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessage> exceptionHandler(BadRequestException exception) {
        errorMessage.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }
}