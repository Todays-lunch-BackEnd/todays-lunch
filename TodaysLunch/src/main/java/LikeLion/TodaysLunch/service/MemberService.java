package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    public MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
//
//    public Long signUp(String nickname, String password) {
//        Member member = new Member(nickname, password);
//        validateDuplication(member);
//        memberRepository.save(member);
//        return member.getId();
//    }

    private void validateDuplication(Member member) {
        memberRepository.findByNickname(member.getNickname())
                .ifPresent(e -> {
                    throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
                });
    }

    public void setFoodCategory(Member member, String foodCategory) {
        setCategory(member, foodCategory);
    }

    public void setLocationCategory(Member member, String locationCategory) {
        setCategory(member, locationCategory);
    }

    private void setCategory(Member member, String category) {
        memberRepository.findByNickname(member.getNickname())
                .ifPresent(value -> {
                            value.updateLocationCategory(category);
                            memberRepository.save(value);
                        });
    }


}
