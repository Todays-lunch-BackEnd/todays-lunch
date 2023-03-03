package LikeLion.TodaysLunch.controller;

import LikeLion.TodaysLunch.repository.MemberRepository;
import LikeLion.TodaysLunch.repository.MenuRepository;
import LikeLion.TodaysLunch.repository.RestaurantRepository;
import LikeLion.TodaysLunch.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/likes")
public class LikeController {
    private final LikeService likeService;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public LikeController(LikeService likeService, RestaurantRepository restaurantRepository,
                          MenuRepository menuRepository,
                          MemberRepository memberRepository) {
        this.likeService = likeService;
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
        this.memberRepository = memberRepository;
    }

    @PutMapping("/{memberId}/{itemId}")
    public ResponseEntity<Void> likeItem(
            @PathVariable Long memberId,
            @PathVariable Long itemId,
            @RequestParam("itemType") String itemType) {

        if (!memberRepository.existsById(memberId)
                || !restaurantRepository.existsById(itemId)
                || !menuRepository.existsById(itemId)) {
            return ResponseEntity.notFound().build();
        }

        switch (itemType) {
            case "restaurant":
                likeService.createRestaurantLike(memberId, itemId);
                break;
            case "menu":
                likeService.createMenuLike(memberId, itemId);
                break;
            default:
                return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
