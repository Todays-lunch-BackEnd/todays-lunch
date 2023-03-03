package LikeLion.TodaysLunch.repository;

import LikeLion.TodaysLunch.domain.Restaurant;
import LikeLion.TodaysLunch.domain.Review;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  Page<Review> findAllByRestaurant(Restaurant restaurant, Pageable pageable);

  Optional<Review> findByRestaurantIdAndId(Long restaurantId, Long reviewId);
}
