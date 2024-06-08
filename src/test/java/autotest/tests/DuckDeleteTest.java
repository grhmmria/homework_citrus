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

public class DuckDeleteTest extends DuckActionClient {


    @Test(description = "Удаление утки")
    @CitrusTest
    public void deleteExisting(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.0, "rubber", "quack", "ACTIVE");
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        deleteDuck(runner, "${duckId}");
        validateResponse(runner, "{\"message\":\"Duck is deleted\"}", HttpStatus.OK);
    }
}
