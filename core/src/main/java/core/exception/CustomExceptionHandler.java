package core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;


@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = {CustomRequestException.class})
    public ResponseEntity<Object> handlerException(CustomRequestException e) {
        HttpStatus status = e.getStatus() != null ? e.getStatus() : HttpStatus.BAD_REQUEST;

        ExceptionEntity exceptionEntity = new ExceptionEntity(
                e.getMessage(),
                status,
                ZonedDateTime.now(ZoneId.systemDefault())
        );
        return new ResponseEntity<>(exceptionEntity, status);
    }
}
