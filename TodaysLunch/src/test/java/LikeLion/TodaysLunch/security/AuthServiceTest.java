package LikeLion.TodaysLunch.security;

import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
public class AuthServiceTest {
  @Autowired
  private MemberRepository memberRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private AuthService authService;

  @Test
  public void 로그인(){
    //given
    JwtRequestDto request = new JwtRequestDto();
    request.setEmail("jp3869@naver.com");
    request.setPassword("dreamer38");

    //when
    String email = authService.login(request);

    //then
    Assertions.assertEquals(request.getEmail(), email);
  }
}
