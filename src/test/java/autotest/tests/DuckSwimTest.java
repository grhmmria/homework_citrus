package autotest.tests;

import autotest.payloads.DuckCreate;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static autotest.payloads.WingsState.ACTIVE;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

import autotest.clients.DuckActionClient;

public class DuckSwimTest extends DuckActionClient {

    @Test(description = "Плавание, существующий id")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreate duckling = new DuckCreate().color("yellow").height(8.0).material("rubber").sound("quack").wingsState(ACTIVE);
        createDuck(runner, duckling);
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        duckSwim(runner, "${duckId}");
        validateResources(runner, "duckSwimTest/duckSwimExisting.json", HttpStatus.OK);
    }

    // так как случая для несуществующего айди не прописано в документации оставила фактический
    @Test(description = "Плавание, несуществующий id")
    @CitrusTest
    public void unsuccessfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreate duckling = new DuckCreate().color("yellow").height(8.0).material("rubber").sound("quack").wingsState(ACTIVE);
        createDuck(runner, duckling);
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        deleteDuck(runner, "${duckId}");
        duckSwim(runner, "${duckId}");
        validateResources(runner, "duckSwimTest/duckSwimNotExisting.json", HttpStatus.NOT_FOUND);
    }

}
