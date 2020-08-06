package internet.applications.restservice.error_hander;

@SuppressWarnings("serial")
// custom exception thrown for bad ip format
public class IPException extends Exception{

    public IPException(String message){
        super(message);
    }

}