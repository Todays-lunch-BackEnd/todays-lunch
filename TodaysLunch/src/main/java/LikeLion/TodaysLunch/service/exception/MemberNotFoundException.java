package LikeLion.TodaysLunch.service.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(Long memberId) {
        super("해당 ID의 멤버를 찾을 수 없습니다: " + memberId);
    }
}

