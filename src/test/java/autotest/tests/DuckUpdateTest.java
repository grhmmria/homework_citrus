package autotest.tests;

import autotest.clients.DuckActionClient;
import autotest.payloads.DuckCreate;
import autotest.payloads.DuckMessage;
import autotest.payloads.DuckUpdate;
import autotest.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static autotest.payloads.WingsState.ACTIVE;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckUpdateTest extends DuckActionClient {

    @Test(description = "Обновление цвета и высоты уточки")
    @CitrusTest
    public void updateColorAndHeight(@Optional @CitrusResource TestCaseRunner runner, @Optional @CitrusResource TestContext context) {
        String material = "rubber";
        String sound = "quack";
        WingsState wingsState = ACTIVE;
        DuckCreate duckling = new DuckCreate().color("yellow").height(8.0).material(material).sound(sound).wingsState(wingsState);
        createDuck(runner, duckling);
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        duckUpdate(runner, "white", 8.0, "${duckId}", material, sound, wingsState);
        DuckMessage message = new DuckMessage().message("Duck with id = " + "${duckId}" + " is updated");
        validatePayloads(runner, message, HttpStatus.OK);

    }

    @Test(description = "Обновление цвета и высоты уточки")
    @CitrusTest
    public void updateColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        double height = 5.0;
        String material = "rubber";
        WingsState wingsState = ACTIVE;
        DuckCreate duckling = new DuckCreate().color("yellow").height(height).material(material).sound("quack").wingsState(wingsState);
        createDuck(runner, duckling);
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        duckUpdate(runner, "white", height, "${duckId}", material, "quack!!!!!", wingsState);
        DuckMessage message = new DuckMessage().message("Duck with id = " + "${duckId}" + " is updated");
        validatePayloads(runner, message, HttpStatus.OK);
    }

}
