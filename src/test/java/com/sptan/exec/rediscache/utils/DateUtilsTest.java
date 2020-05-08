package com.sptan.exec.rediscache.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;


/**
 * @author liupeng
 * @date 2020-04-28 14:31
 */
class DateUtilsTest {

    @Test
    void differenceMinute() {
    }

    @Test
    public void givenArraysAsList_thenInitialiseList() {
        List<String> list = Arrays.asList("foo", "bar");

        Assert.assertTrue(list.contains("foo"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListOf() {
        List<String> list = List.of("foo", "bar", "baz");
        Set<String> set = Set.of("foo", "bar", "baz");
        list.add("dd");
    }
}