package assertion;

import exception.ScenarioInfoException;

/**
 * Assertion
 *
 * Purpose:
 *  - Wrapper around TestNG's Assert class.
 *  - Adds custom error handling: wraps AssertionError into ScenarioInfoException.
 *  - This way, failure messages include browser, URL, and scenario info
 *    (via ScenarioInfoException).
 *
 * Benefits:
 *  - Clearer failure reports (more context than plain AssertionError).
 *  - Consistent assertion style across the framework.
 *
 * Usage:
 *   Assertion.assertEquals("expectedTitle", actualTitle);
 *   Assertion.assertTrue(element.isDisplayed(), "Login button should be visible");
 */
public class Assertion {

    /**
     * Asserts two Strings are equal.
     */
    public static void assertEquals(String expected, String actual){
        try {
            org.testng.Assert.assertEquals(expected, actual);
        } catch (java.lang.AssertionError e) {
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    /**
     * Asserts two ints are equal.
     */
    public static void assertEquals(int expected, int actual){
        try {
            org.testng.Assert.assertEquals(expected, actual);
        } catch (java.lang.AssertionError e) {
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    /**
     * Asserts two doubles are equal.
     */
    public static void assertEquals(double expected, double actual){
        try {
            org.testng.Assert.assertEquals(expected, actual);
        } catch (java.lang.AssertionError e) {
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    /**
     * Asserts two Strings are equal with a custom message.
     */
    public static void assertEquals(String expected, String actual, String text){
        try {
            org.testng.Assert.assertEquals(expected, actual);
        } catch (java.lang.AssertionError e) {
            System.out.println(text); // log to console
            throw new ScenarioInfoException(text + "\n" + e.getMessage());
        }
    }

    /**
     * Asserts two Strings are NOT equal with a custom message.
     */
    public static void assertNotEquals(String expected, String actual, String text){
        try {
            org.testng.Assert.assertNotEquals(expected, actual);
        } catch (java.lang.AssertionError e) {
            System.out.println(text); // log to console
            throw new ScenarioInfoException(text + "\n" + e.getMessage());
        }
    }

    /**
     * Asserts a boolean condition is true.
     */
    public static void assertTrue(boolean expected){
        try {
            org.testng.Assert.assertTrue(expected);
        } catch (java.lang.AssertionError e) {
            throw new ScenarioInfoException(e.getMessage());
        }
    }

    /**
     * Asserts a boolean condition is true with a custom message.
     */
    public static void assertTrue(boolean expected, String text){
        try {
            org.testng.Assert.assertTrue(expected);
        } catch (java.lang.AssertionError e) {
            System.out.println(text); // log to console
            throw new ScenarioInfoException(e.getMessage() + "\n" + text);
        }
    }

    /**
     * Asserts that the expected String contains the actual substring.
     */
    public static void assertTrueWithContains(String expected, String actual){
        try {
            org.testng.Assert.assertTrue(expected.contains(actual));
        } catch (java.lang.AssertionError e) {
            throw new ScenarioInfoException(e.getMessage());
        }
    }
}
