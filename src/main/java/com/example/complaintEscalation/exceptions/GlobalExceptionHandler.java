package com.example.complaintEscalation.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleNotFound(ResourceNotFoundException ex){
        Map<String,String> body=new HashMap<>();
        body.put("error",ex.getMessage());
        return new ResponseEntity<>(body,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Map<String,String>> handleInvalid(InvalidRequestException ex){
        Map<String,String> body=new HashMap<>();
        body.put("error",ex.getMessage());
        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleValidation(MethodArgumentNotValidException ex){
        Map<String,String> body=new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error->
            body.put(error.getField(),error.getDefaultMessage())
        );
        return new ResponseEntity<>(body,HttpStatus.BAD_REQUEST);
    }
}
