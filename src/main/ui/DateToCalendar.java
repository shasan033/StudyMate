package ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// Represents implementation of dateToCalendar to increase abstraction
public interface DateToCalendar {

    // EFFECTS: generates Calendar value from Date
    // Throws ParseException if Date value cannot be parsed
    // NOTE: method exists as previous methods are deprecated
    default Calendar dateToCalendar(Date date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.CANADA);
        String dateInString = date.toString();
        try {
            calendar.setTime(sdf.parse(dateInString));
        } catch (ParseException e) {
            throw new ParseException("Unparseable data", 0);
        }
        return calendar;
    }
}
