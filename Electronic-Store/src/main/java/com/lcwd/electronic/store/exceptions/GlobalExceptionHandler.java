package com.lcwd.electronic.store.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.lcwd.electronic.store.dtos.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResouceNotFoundException(ResourceNotFoundException exception){
		ErrorResponse message = new ErrorResponse();
		message.setMessage(exception.getMessage());
		message.setHttpStatus(HttpStatus.NOT_FOUND);
		return new ResponseEntity<ErrorResponse>(message, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
		Map<String, String> errors = new HashMap<>();
		exception
		.getBindingResult()
		.getFieldErrors().
		forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

	}
	
	@ExceptionHandler(BadApiRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadApiRequestException(BadApiRequestException exception){
		ErrorResponse message = new ErrorResponse();
		message.setMessage(exception.getMessage());
		message.setHttpStatus(HttpStatus.BAD_REQUEST);
		return ResponseEntity.badRequest().body(message);
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException exception) {
		ErrorResponse message = new ErrorResponse();
		String errorMessage = "File too large! Maximum allowed size is " + exception.getMaxUploadSize() + " bytes.";
		message.setMessage(errorMessage);
		message.setHttpStatus(HttpStatus.PAYLOAD_TOO_LARGE);
        return new ResponseEntity<>(message, HttpStatus.PAYLOAD_TOO_LARGE);
    }


}
