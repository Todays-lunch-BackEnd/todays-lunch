package LikeLion.TodaysLunch.service.exception;

public class MenuNotFoundException extends RuntimeException {
    public MenuNotFoundException(Long memuId) {
        super("해당 ID의 메뉴를 찾을 수 없습니다: " + memuId);
    }
}
