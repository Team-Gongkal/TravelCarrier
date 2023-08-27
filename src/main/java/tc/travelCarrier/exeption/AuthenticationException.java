package tc.travelCarrier.exeption;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(){
        super();
    }

    public AuthenticationException(String path, String msg){
        super(path + " => AuthenticationException ERROR : "+msg);
    }
}
