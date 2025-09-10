package assertion;

import exception.ScenarioInfoException;

public class Assertion {

    public static void assertEquals(String expected, String actual){

        try {
            org.testng.Assert.assertEquals(expected,actual);
        }catch (java.lang.AssertionError e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    public static void assertEquals(int expected, int actual){
        try {
            org.testng.Assert.assertEquals(expected,actual);
        }catch (java.lang.AssertionError e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }
    public static void assertEquals(double expected, double actual){
        try {
            org.testng.Assert.assertEquals(expected,actual);
        }catch (java.lang.AssertionError e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }
    public static void assertEquals(String expected, String actual, String text){
        try {
            org.testng.Assert.assertEquals(expected,actual);
        }catch (java.lang.AssertionError e){
            System.out.println(text);
            throw new ScenarioInfoException(text+"\n"+e.getMessage());
        }
    }

    public static void assertNotEquals(String expected, String actual, String text){
        try {
            org.testng.Assert.assertNotEquals(expected,actual);
        }catch (java.lang.AssertionError e){
            System.out.println(text);
            throw new ScenarioInfoException(text+"\n"+e.getMessage());
        }
    }

    public static void assertTrue(boolean expected){
        try {
            org.testng.Assert.assertTrue(expected);
        }catch (java.lang.AssertionError e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    public static void assertTrue(boolean expected, String text){
        try {
            org.testng.Assert.assertTrue(expected);
        }catch (java.lang.AssertionError e){
            System.out.println(text);
            throw new ScenarioInfoException(e.getMessage()+"\n"+text);
        }
    }

    public static void assertTrueWithContains(String expected, String actual){
        try {
            org.testng.Assert.assertTrue(expected.contains(actual));
        }catch (java.lang.AssertionError e){
            throw new ScenarioInfoException(e.getMessage());
        }
    }
}
