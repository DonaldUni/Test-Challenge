package TimeRecordMicroservice.Donald;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class helper {

    //TODO this class is to complete
    /*LocalDate localDate = LocalDate.now();

        String date = "09-09-2021";
        String h1 = "08:mm";
        String h2 = "26:mm";
        String h3 = "08:09";
        String h4 = "08:72";
        String h5 = "08:mmm";
        String h6 = "088:mm";


        /*System.out.println(isLocalTimeAvalaible(h1));
        System.out.println(isLocalTimeAvalaible(h2));
        System.out.println(isLocalTimeAvalaible(h3));
        System.out.println(isLocalTimeAvalaible(h4));*/

        /*System.out.println(isLocalTimeAvalaible(h5));
        System.out.println(isLocalTimeAvalaible(h6));*/
    //System.out.println(localDate);*/


    public static boolean isLocalTimeAvalaible(String time){

        //String timeFormat = "HH:mm";

        boolean isAvailable = true;

        if (time.contains(":")){
            int indexOfTwoPoint = time.indexOf(":");
            String part1 = time.substring(0,indexOfTwoPoint);
            String part2 = time.substring(indexOfTwoPoint+1);

            if (part1.length() == 2){
                int hour = -1;
                try {
                    hour = Integer.parseInt(part1);
                }catch (Exception e){
                    isAvailable = isAvailable && false;
                }

                if ((hour != -1) && (hour < 24) && (hour >= 0)){

                    isAvailable = true;
                }

                if (part2.length() == 2){
                    if (isAvailable){
                        int minutes = -1;
                        try {
                            minutes = Integer.parseInt(part2);
                        }catch (Exception e){
                            isAvailable = isAvailable && false;
                        }

                        if ((minutes != -1) && (minutes < 60) && (minutes >= 0)){

                            isAvailable = true;
                        }
                    }
                }

            }

        }

        return isAvailable;//To change

    }

    public static boolean isLocalDateAvalaible(String date){

        String dateFormat = "MM-dd-yyyy";
        //String dateString = "05-26-2020";

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat)
                .withResolverStyle(ResolverStyle.STRICT);

        try {
            System.out.println(LocalDate.parse(date, dateFormatter));
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;


    }
}
