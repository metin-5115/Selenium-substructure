package exception;

/**
 * Custom exception: HeadlessNotSupportedException
 *
 * Thrown when a test tries to run in headless mode on a browser
 * that does not support headless execution.
 *
 * Example use case:
 *   - Internet Explorer or Safari where headless mode is not available
 *   - If configuration sets -Dheadless=true but browser cannot run headless
 */
public class HeadlessNotSupportedException extends IllegalStateException {

    /**
     * Creates an exception with a descriptive message
     * mentioning the unsupported browser.
     *
     * @param browser the browser name where headless is not supported
     */
    public HeadlessNotSupportedException(String browser) {
        super(String.format("Headless not supported for %s browser", browser));
    }
}
