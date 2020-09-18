package student.adventure;

import java.util.Arrays;

import static java.lang.Character.isLetter;

public class Utils {

    //code below from:
    //https://stackoverflow.com/questions/18830813/how-can-i-remove-punctuation-from-input-text-in-java
    /**
     * Preliminarily formats a string, by removing punctuation
     * @param string the string we are formatting
     * @return the formatting string
     */
    public static String formatString(String string) {
        String formattedString = string.replaceAll("[^a-zA-Z]", " ")
                .replaceAll("\\s+", " ");
        return formattedString.trim();
    }

    /**
     * Joins strings of a string array into a single string
     * @param strArray array of strings passed in to join as a string
     * @param startIndex the index of strArray at which to start the joining process
     * @param endIndex the index of strArray at which to end the joining process
     * @return the joined string
     */
    public static String joinStringArray(String[] strArray, int startIndex, int endIndex) {
        StringBuffer sb = new StringBuffer();
        for(int i = startIndex; i < endIndex; i++) {
            sb.append(strArray[i]+" ");
        }
        return sb.toString().trim();
    }

    /**
     * Finds distance between two given locations
     * @param startLoc the starting location
     * @param endLoc the ending location
     * @return the distance between the two locations as a double
     */
    public static double findDistance(Location startLoc, Location endLoc) {
        return (double)(Math.round(Math.sqrt(Math.pow(endLoc.getX()- startLoc.getX(), 2)
                +Math.pow(endLoc.getY()- startLoc.getY(), 2))*100))/100;
        //returns double formatted to only 2 decimal places
    }
}
