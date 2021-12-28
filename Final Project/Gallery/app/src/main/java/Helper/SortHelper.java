package Helper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class SortHelper {

    private static SimpleDateFormat _formatter =
            new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ROOT);

    /*Sort criteria*/
    public enum SortCriteria {
        NAME,
        LAST_MODIFIED_DATE,
        FILE_SIZE
    }

    /*Sort type*/
    public enum SortType {
        INCREASE(1), DECREASE(-1);

        private int _value;
        public int value() {return _value; }
        private SortType(int value) {_value = value;}
    }

    public static void sort(File[] files, SortCriteria criteria, SortType type) {
        if (SortCriteria.NAME.equals(criteria))
        {
            Arrays.sort(files, (a, b) -> type.value()*a.getName().compareTo(b.getName()));
        }
        else if (SortCriteria.LAST_MODIFIED_DATE.equals(criteria))
        {
            Arrays.sort(files, (a, b) -> type.value()*Long.compare(a.lastModified(), b.lastModified()));
        }
        else if (SortCriteria.FILE_SIZE.equals(criteria))
        {
            Arrays.sort(files, (a, b) -> type.value()*Long.compare(a.length(), b.length()));
        }
    }



}
