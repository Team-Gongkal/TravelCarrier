package tc.travelCarrier.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.repository.MemberRepository;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordEncodingTest {
    public PasswordEncodingTest() {
        // 생성자 내용
    }

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testPasswordEncoding() {
        // 비번 바꿀 유저
        User savedUser = memberRepository.findUserById(1);

        // 비밀번호 인코딩
        String encodedPassword = new BCryptPasswordEncoder().encode(savedUser.getPassword());

        // 인코딩된 비밀번호로 유저 업데이트
        savedUser.setPw(encodedPassword);
        memberRepository.save(savedUser);

        // 업데이트된 유저 불러오기
        User updatedUser = memberRepository.findById(savedUser.getId()).orElse(null);

        // 업데이트된 비밀번호 확인
        assert updatedUser != null;
        assertTrue(new BCryptPasswordEncoder().matches("변경 전 비밀번호", updatedUser.getPassword()));

    }
}
