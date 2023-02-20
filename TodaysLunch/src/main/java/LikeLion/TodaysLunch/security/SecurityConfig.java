package LikeLion.TodaysLunch.security;

import LikeLion.TodaysLunch.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  // 정적인 파일에 대한 요청들
  private static final String[] AUTH_WHITELIST = {
      "/h2/**"
  };
  // 회원가입 시 비밀번호 암호화에 사용할 Encoder 빈 등록
  @Bean
  public BCryptPasswordEncoder encodePassword() {
    return new BCryptPasswordEncoder();
  }


  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
//        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        .and()
        .authorizeRequests()
        .antMatchers("/auth/**", "/menu/**", "/restaurants/**").permitAll()
        .anyRequest().hasRole("USER");
//        .and()
//        .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
//        .and()
//        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

  }
  @Override
  public void configure(WebSecurity web) throws Exception {
    // 정적인 파일 요청에 대해 무시
    web.ignoring().antMatchers(AUTH_WHITELIST);
  }
//  @Override
//  public void configure(AuthenticationManagerBuilder auth) throws Exception {
//    auth.userDetailsService(customUserDetailService()).passwordEncoder(encodePassword());
//  }
}
