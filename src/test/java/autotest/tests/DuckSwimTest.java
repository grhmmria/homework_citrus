package autotest.tests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;
import autotest.clients.DuckActionClient;

public class DuckSwimTest extends DuckActionClient {

    @Test(description = "Плавание, существующий id")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.0, "rubber", "quack", "ACTIVE");
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        duckSwim(runner, "${duckId}");
        validateResponse(runner, "{\"message\": \"I’m swimming\"}", HttpStatus.OK);
    }

    // так как случая для несуществующего айди не прописано в документации оставила фактический
    @Test(description = "Плавание, несуществующий id")
    @CitrusTest
    public void unsuccessfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.0, "rubber", "quack", "ACTIVE");
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        deleteDuck(runner, "${duckId}");
        duckSwim(runner, "${duckId}");
        validateResponse(runner, "{\"message\": \"Paws are not found ((((\" }", HttpStatus.NOT_FOUND);
    }

}
