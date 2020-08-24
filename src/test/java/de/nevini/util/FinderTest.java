package de.nevini.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;

public class FinderTest {

    private static final Function<Object, String[]> Object__toStringArray = obj -> new String[]{obj.toString()};

    private final String[] stringArray = new String[]{"value", "VALUE", "values", "devalue"};
    private final Collection<String> stringCollection = Arrays.asList(stringArray);
    private final Object[] objectArray = new Object[]{"value", "VALUE", "values", "devalue"};
    private final Collection<Object> objectCollection = Arrays.asList(objectArray);

    @Test
    public void testEmptyValueList() {
        // make sure the result is empty for all variants
        Assert.assertTrue(Finder.find(new String[0], "any").isEmpty());
        Assert.assertTrue(Finder.find(Collections.emptyList(), "any").isEmpty());
        Assert.assertTrue(Finder.find(new Object[0], Object::toString, "any").isEmpty());
        Assert.assertTrue(Finder.find(Collections.emptyList(), Object::toString, "any").isEmpty());
        Assert.assertTrue(Finder.findAny(new Object[0], Object__toStringArray, "any").isEmpty());
        Assert.assertTrue(Finder.findAny(Collections.emptyList(), Object__toStringArray, "any").isEmpty());
        Assert.assertTrue(Finder.findLenient(new String[0], "any").isEmpty());
        Assert.assertTrue(Finder.findLenient(Collections.emptyList(), "any").isEmpty());
        Assert.assertTrue(Finder.findLenient(new Object[0], Object::toString, "any").isEmpty());
        Assert.assertTrue(Finder.findLenient(Collections.emptyList(), Object::toString, "any").isEmpty());
        Assert.assertTrue(Finder.findAnyLenient(new Object[0], Object__toStringArray, "any").isEmpty());
        Assert.assertTrue(Finder.findAnyLenient(Collections.emptyList(), Object__toStringArray, "any").isEmpty());
    }

    @Test
    public void testEquals() {
        // make sure only the "equals" value is returned
        List<String> expected = new ArrayList<>(Collections.singleton("value"));
        Assert.assertEquals(expected, Finder.find(stringArray, "value"));
        Assert.assertEquals(expected, Finder.find(stringCollection, "value"));
        Assert.assertEquals(expected, Finder.find(objectArray, Object::toString, "value"));
        Assert.assertEquals(expected, Finder.find(objectCollection, Object::toString, "value"));
        Assert.assertEquals(expected, Finder.findAny(objectArray, Object__toStringArray, "value"));
        Assert.assertEquals(expected, Finder.findAny(objectCollection, Object__toStringArray, "value"));
        Assert.assertEquals(expected, Finder.findLenient(stringArray, "value"));
        Assert.assertEquals(expected, Finder.findLenient(stringCollection, "value"));
        Assert.assertEquals(expected, Finder.findLenient(objectArray, Object::toString, "value"));
        Assert.assertEquals(expected, Finder.findLenient(objectCollection, Object::toString, "value"));
        Assert.assertEquals(expected, Finder.findAnyLenient(objectArray, Object__toStringArray, "value"));
        Assert.assertEquals(expected, Finder.findAnyLenient(objectCollection, Object__toStringArray, "value"));
    }

    @Test
    public void testEqualsIgnoreCase() {
        // make sure the "equalsIgnoreCase" values are returned
        List<String> expected = new ArrayList<>(Arrays.asList("value", "VALUE"));
        Assert.assertEquals(expected, Finder.find(stringArray, "Value"));
        Assert.assertEquals(expected, Finder.find(stringCollection, "Value"));
        Assert.assertEquals(expected, Finder.find(objectArray, Object::toString, "Value"));
        Assert.assertEquals(expected, Finder.find(objectCollection, Object::toString, "Value"));
        Assert.assertEquals(expected, Finder.findAny(objectArray, Object__toStringArray, "Value"));
        Assert.assertEquals(expected, Finder.findAny(objectCollection, Object__toStringArray, "Value"));
        Assert.assertEquals(expected, Finder.findLenient(stringArray, "Value"));
        Assert.assertEquals(expected, Finder.findLenient(stringCollection, "Value"));
        Assert.assertEquals(expected, Finder.findLenient(objectArray, Object::toString, "Value"));
        Assert.assertEquals(expected, Finder.findLenient(objectCollection, Object::toString, "Value"));
        Assert.assertEquals(expected, Finder.findAnyLenient(objectArray, Object__toStringArray, "Value"));
        Assert.assertEquals(expected, Finder.findAnyLenient(objectCollection, Object__toStringArray, "Value"));
    }

    @Test
    public void testStartsWith() {
        // make sure the "startsWith" values are returned
        List<String> expected = new ArrayList<>(Arrays.asList("value", "VALUE", "values"));
        Assert.assertEquals(expected, Finder.find(stringArray, "val"));
        Assert.assertEquals(expected, Finder.find(stringCollection, "val"));
        Assert.assertEquals(expected, Finder.find(objectArray, Object::toString, "val"));
        Assert.assertEquals(expected, Finder.find(objectCollection, Object::toString, "val"));
        Assert.assertEquals(expected, Finder.findAny(objectArray, Object__toStringArray, "val"));
        Assert.assertEquals(expected, Finder.findAny(objectCollection, Object__toStringArray, "val"));
        Assert.assertEquals(expected, Finder.findLenient(stringArray, "val"));
        Assert.assertEquals(expected, Finder.findLenient(stringCollection, "val"));
        Assert.assertEquals(expected, Finder.findLenient(objectArray, Object::toString, "val"));
        Assert.assertEquals(expected, Finder.findLenient(objectCollection, Object::toString, "val"));
        Assert.assertEquals(expected, Finder.findAnyLenient(objectArray, Object__toStringArray, "val"));
        Assert.assertEquals(expected, Finder.findAnyLenient(objectCollection, Object__toStringArray, "val"));
    }

    @Test
    public void testContains() {
        // make sure the "contains" values are returned
        List<String> expected = new ArrayList<>(Arrays.asList("value", "VALUE", "values", "devalue"));
        Assert.assertEquals(expected, Finder.find(stringArray, "lu"));
        Assert.assertEquals(expected, Finder.find(stringCollection, "lu"));
        Assert.assertEquals(expected, Finder.find(objectArray, Object::toString, "lu"));
        Assert.assertEquals(expected, Finder.find(objectCollection, Object::toString, "lu"));
        Assert.assertEquals(expected, Finder.findAny(objectArray, Object__toStringArray, "lu"));
        Assert.assertEquals(expected, Finder.findAny(objectCollection, Object__toStringArray, "lu"));
        Assert.assertEquals(expected, Finder.findLenient(stringArray, "lu"));
        Assert.assertEquals(expected, Finder.findLenient(stringCollection, "lu"));
        Assert.assertEquals(expected, Finder.findLenient(objectArray, Object::toString, "lu"));
        Assert.assertEquals(expected, Finder.findLenient(objectCollection, Object::toString, "lu"));
        Assert.assertEquals(expected, Finder.findAnyLenient(objectArray, Object__toStringArray, "lu"));
        Assert.assertEquals(expected, Finder.findAnyLenient(objectCollection, Object__toStringArray, "lu"));
    }

    @Test
    public void testMostSimilar() {
        // makes sure the "mostSimilar" values are returned
        List<String> expected = new ArrayList<>(Collections.singleton("devalue"));
        Assert.assertEquals(expected, Finder.findLenient(stringArray, "vedalu"));
        Assert.assertEquals(expected, Finder.findLenient(stringCollection, "vedalu"));
        Assert.assertEquals(expected, Finder.findLenient(objectArray, Object::toString, "vedalu"));
        Assert.assertEquals(expected, Finder.findLenient(objectCollection, Object::toString, "vedalu"));
        Assert.assertEquals(expected, Finder.findAnyLenient(objectArray, Object__toStringArray, "vedalu"));
        Assert.assertEquals(expected, Finder.findAnyLenient(objectCollection, Object__toStringArray, "vedalu"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyQueryFindingStringInArray() {
        Finder.find(new String[]{"any"}, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyQueryFindingStringInCollection() {
        Finder.find(Collections.singleton("any"), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyQueryFindingIdentifierInArray() {
        Finder.find(new Object[]{new Object()}, Object::toString, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyQueryFindingIdentifierInCollection() {
        Finder.find(Collections.singleton(new Object()), Object::toString, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyQueryFindingIdentifiersInArray() {
        Finder.findAny(new Object[]{new Object()}, Object__toStringArray, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyQueryFindingIdentifiersInCollection() {
        Finder.findAny(Collections.singleton(new Object()), Object__toStringArray, "");
    }

    /*
    There is no need to explicitly test for null pointer exceptions, as these will occur "naturally",
    i.e. they are not explicitly thrown, but are side-effects of calling methods on them.
     */

}
