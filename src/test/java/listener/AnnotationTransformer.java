package listener;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;


/**
 * AnnotationTransformer
 *
 * Purpose:
 *  - A TestNG annotation transformer that automatically applies a retry analyzer
 *    to all test methods.
 *  - Eliminates the need to manually annotate each test with @Test(retryAnalyzer=...).
 *
 * How it works:
 *  - For every test method discovered by TestNG, the transform() method is called.
 *  - Here, the RetryAnalyzer.class is set on the annotation.
 *
 * Example:
 *  - Normally: @Test(retryAnalyzer = RetryAnalyzer.class)
 *  - With this transformer: @Test is enough, RetryAnalyzer is applied automatically.
 */
public class AnnotationTransformer implements IAnnotationTransformer {

    /**
     * Called by TestNG for each test method annotation.
     *
     * @param annotation     the test annotation being transformed
     * @param testClass      the class containing the test method
     * @param testConstructor the constructor (if applicable)
     * @param testMethod     the test method itself
     */
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        // Attach RetryAnalyzer to all test methods dynamically
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}