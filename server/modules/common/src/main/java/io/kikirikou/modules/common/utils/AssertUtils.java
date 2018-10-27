package io.kikirikou.modules.common.utils;

import java.util.Collection;
import java.util.Map;


public class AssertUtils {

    /**
     * AssertUtils that the given String has actual non-whitepsace text.
     *
     * @param str
     * @param propOrMsg
     * @return
     */
    public static String hasText(String str, String propOrMsg) {
        boolean hasText = false;
        int strLen = hasLength(str, propOrMsg).length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                hasText = true;
            }
        }
        return assertThat(hasText, propOrMsg, "has no text", str);
    }

    /**
     * AssertUtils that the given object is not null
     *
     * @param <T>
     * @param object
     * @param propOrMsg
     * @return
     */
    public static <T> T notNull(T object, String propOrMsg) {
        return assertThat(object != null, propOrMsg, "is null", object);
    }

    /**
     * AssertUtils that the given object is not null
     *
     * @param <T>
     * @param object
     * @param propOrMsg
     * @return
     */
    public static <T> T isNull(T object, String propOrMsg) {
        return assertThat(object == null, propOrMsg, "is not null", object);
    }

    /**
     * AssertUtils that the given String is not empty
     *
     * @param str
     * @param propOrMsg
     * @return
     */
    public static String hasLength(String str, String propOrMsg) {
        return assertThat(str != null && str.length() > 0, propOrMsg, "is empty", str);
    }

    /**
     * AssertUtils that the given array is not empty
     *
     * @param <T>
     * @param objects
     * @param propOrMsg
     * @return
     */
    public static <T> T[] notEmpty(T[] objects, String propOrMsg) {
        return assertThat(objects != null && objects.length > 0, propOrMsg, "is empty", objects);
    }

    /**
     * AssertUtils that the given Map is not empty
     *
     * @param <M>
     * @param map
     * @param propOrMsg
     * @return
     */
    public static <M extends Map<?, ?>> M notEmpty(M map, String propOrMsg) {
        return assertThat(!map.isEmpty(), propOrMsg, "is empty", map);
    }

    /**
     * AssertUtils that the given Collection is not empty
     *
     * @param <C>
     * @param col
     * @param propOrMsg
     * @return
     */
    public static <C extends Collection<?>> C notEmpty(C col, String propOrMsg) {
        return assertThat(!col.isEmpty(), propOrMsg, "is empty", col);
    }

    /**
     * General assertion
     *
     * @param <T>
     * @param condition
     * @param propOrMsg
     * @return
     */
    public static <T> T assertThat(boolean condition, String propOrMsg) {
        return assertThat(condition, propOrMsg, "", null);
    }

    /**
     * General assertion
     *
     * @param <T>
     * @param condition
     * @param propOrMsg
     * @param msgSuffix
     * @param rv
     * @return
     */
    public static <T> T assertThat(boolean condition, String propOrMsg, String msgSuffix, T rv) {
        if (!condition) {
            if (propOrMsg.contains(" ")) {
                throw new IllegalArgumentException(propOrMsg);
            } else {
                throw new IllegalArgumentException(propOrMsg + " " + msgSuffix);
            }
        }
        return rv;
    }
}