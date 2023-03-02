package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.Menu;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.dto.MemberDto;
import LikeLion.TodaysLunch.dto.TokenDto;
import LikeLion.TodaysLunch.service.MenuService;
import LikeLion.TodaysLunch.service.RestaurantService;
import LikeLion.TodaysLunch.service.login.MemberService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final RestaurantService restaurantService;
    private final MenuService menuService;

    @Autowired
    public MemberController(MemberService memberService, RestaurantService restaurantService, MenuService menuService) {
        this.memberService = memberService;
        this.restaurantService = restaurantService;
        this.menuService = menuService;
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@Valid @RequestBody MemberDto memberDto) {
        try {
            memberService.join(memberDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        return ResponseEntity.ok("성공적으로 가입했습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDto memberDto) {
        TokenDto tokenDto;
        try {
            tokenDto = memberService.login(memberDto);
            return ResponseEntity.ok(tokenDto);
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            if (errorMessage.equals("존재하지 않는 회원입니다.")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }
            if (errorMessage.equals("비밀번호가 일치하지 않습니다.")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
            }
        }
    }

    @PostMapping("/logout-member")
    public ResponseEntity<String> logout(@RequestHeader String Authentication) {
        try {
            memberService.logout(Authentication);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        return ResponseEntity.ok("성공적으로 로그아웃했습니다.");
    }

    @GetMapping("/mypage")
    public ResponseEntity<?> myPage(@AuthenticationPrincipal Member member) {
        try {
            MemberDto memberDto = memberService.getAuthenticatedMember(member);
            return ResponseEntity.ok(memberDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{memberId}/likes/{itemId}")
    public ResponseEntity<Member> likeItem(
            @PathVariable Long memberId,
            @PathVariable Long itemId,
            @RequestParam("itemType") String itemType) {

        Member member = memberService.getMemberById(memberId);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        switch (itemType) {
            case "restaurant":
                Restaurant restaurant = restaurantService.getRestaurantById(itemId);
                if (restaurant == null) {
                    return ResponseEntity.notFound().build();
                }
                memberService.likeRestaurant(member, restaurant);
                break;
            case "menu":
                Menu menu = menuService.getMenuById(itemId);
                if (menu == null) {
                    return ResponseEntity.notFound().build();
                }
                memberService.likeMenu(member, menu);
                break;
            default:
                return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(member);
    }
}
