package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.Menu;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.domain.relation.MenuLike;
import LikeLion.TodaysLunch.domain.relation.RestaurantLike;
import LikeLion.TodaysLunch.repository.MemberRepository;
import LikeLion.TodaysLunch.repository.MenuRepository;
import LikeLion.TodaysLunch.repository.RestaurantRepository;
import LikeLion.TodaysLunch.service.exception.MemberNotFoundException;
import LikeLion.TodaysLunch.service.exception.MenuNotFoundException;
import LikeLion.TodaysLunch.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public LikeService(MemberRepository memberRepository, MenuRepository menuRepository,
                       RestaurantRepository restaurantRepository) {
        this.memberRepository = memberRepository;
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public void createRestaurantLike(Long memberId, Long restaurantId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));

        if (member.getRestaurantLikes()
                .stream()
                .anyMatch(like -> like.getRestaurant().equals(restaurant))) {
            return;
        }
        member.addRestaurantLike(new RestaurantLike(member, restaurant));
        memberRepository.save(member);
    }

    @Transactional
    public void createMenuLike(Long memberId, Long menuId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException(menuId));

        if (member.getMenuLikes()
                .stream()
                .anyMatch(like -> like.getMenu().equals(menu))) {
            return;
        }
        member.addMenuLike(new MenuLike(member, menu));
        memberRepository.save(member);
    }


}
