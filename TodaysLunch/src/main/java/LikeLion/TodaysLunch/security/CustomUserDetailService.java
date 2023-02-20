package LikeLion.TodaysLunch.security;

import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.repository.MemberRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Transactional
public class CustomUserDetailService implements UserDetailsService {
  private final MemberRepository memberRepository;
  @Autowired
  public CustomUserDetailService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member member = memberRepository.findById(username)
        .orElseThrow(()-> new UsernameNotFoundException("등록되지 않은 사용자 입니다"));
    return new UserDetailsImpl(member);
  }
}
