package LikeLion.TodaysLunch.service.exception;

public class RestaurantNotFoundException extends RuntimeException {
    public RestaurantNotFoundException(Long memuId) {
        super("해당 ID의 맛집을 찾을 수 없습니다: " + memuId);
    }
}
