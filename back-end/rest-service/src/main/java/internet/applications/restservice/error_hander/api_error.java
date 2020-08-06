package internet.applications.restservice.error_hander;

import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

// a simple custom error to return as a response in case something goes wrong
@Setter
@Getter
public class api_error {
    private Date timestamp;
    private String message;
    private HttpStatus status;

    private api_error(){
        this.timestamp = new Date();
    }

    api_error(HttpStatus status){
        this();
        this.status = status;
    }

    api_error(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
    }

    api_error(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
    }

}