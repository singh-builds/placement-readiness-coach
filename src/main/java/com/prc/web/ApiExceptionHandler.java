package com.prc.web;
import java.time.Instant;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
@RestControllerAdvice public class ApiExceptionHandler {
 @ExceptionHandler(NoSuchElementException.class) ResponseEntity<Map<String,Object>> notFound(NoSuchElementException e){return error(HttpStatus.NOT_FOUND,"NOT_FOUND","Requested resource does not exist");}
 @ExceptionHandler({IllegalArgumentException.class,MethodArgumentNotValidException.class}) ResponseEntity<Map<String,Object>> invalid(Exception e){return error(HttpStatus.BAD_REQUEST,"VALIDATION_ERROR","Request is invalid");}
 private ResponseEntity<Map<String,Object>> error(HttpStatus s,String code,String message){return ResponseEntity.status(s).body(Map.of("timestamp",Instant.now().toString(),"code",code,"message",message));}
}
