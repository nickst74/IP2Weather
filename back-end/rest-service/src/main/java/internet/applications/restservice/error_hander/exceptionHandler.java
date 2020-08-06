package internet.applications.restservice.error_hander;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// global handler for thrown exception during execution
// returns correct error depending on caught exception type
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class exceptionHandler extends ResponseEntityExceptionHandler {
    
    private ResponseEntity<Object> buildResponseEntity(api_error apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<Object> noLocationFound(HttpClientErrorException.BadRequest ex){
        api_error err = new api_error(HttpStatus.NOT_FOUND);
        err.setMessage("No location or weather data matches specified ip");
        return buildResponseEntity(err);
    }

    @ExceptionHandler(IPException.class)
    protected ResponseEntity<Object> uknownIPFormat(IPException ex){
        api_error err = new api_error(HttpStatus.BAD_REQUEST);
        err.setMessage(ex.getMessage());
        return buildResponseEntity(err);
    }

    @ExceptionHandler(DataAccessException.class)
    protected ResponseEntity<Object> DBDown(DataAccessException ex){
        api_error err = new api_error(HttpStatus.INTERNAL_SERVER_ERROR);
        err.setMessage("Database temporarily inaccessible");
        return buildResponseEntity(err);
    }

    @ExceptionHandler(ResourceAccessException.class)
    protected ResponseEntity<Object> DBDown(ResourceAccessException ex){
        api_error err = new api_error(HttpStatus.INTERNAL_SERVER_ERROR);
        err.setMessage("Unable to contact web services");
        return buildResponseEntity(err);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> generalHandler(Exception ex){
        api_error err = new api_error(HttpStatus.INTERNAL_SERVER_ERROR);
        err.setMessage("Unexpected error occured");
        return buildResponseEntity(err);
    }

}