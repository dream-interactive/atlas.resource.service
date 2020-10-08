package core.exception;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

public class CustomRequestException extends RuntimeException{

    @Getter
    @Setter
    private HttpStatus status;

    public CustomRequestException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public CustomRequestException(String message) {
        super(message);
    }
}
