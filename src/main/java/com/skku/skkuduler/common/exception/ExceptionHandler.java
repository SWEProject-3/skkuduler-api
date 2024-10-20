package com.skku.skkuduler.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {


        ErrorDTO errorDTO = new ErrorDTO(
                Error.INVALID_FIELD.getStatus(),
                Error.INVALID_FIELD.getMsg(),
                Error.INVALID_FIELD.getCode()
        );

        return ResponseEntity.status(errorDTO.getStatus()).body(errorDTO);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ErrorException.class)
    public ResponseEntity<ErrorDTO> handleCustomErrorException(ErrorException e) {
        Error error = e.getError();
        ErrorDTO errorDTO = new ErrorDTO(
                error.getStatus(),
                error.getMsg(),
                error.getCode()
        );

        return ResponseEntity.status(errorDTO.getStatus()).body(errorDTO);
    }

    @Setter
    @Getter
    public static class ErrorDTO {
        private int status;
        private String code;
        private String msg;

        public ErrorDTO(int status, String msg, String code) {
            this.status = status;
            this.msg = msg;
            this.code = code;
        }
    }
}
