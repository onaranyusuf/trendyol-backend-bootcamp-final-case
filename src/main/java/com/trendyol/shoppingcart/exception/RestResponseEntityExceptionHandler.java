package com.trendyol.shoppingcart.exception;

import com.trendyol.shoppingcart.models.dto.ResponseDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler({ ShoppingCartException.class })
    public ResponseEntity<ResponseDTO> handleAccessDeniedException(Exception ex, WebRequest request) {
        ResponseDTO<String> responseDTO = new ResponseDTO(false, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(responseDTO);
    }
}
