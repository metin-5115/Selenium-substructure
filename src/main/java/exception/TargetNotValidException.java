package exception;

/**
 * Custom exception: TargetNotValidException
 *
 * Purpose:
 *  - Thrown when an invalid target type is provided for WebDriver creation.
 *  - The framework supports only "local" or "remote/grid" execution.
 *
 * Example:
 *   If configuration contains:
 *     -Dtarget=invalid
 *   Then this exception will be thrown with a descriptive message.
 */
public class TargetNotValidException extends IllegalStateException {

    /**
     * Creates an exception message showing the invalid target and
     * reminding the user of valid options ("local" or "grid").
     *
     * @param target the invalid target name provided in configuration
     */
    public TargetNotValidException(String target) {
        super(String.format("Target %s not supported. Use either local or grid", target));
    }
}