package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByNickname(String nickname);
    Boolean existsByEmail(String email);
}
