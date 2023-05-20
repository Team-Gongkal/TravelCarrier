package tc.travelCarrier.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler{


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        System.out.println("========로그인 실패===========");
        String msg = "Invalid Email or Password";
        // exception 처리
        if(exception instanceof UsernameNotFoundException) msg = "UsernameNotFound";
        else if(exception instanceof BadCredentialsException) msg = "BadCredentialsException account";
        setDefaultFailureUrl("/TravelCarrier/member/login?error=true&exception="+msg);

        super.onAuthenticationFailure(request, response,exception);
    }
}
