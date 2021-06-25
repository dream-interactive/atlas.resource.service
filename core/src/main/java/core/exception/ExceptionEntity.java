package core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ExceptionEntity {

    @Getter
    private final String message;
    @Getter
    private final HttpStatus httpStatus;
    @Getter
    private final ZonedDateTime timestamp;

    public ExceptionEntity(String message, HttpStatus httpStatus, ZonedDateTime timestamp) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }
}
