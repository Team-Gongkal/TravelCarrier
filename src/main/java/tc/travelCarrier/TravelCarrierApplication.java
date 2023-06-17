package tc.travelCarrier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TravelCarrierApplication {
	@Bean
	public BCryptPasswordEncoder encoder(){
		return new BCryptPasswordEncoder(); //순환참조오류 이슈 때문에 @bean을 여기로 뺐음!
	}
	public static void main(String[] args) {

		SpringApplication.run(TravelCarrierApplication.class, args);
	}

}
