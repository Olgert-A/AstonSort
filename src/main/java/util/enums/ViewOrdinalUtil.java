package util.enums;

public class ViewOrdinalUtil {
    public static int getOrdinalBy(int viewOrdinal) {
        return viewOrdinal-1;
    }

    public static int getViewOrdinal(int enumOrdinal) {
        return enumOrdinal+1;
    }
}
