package co.tinab.darchin.controller.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by A.S.R on 3/6/2018.
 */

public class MyDateTime {
    public static String getToday(){
        Calendar calendar = Calendar.getInstance();
        String name = calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG, Locale.US);
        return name.toLowerCase();
    }

    public static String getTomorrow(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK,1);
        String name = calendar.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG, Locale.US);
        return name.toLowerCase();
    }

    public static Date getCurrentTime(){
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.US);
            Date date = new Date();
            return formatter.parse(formatter.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTodayDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = new Date();
        return formatter.format(date);
    }

    public static String getTomorrowDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,1);
        return formatter.format(calendar.getTime());
    }

    //example 2016-07-17 10:30:59  result -> 1395/4/17 10:30:59
    public static String convertGeorgianToShamsi(String datetime) {

        String timeStr = getTime(datetime);
        String dateStr = getDate(datetime);

        int ld;
        char[] chars = dateStr.toCharArray();
        int check =0;

        StringBuilder string_year = new StringBuilder();
        StringBuilder string_mounth = new StringBuilder();
        StringBuilder string_miladiDate = new StringBuilder();

        for (char aChar : chars) {
            if (aChar != '-') {
                if (check == 0) {
                    string_year.append(aChar).append("");
                } else if (check == 1) {
                    string_mounth.append(aChar).append("");
                } else {
                    string_miladiDate.append(aChar).append("");
                }

            } else {
                check++;
            }

        }

        int miladiYear = Integer.parseInt(string_year.toString());
        int miladiMonth = Integer.parseInt(string_mounth.toString());
        int miladiDate = Integer.parseInt(string_miladiDate.toString());

        int[] buf1 = new int[12];
        int[] buf2 = new int[12];

        buf1[0] = 0;
        buf1[1] = 31;
        buf1[2] = 59;
        buf1[3] = 90;
        buf1[4] = 120;
        buf1[5] = 151;
        buf1[6] = 181;
        buf1[7] = 212;
        buf1[8] = 243;
        buf1[9] = 273;
        buf1[10] = 304;
        buf1[11] = 334;

        buf2[0] = 0;
        buf2[1] = 31;
        buf2[2] = 60;
        buf2[3] = 91;
        buf2[4] = 121;
        buf2[5] = 152;
        buf2[6] = 182;
        buf2[7] = 213;
        buf2[8] = 244;
        buf2[9] = 274;
        buf2[10] = 305;
        buf2[11] = 335;

        int date;
        int month;
        int year;
        if ((miladiYear % 4) != 0) {
            date = buf1[miladiMonth - 1] + miladiDate;

            if (date > 79) {
                date = date - 79;
                if (date <= 186) {
                    switch (date % 31) {
                        case 0:
                            month = date / 31;
                            date = 31;
                            break;
                        default:
                            month = (date / 31) + 1;
                            date = (date % 31);
                            break;
                    }
                    year = miladiYear - 621;
                } else {
                    date = date - 186;

                    switch (date % 30) {
                        case 0:
                            month = (date / 30) + 6;
                            date = 30;
                            break;
                        default:
                            month = (date / 30) + 7;
                            date = (date % 30);
                            break;
                    }
                    year = miladiYear - 621;
                }
            } else {
                if ((miladiYear > 1996) && (miladiYear % 4) == 1) {
                    ld = 11;
                } else {
                    ld = 10;
                }
                date = date + ld;

                switch (date % 30) {
                    case 0:
                        month = (date / 30) + 9;
                        date = 30;
                        break;
                    default:
                        month = (date / 30) + 10;
                        date = (date % 30);
                        break;
                }
                year = miladiYear - 622;
            }
        } else {
            date = buf2[miladiMonth - 1] + miladiDate;

            if (miladiYear >= 1996) {
                ld = 79;
            } else {
                ld = 80;
            }
            if (date > ld) {
                date = date - ld;

                if (date <= 186) {
                    switch (date % 31) {
                        case 0:
                            month = (date / 31);
                            date = 31;
                            break;
                        default:
                            month = (date / 31) + 1;
                            date = (date % 31);
                            break;
                    }
                    year = miladiYear - 621;
                } else {
                    date = date - 186;

                    switch (date % 30) {
                        case 0:
                            month = (date / 30) + 6;
                            date = 30;
                            break;
                        default:
                            month = (date / 30) + 7;
                            date = (date % 30);
                            break;
                    }
                    year = miladiYear - 621;
                }
            } else {
                date = date + 10;

                switch (date % 30) {
                    case 0:
                        month = (date / 30) + 9;
                        date = 30;
                        break;
                    default:
                        month = (date / 30) + 10;
                        date = (date % 30);
                        break;
                }
                year = miladiYear - 622;
            }

        }

        return (year +"/"+ month +"/"+ date).concat("  ").concat(timeStr);
    }


    // get date from datetime
    private static String getDate(String input) {
        char[] chars = input.toCharArray();
        StringBuilder string_date = new StringBuilder();
        for (char aChar : chars) {
            if (aChar == ' ') {
                break;
            } else {
                string_date.append(aChar).append("");
            }
        }
        return string_date.toString();
    }

    // get time from datetime
    private static String getTime(String input) {
        char[] chars = input.toCharArray();
        int check =0;
        StringBuilder string_time = new StringBuilder();
        for (char aChar : chars) {
            if (aChar == ' ') {
                check = 1;
            } else if (check == 1) {
                string_time.append(aChar).append("");
            }
        }
        return string_time.toString();
    }
}
