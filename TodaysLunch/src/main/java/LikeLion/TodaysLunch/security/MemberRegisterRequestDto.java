package LikeLion.TodaysLunch.security;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRegisterRequestDto {
  private String email;
  private String password;
  private String name;
  private String nickname;
}
