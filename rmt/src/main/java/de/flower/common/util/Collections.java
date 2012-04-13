package de.flower.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author flowerrrr
 */
public class Collections {

    private Collections() {}

    public static <S, T> List<T> convert(List<S> in, IElementConverter<S, T> converter) {
        if (in == null) {
            return null;
        }
        List<T> out = new ArrayList<T>();
        for (S element : in) {
            out.add(converter.convert(element));
        }
        return out;
    }

    public static <T> List<T> flattenArray(final List<T[]> in) {
        List<T> result = new ArrayList<T>();
        for (T[] tArray : in) {
            for (T t : tArray) {
                result.add(t);
            }
        }
        return result;
    }

    public static interface IElementConverter<S, T> {

        T convert(S element);
    }
}
