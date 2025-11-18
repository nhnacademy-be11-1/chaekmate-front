package shop.chaekmate.front.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class StringToLocalDateTime {

    private StringToLocalDateTime(){}

    public static LocalDateTime parseToLocalDateTime(String dateStr) {
        // 허용할 날짜/시간 패턴 목록
        String[] patterns = {
                "yyyy-MM-dd",
                "yyyy/MM/dd",
                "yyyy-MM-dd HH:mm:ss",
                "yyyy/MM/dd HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm:ss"
        };

        try {
            // DateUtils가 패턴을 순서대로 시도하며 파싱
            Date date = DateUtils.parseDate(dateStr, patterns);

            // Date → LocalDateTime 변환
            return date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (Exception e) {
            // 변환 실패 시 null 반환 (필요하면 예외 던지기)
            log.warn("Failed to parse date: " + dateStr);
            return LocalDateTime.now();
        }
    }
}
