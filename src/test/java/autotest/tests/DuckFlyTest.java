package autotest.tests;

import autotest.clients.DuckActionClient;
import autotest.payloads.DuckCreate;
import autotest.payloads.DuckMessage;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static autotest.payloads.WingsState.*;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckFlyTest extends DuckActionClient {
    @Test(description = "Полёт, ACTIVE крылья")
    @CitrusTest
    public void flyActive(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreate duckling = new DuckCreate().color("yellow")
                .height(8.0).material("rubber")
                .sound("quack").wingsState(ACTIVE);
        createDuck(runner, duckling);
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        duckFly(runner, "${duckId}");
        validateResources(runner, "duckFlyTest/duckFlyActive.json", HttpStatus.OK);
    }

    @Test(description = "Полёт, FIXED крылья")
    @CitrusTest
    public void flyFixed(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreate duckling = new DuckCreate().color("yellow")
                .height(8.0).material("rubber")
                .sound("quack").wingsState(FIXED);
        createDuck(runner, duckling);
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        duckFly(runner, "${duckId}");
        validateResponse(runner, "{\"message\":\"I can't fly\"}", HttpStatus.OK);
    }

    @Test(description = "Полёт, UNDEFINED крылья")
    @CitrusTest
    public void flyUndefined(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreate duckling = new DuckCreate().color("yellow")
                .height(8.0).material("rubber")
                .sound("quack").wingsState(UNDEFINED);
        createDuck(runner, duckling);
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        duckFly(runner, "${duckId}");
        DuckMessage message = new DuckMessage().message("Wings are not detected :(");
        validatePayloads(runner, message, HttpStatus.OK);
    }
}