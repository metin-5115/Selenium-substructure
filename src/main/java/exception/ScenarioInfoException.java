package exception;

import factory.DriverFactory;
import factory.DriverFactory;

/**
 * Custom exception: ScenarioInfoException
 *
 * Purpose:
 *  - Wraps an error message with additional runtime information
 *    about the current WebDriver session.
 *  - Provides context for debugging failed test steps.
 *
 * The message includes:
 *  - Browser name, version, and platform (from DriverFactory.getInfo()).
 *  - Current URL being tested (from WebDriver.getCurrentUrl()).
 *  - Original error message.
 *
 * Example:
 *  browser: chrome v: 114.0 platform: windows
 *  https://example.com/page
 *  Element not found: #login-button
 */
public class ScenarioInfoException extends IllegalStateException {

    /**
     * Constructs the exception message enriched with driver/session info.
     *
     * @param error the original error message (e.g., from AssertionError, TimeoutException, etc.)
     */
    public ScenarioInfoException(String error){
        super(
                String.format(
                        new DriverFactory().getInfo().toUpperCase() + "\n" +
                                DriverFactory.getDriver().getCurrentUrl() + "\n" +
                                error
                )
        );
    }
}
