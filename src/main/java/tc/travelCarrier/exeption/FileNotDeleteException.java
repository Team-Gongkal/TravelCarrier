package tc.travelCarrier.exeption;

public class FileNotDeleteException extends RuntimeException{
    
    public FileNotDeleteException(){
        super();
    }
    public FileNotDeleteException(String method, String msg){
        super(method + " => FileNotDeleteException ERROR : "+msg);
    }
}
