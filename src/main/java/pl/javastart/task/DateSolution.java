package pl.javastart.task;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateSolution {
    static final List<String> FORMATTER_WITH_TIME = List.of("yyyy-MM-dd HH:mm:ss", "dd.MM.yyyy HH:mm:ss");
    static final List<String> FORMATTER_WITHOUT_TIME = List.of("yyyy-MM-dd");
    static final Pattern PATTERN = Pattern.compile("(?i)([+-]?)(\\d+)([yMdhms])");

    public static ZonedDateTime returnDateWithTime(String date) {
        LocalDateTime localDateTime = null;

        for (String s : FORMATTER_WITH_TIME) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(s);
            try {
                localDateTime = LocalDateTime.parse(date, dateFormatter);
                return localDateTime.atZone(ZoneId.systemDefault());
            } catch (DateTimeParseException e) {
                //nop
            }
        }
        return null;
    }

    public static ZonedDateTime returnDateWithPattern(String date) {
        LocalDateTime localDateTime = LocalDateTime.now();
        Matcher matcher = PATTERN.matcher(date);
        while (matcher.find()) {
            String sign = matcher.group(1);
            Integer number = Integer.valueOf(matcher.group(2));
            String value = matcher.group(3);
            if (sign.equals("-")) {
                number = -number;
            }
            localDateTime = switch (value) {
                case "y" -> localDateTime.plusYears(number);
                case "M" -> localDateTime.plusMonths(number);
                case "d" -> localDateTime.plusDays(number);
                case "h" -> localDateTime.plusHours(number);
                case "m" -> localDateTime.plusMinutes(number);
                case "s" -> localDateTime.plusSeconds(number);
                default -> throw new IllegalArgumentException();
            };
        }

        return localDateTime.atZone(ZoneId.systemDefault());
    }


    public static ZonedDateTime returnDateWithoutTime(String date) {
        for (String s : FORMATTER_WITHOUT_TIME) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(s);
            try {
                LocalDate localDate = LocalDate.parse(date, dateFormatter);
                return LocalDateTime.of(localDate, LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault());
            } catch (DateTimeParseException e) {
                //nop
            }
        }
        return null;
    }

    public static ZonedDateTime returnDate(String date) {
        ZonedDateTime zonedDateTime = returnDateWithTime(date);
        if (zonedDateTime == null) {
            zonedDateTime = returnDateWithoutTime(date);
        }
        if (zonedDateTime == null) {
            zonedDateTime = returnDateWithPattern(date);
        }
        return zonedDateTime;
    }

    public static String formatCorrectTime(ZonedDateTime zonedDateTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateFormatter.format(zonedDateTime);
    }

}