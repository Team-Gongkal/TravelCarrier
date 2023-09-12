package tc.travelCarrier.exeption;

public class ConvertImgException  extends RuntimeException{

    public ConvertImgException(){
        super();
    }
    public ConvertImgException(String msg){
        super("ConvertImgException ERROR : "+msg);
    }
}