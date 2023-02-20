package LikeLion.TodaysLunch.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
  private final AuthService authService;
  @PostMapping("/register")
  public String register(@RequestBody MemberRegisterRequestDto request){
    return authService.register(request);
  }
  @PostMapping("/login")
  public String login(@RequestBody JwtRequestDto request){
    try {
      return authService.login(request);
    } catch (Exception e) {
      return e.getMessage();
    }
  }
}
