package autotest.tests;

import autotest.clients.DuckActionClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckFlyTest extends DuckActionClient {
    @Test(description = "Полёт, ACTIVE крылья")
    @CitrusTest
    public void flyActive(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.0, "rubber", "quack", "ACTIVE");
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\"message\":\"I am flying\"}", HttpStatus.OK);
    }

    @Test(description = "Полёт, FIXED крылья")
    @CitrusTest
    public void flyFixed(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.0, "rubber", "quack", "FIXED");
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\"message\":\"I can't fly\"}",HttpStatus.OK);
    }

    @Test(description = "Полёт, UNDEFINED крылья")
    @CitrusTest
    public void flyUndefined(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.0, "rubber", "quack", "UNDEFINED");
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\"message\":\"Wings are not detected :(\"},",HttpStatus.OK);
    }
}
