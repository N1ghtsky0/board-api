package xyz.jiwook.toyBoard.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final String YEAR_DATE_FORMAT = "yyyy-MM-dd";
    private static final String MONTH_DATE_FORMAT = "MM-dd";
    private static final String TIME_FORMAT = "HH:mm";

    /**
     * 주어진 LocalDateTime을 현재 날짜를 기준으로 문자열로 포맷팅합니다.
     * 대상 날짜가 현재 날짜와 일치하는 경우 시간 형식으로 표시합니다.
     * 그렇지 않은 경우 월과 일 형식으로 표시합니다.
     *
     * @param targetDateTime 포맷팅할 LocalDateTime
     * @return targetDateTime의 포맷팅된 문자열
     */
    public static String summary(LocalDateTime targetDateTime) {
        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(YEAR_DATE_FORMAT));
        String targetDate = targetDateTime.format(DateTimeFormatter.ofPattern(YEAR_DATE_FORMAT));
        if (nowDate.equals(targetDate)) {
            return targetDateTime.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
        } else {
            return targetDateTime.format(DateTimeFormatter.ofPattern(MONTH_DATE_FORMAT));
        }
    }

    public static String full(LocalDateTime targetDateTime) {
        return targetDateTime.format(DateTimeFormatter.ofPattern(YEAR_DATE_FORMAT + " " + TIME_FORMAT));
    }
}