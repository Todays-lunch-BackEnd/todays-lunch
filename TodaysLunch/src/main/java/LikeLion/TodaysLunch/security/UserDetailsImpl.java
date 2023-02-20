package LikeLion.TodaysLunch.security;

import LikeLion.TodaysLunch.domain.Member;
import java.util.ArrayList;
import java.util.Collection;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
  private static final String ROLE_PREFIX = "ROLE_";
  private final Member member;
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Role role = member.getRole();
    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ROLE_PREFIX + role.toString());
    Collection<GrantedAuthority> authorities = new ArrayList<>(); //List인 이유 : 여러개의 권한을 가질 수 있다
    authorities.add(authority);
    return authorities;
  }

  public String getNickName(){
    return member.getNickname();
  }

  public String getName(){
    return member.getName();
  }

  @Override
  public String getPassword() {
    return member.getPassword();
  }

  @Override
  public String getUsername() {
    return member.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
