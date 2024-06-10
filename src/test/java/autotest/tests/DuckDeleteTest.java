package autotest.tests;

import autotest.clients.DuckActionClient;
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

public class DuckDeleteTest extends DuckActionClient {

    @Test(description = "Удаление утки")
    @CitrusTest
    public void deleteExisting(@Optional @CitrusResource TestCaseRunner runner) {
        DuckCreate duckling = new DuckCreate().color("yellow")
                .height(5.0).material("rubber")
                .sound("quack").wingsState(ACTIVE);
        createDuck(runner, duckling);
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        deleteDuck(runner, "${duckId}");
        validateResources(runner, "duckDeleteTest/duckDeleteExisting.json", HttpStatus.OK);

    }


}