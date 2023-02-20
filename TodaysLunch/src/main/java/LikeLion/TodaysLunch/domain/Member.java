package LikeLion.TodaysLunch.domain;

import LikeLion.TodaysLunch.security.MemberRegisterRequestDto;
import LikeLion.TodaysLunch.security.Role;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Member {
    @Id
    private String email;
    private String nickname;
    private String name;

    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    private LocationCategory locationCategory;
    @ManyToOne
    private FoodCategory foodCategory;

    public Member(MemberRegisterRequestDto request) {
        this.nickname = request.getNickname();
        this.password = request.getPassword();
        this.email = request.getEmail();
        this.name = request.getName();
        this.role = Role.USER;
    }
    public void encryptPassword(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }

//    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "member")
//    private List<Review> review = new ArrayList<>();



    public void updateLocationCategory(String locationCategory) {
        this.locationCategory = new LocationCategory(locationCategory);
    }


}
