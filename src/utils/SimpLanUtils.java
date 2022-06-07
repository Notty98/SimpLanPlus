package src.utils;

import java.util.Date;

public class SimpLanUtils {


    public static String generateLabel(String suffix) {
        Date date = new Date();
        return suffix + date.getTime();
    }

    public static String generateLabel() {
        return Long.toString(new Date().getTime());
    }

}
