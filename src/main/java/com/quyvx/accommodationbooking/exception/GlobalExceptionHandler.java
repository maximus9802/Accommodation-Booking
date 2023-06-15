package com.quyvx.accommodationbooking.exception;

import com.quyvx.accommodationbooking.dto.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@ControllerAdvice
public class GlobalExceptionHandler {

//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//        Map<String, Object> body = new LinkedHashMap<>();
//        body.put("timestamp", System.currentTimeMillis());
//        body.put("status", status.value());
//
//        List<String> errors = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(x -> x.getDefaultMessage())
//                .collect(Collectors.toList());
//        body.put("errors", errors);
//
//        return new ResponseEntity<Object>(body, status);
//    }
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(InvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleInvalidException(InvalidException ex){
        return  new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse exception(Exception ex){
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }
}
