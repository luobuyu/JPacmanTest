package cn.edu.hust;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
public class PacmanConfigurationExceptionTest {
    private PacmanConfigurationException exception;

    @Test
    void testConstructor1() {
        String msg = "null pointer!!!";
        exception = new PacmanConfigurationException(msg);
        assertThat(exception.getMessage()).isEqualTo(msg);
    }
    @Test
    void testConstructor2() {
        String msg = "null pointer!!!";
        String cause = "divide zero!!!";
        exception = new PacmanConfigurationException(msg, new Throwable(cause));
        assertThat(exception.getMessage()).isEqualTo(msg);
        assertThat(exception.getCause().getMessage()).isEqualTo(cause);
    }

}
