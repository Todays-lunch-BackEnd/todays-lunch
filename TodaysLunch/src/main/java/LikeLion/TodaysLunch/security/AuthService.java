package LikeLion.TodaysLunch.security;

import LikeLion.TodaysLunch.repository.MemberRepository;
import LikeLion.TodaysLunch.domain.Member;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
@Transactional
@AllArgsConstructor
public class AuthService {
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

  public String register(MemberRegisterRequestDto request){
    boolean existMember = memberRepository.existsByEmail(request.getEmail());
    // 이미 회원이 존재하는 경우
    if (existMember) return null;

    Member member = new Member(request);
    member.encryptPassword(passwordEncoder);
    memberRepository.save(member);
    return member.getEmail();
  }
  public String login(JwtRequestDto request){
    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
    return principal.getUsername();
  }
}
