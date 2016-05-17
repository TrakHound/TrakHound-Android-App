package org.trakhound.www.trakhound;

/**
 * Created by Patrick on 5/16/2016.
 */
public class TH_Tools {

    public static String capitalizeFirst(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

}
