package tc.travelCarrier.exeption;

import java.io.UncheckedIOException;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(){
        super();
    }

    public FileNotFoundException(String message){
        super(message+" 파일이 없습니다!!");
    }
}
