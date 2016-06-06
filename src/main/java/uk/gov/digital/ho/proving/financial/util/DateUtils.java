package uk.gov.digital.ho.proving.financial.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Optional;

import static java.time.ZoneId.systemDefault;
/**
 * @Author Home Office Digital
 */
public class DateUtils {


    public static final String IPS_DATE_FORMAT = "yyyy-M-d";

    /**
     * Parse a string date representation in a flexible ISO format {@link #IPS_DATE_FORMAT}
     * @param date
     * @return LocalDate corresponding to the input or empty if the input string does not parse
     */
    public static Optional<LocalDate> parseIsoDate(String date) {
        try {
            return Optional.of(LocalDate.parse(date, DateTimeFormatter.ofPattern(IPS_DATE_FORMAT)));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    /**
     * Convert LocalDate to Date using the system default time zone.
     * @param localDate
     * @return
     */
    public static Date dateWithDefaultZone(LocalDate localDate){
        return Date.from(asLocalInstant(localDate));
    }


    private static Instant asLocalInstant(LocalDate localDate) {
        return localDate.atStartOfDay().atZone(systemDefault()).toInstant();
    }
}
