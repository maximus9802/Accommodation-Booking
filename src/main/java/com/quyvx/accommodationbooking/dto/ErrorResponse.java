package com.quyvx.accommodationbooking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private ErrorCode code;
    private HttpStatus status;

    public static ErrorResponse of(String message, ErrorCode code, HttpStatus status){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(message);
        errorResponse.setCode(code);
        errorResponse.setStatus(status);
        return errorResponse;
    }
}
