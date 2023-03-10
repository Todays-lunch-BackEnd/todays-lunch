package LikeLion.TodaysLunch.domain;

import LikeLion.TodaysLunch.domain.relation.RestaurantLike;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Restaurant {
  @PrePersist
  public void prePersist() {
    this.rating = this.rating == null? 0.0:this.rating;
    this.judgement = this.judgement == null? false:this.judgement;
    this.reviewCount = this.reviewCount == null? 0L:this.reviewCount;
    this.agreement = this.agreement == null? 0L:this.agreement;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private String restaurantName;
  @ManyToOne
  @JoinColumn
  private FoodCategory foodCategory;
  @ManyToOne
  @JoinColumn
  private LocationCategory locationCategory;
  @ManyToOne
  @JoinColumn
  private LocationTag locationTag;
  @OneToOne
  @JoinColumn
  private ImageUrl imageUrl;
  private Double latitude;
  private Double longitude;

  private String address;
  private String introduction;
  private Double rating;
  private Boolean judgement;
  private LocalDate startDate;
  private LocalDate endDate;
  private Long agreement;
  private Long reviewCount;
  private Long lowestPrice;

  @OneToMany(mappedBy = "restaurant")
  private List<RestaurantLike> likes = new ArrayList<>();

  @OneToMany(mappedBy = "restaurant")
  private List<Menu> menus = new ArrayList<>();

  // 맛집 심사를 위한 등록에서 쓰임
  @Builder
  public Restaurant(String restaurantName, FoodCategory foodCategory,
      LocationCategory locationCategory, LocationTag locationTag, String address, String introduction) {
    this.restaurantName = restaurantName;
    this.foodCategory = foodCategory;
    this.locationCategory = locationCategory;
    this.locationTag = locationTag;
    this.address = address;
    this.introduction = introduction;
    this.judgement = true;
    this.startDate = LocalDate.now();
    this.endDate = LocalDate.now().plusDays(7);
  }
  public void setId(Long id) {
    this.id = id;
  }

  public void setImageUrl(ImageUrl imageUrl) {
    this.imageUrl = imageUrl;
  }

  public void setRating(Double rating) {
    this.rating = rating;
  }

  public void setReviewCount(Long reviewCount) {
    this.reviewCount = reviewCount;
  }
  /*public void setMember(Member member) { this.member = member; }*/
  public void setLowestPrice(Long lowestPrice) { this.lowestPrice = lowestPrice; }
}
