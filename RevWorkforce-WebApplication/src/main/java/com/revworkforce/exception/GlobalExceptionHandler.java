package com.revworkforce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.revworkforce.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(ResourceNotFoundException ex){
        return new ResponseEntity<>(
                new ApiResponse(404, ex.getMessage(), null),
                HttpStatus.NOT_FOUND
        );
    }
	
	@ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleUnauthorized(UnauthorizedException ex) {
        return new ResponseEntity<>(
        		new ApiResponse(403, ex.getMessage(), null),
        		HttpStatus.FORBIDDEN
        );
    }
	
	@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleBadRequest(IllegalArgumentException ex) {
        return new ResponseEntity<>(
        		new ApiResponse(400, ex.getMessage(), null),
        		HttpStatus.BAD_REQUEST
        );
    }
	
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneral(Exception ex){
        return new ResponseEntity<>(
                new ApiResponse(500, ex.getMessage(), null),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}