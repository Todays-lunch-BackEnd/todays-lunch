package LikeLion.TodaysLunch.service;

import LikeLion.TodaysLunch.domain.FoodCategory;
import LikeLion.TodaysLunch.domain.ImageUrl;
import LikeLion.TodaysLunch.domain.LocationCategory;
import LikeLion.TodaysLunch.domain.LocationTag;
import LikeLion.TodaysLunch.domain.Member;
import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.dto.JudgeDto;
import LikeLion.TodaysLunch.repository.DataJpaRestaurantRepository;
import LikeLion.TodaysLunch.repository.FoodCategoryRepository;
import LikeLion.TodaysLunch.repository.ImageUrlRepository;
import LikeLion.TodaysLunch.repository.LocationCategoryRepository;
import LikeLion.TodaysLunch.repository.LocationTagRepository;
import LikeLion.TodaysLunch.repository.MemberRepository;
import LikeLion.TodaysLunch.repository.RestaurantSpecification;
import LikeLion.TodaysLunch.s3.S3UploadService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Transactional
public class RestaurantService {
  private final DataJpaRestaurantRepository restaurantRepository;
  private final FoodCategoryRepository foodCategoryRepository;
  private final LocationTagRepository locationTagRepository;
  private final LocationCategoryRepository locationCategoryRepository;
  private final ImageUrlRepository imageUrlRepository;
  private final MemberRepository memberRepository;

  @Autowired
  private S3UploadService s3UploadService;

  public RestaurantService(DataJpaRestaurantRepository restaurantRepository,
      FoodCategoryRepository foodCategoryRepository,
      LocationTagRepository locationTagRepository,
      LocationCategoryRepository locationCategoryRepository,
      ImageUrlRepository imageUrlRepository,
      MemberRepository memberRepository
      ) {
    this.restaurantRepository = restaurantRepository;
    this.foodCategoryRepository = foodCategoryRepository;
    this.locationTagRepository = locationTagRepository;
    this.locationCategoryRepository = locationCategoryRepository;
    this.imageUrlRepository = imageUrlRepository;
    this.memberRepository = memberRepository;
  }

  public Page<Restaurant> restaurantList(
      String foodCategory, String locationCategory,
      String locationTag, String keyword,
      int page, int size, String sort, String order) {

    Pageable pageable = determineSort(page, size, sort, order);

    FoodCategory foodCategoryObj;
    LocationCategory locationCategoryObj;
    LocationTag locationTagObj;

    Specification<Restaurant> spec =(root, query, criteriaBuilder) -> null;
    if (foodCategory != null) {
      foodCategoryObj = foodCategoryRepository.findByName(foodCategory).get();
      spec = spec.and(RestaurantSpecification.equalFoodCategory(foodCategoryObj));
    }
    if (locationCategory != null) {
      locationCategoryObj = locationCategoryRepository.findByName(locationCategory).get();
      spec = spec.and(RestaurantSpecification.equalLocationCategory(locationCategoryObj));
    }
    if (locationTag != null) {
      locationTagObj = locationTagRepository.findByName(locationTag).get();
      spec = spec.and(RestaurantSpecification.equalLocationTag(locationTagObj));
    }
    if (keyword != null) {
      spec = spec.and(RestaurantSpecification.likeRestaurantName(keyword));
    }

    spec = spec.and(RestaurantSpecification.equalJudgement(false));

    return restaurantRepository.findAll(spec, pageable);
  }

  public Restaurant restaurantDetail(Long id){
    return restaurantRepository.findById(id).get();
  }

  public Restaurant createJudgeRestaurant(String address, String restaurantName, String foodCategoryName,
      String locationCategoryName, String locationTagName, String introduction,  MultipartFile restaurantImage)
      throws IOException {
    FoodCategory foodCategory = foodCategoryRepository.findByName(foodCategoryName)
        .orElseThrow(() -> new IllegalArgumentException("?????? ???????????? "+foodCategoryName+" ?????? ??????! ?????? ????????? ????????? ??? ????????????."));
    LocationCategory locationCategory = locationCategoryRepository.findByName(locationCategoryName)
        .orElseThrow(() -> new IllegalArgumentException("?????? ???????????? "+locationCategoryName+" ?????? ??????! ?????? ????????? ????????? ??? ????????????."));
    LocationTag locationTag = locationTagRepository.findByName(locationTagName)
        .orElseThrow(() -> new IllegalArgumentException("?????? ?????? "+locationTagName+" ?????? ??????! ?????? ????????? ????????? ??? ????????????."));

    JudgeDto judgeDto = new JudgeDto(restaurantName, foodCategory,
        locationCategory, locationTag, address, introduction);
    Restaurant restaurant = judgeDto.toEntity();

    if(!restaurantImage.isEmpty()) {
      ImageUrl imageUrl = new ImageUrl();
      String originalName = restaurantImage.getOriginalFilename();
      String savedUrl = s3UploadService.upload(restaurantImage, "judge_restaurant");
      imageUrl.setOriginalName(originalName);
      imageUrl.setImageUrl(savedUrl);
      imageUrlRepository.save(imageUrl);
      restaurant.setImageUrl(imageUrl);
    }

    return restaurantRepository.save(restaurant);
  }

  public Page<Restaurant> judgeRestaurantList(Pageable pageable){
    Specification<Restaurant> spec =(root, query, criteriaBuilder) -> null;
    spec = spec.and(RestaurantSpecification.equalJudgement(true));
    return restaurantRepository.findAll(spec, pageable);
  }
  // ?????? ?????? ?????? : ?????? ????????? ????????? ?????? ????????? ???????????? ???????????? (?????????????)
  public List<Restaurant> recommendation(Long userId){
    Member member = memberRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("?????? ????????? ?????? ?????? ?????? ?????? ??????!"));

    FoodCategory foodCategory = member.getFoodCategory();
    LocationCategory locationCategory = member.getLocationCategory();
    Specification<Restaurant> spec =(root, query, criteriaBuilder) -> null;
    if (foodCategory != null){
      spec = spec.and(RestaurantSpecification.equalFoodCategory(foodCategory));
    }
    if (locationCategory != null){
      spec = spec.and(RestaurantSpecification.equalLocationCategory(locationCategory));
    }
    Pageable pageable = determineSort(0, 5, "rating", "descending");
    Page<Restaurant> recmdResult = restaurantRepository.findAll(spec, pageable);

    // ?????? ?????? ?????? 5????????? ?????? ??????, ?????? ???????????? ?????? ?????? ????????? ????????? ?????? ?????? ?????????.
    List<Restaurant> rest =  new ArrayList<>();
    if(recmdResult.getNumberOfElements() < 5){
      pageable = determineSort(0, 5-recmdResult.getNumberOfElements(), "rating", "descending");
      rest = restaurantRepository.findAll((root, query, criteriaBuilder) -> null, pageable).getContent();
    }
    List<Restaurant> finalList =  new ArrayList<>();
    finalList.addAll(recmdResult.getContent());
    finalList.addAll(rest);
    return finalList;
  }

  public Pageable determineSort(int page, int size, String sort, String order){
    Pageable pageable = PageRequest.of(page, size);
    if(order.equals("ascending")){
      pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
    } else if(order.equals("descending")){
      pageable = PageRequest.of(page, size, Sort.by(sort).descending());
    }
    return pageable;
  }

  public Restaurant getRestaurantById(Long itemId) {
    return restaurantRepository.findById(itemId)
            .orElseThrow(()-> new IllegalArgumentException("???????????? ?????? ???????????????."));
  }
}
