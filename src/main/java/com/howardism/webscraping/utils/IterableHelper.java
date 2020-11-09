package com.howardism.webscraping.utils;

import java.util.Collection;
import java.util.stream.StreamSupport;

public class IterableHelper {

    public static <T> Long size(Iterable<T> iterable) {
        if (iterable instanceof Collection) {
            int size = ((Collection<T>) iterable).size();
            return (long) size;
        }
        return StreamSupport.stream(iterable.spliterator(), false).count();
    }
}
