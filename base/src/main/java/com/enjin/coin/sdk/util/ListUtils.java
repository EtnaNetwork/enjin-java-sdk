package com.enjin.coin.sdk.util;

import com.enjin.coin.sdk.util.OptionalUtils;
import com.enjin.coin.sdk.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

public final class ListUtils {

    protected ListUtils() {

    }

    /**
     * Method to check if a list is empty.
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(List list) {
        return ObjectUtils.isNull(list) || list.size() == 0;
    }

    /**
     * Method to check if a list is not empty.
     *
     * @param list
     * @return
     */
    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }

    /**
     * Method to check if an optional list is empty.
     *
     * @param optional
     * @return
     */
    public static boolean isEmpty(Optional<? extends List> optional) {
        return ObjectUtils.isNull(optional) || OptionalUtils.isNotPresent(optional) || isEmpty(optional.get());
    }

    /**
     * Method to check if an optional list is not empty.
     *
     * @param optional
     * @return
     */
    public static boolean isNotEmpty(Optional<? extends List> optional) {
        return !isEmpty(optional);
    }

}
