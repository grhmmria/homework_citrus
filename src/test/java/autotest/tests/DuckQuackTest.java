package autotest.tests;

import autotest.clients.DuckActionClient;
import autotest.payloads.DuckCreate;
import autotest.payloads.DuckSound;
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

public class DuckQuackTest extends DuckActionClient {

    @Test(description = "Кряканье с существующим нечётным идентификатором")
    @CitrusTest
    public void oddQuack(@Optional @CitrusResource TestCaseRunner runner, @Optional @CitrusResource TestContext context) {
        String color = "yellow";
        double height = 5.0;
        String material = "rubber";
        String sound = "quack";
        WingsState wingsState = ACTIVE;
        String repetitionCount = "2";
        String soundCount = "5";

        DuckCreate duckling = new DuckCreate().color(color).height(height).material(material).sound(sound).wingsState(wingsState);
        createDuck(runner, duckling);

        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duck1"))
        );
        parityQuack(runner, context, "duck1", repetitionCount, soundCount, duckling, 1);

        DuckSound quacking = new DuckSound().sound(quackMessage(repetitionCount, soundCount, sound).toString());
        validatePayloads(runner, quacking, HttpStatus.OK);
    }

    @Test(description = "Кряканье с существующим чётным идентификатором")
    @CitrusTest
    public void evenQuack(@Optional @CitrusResource TestCaseRunner runner, @Optional @CitrusResource TestContext context) {
        String color = "yellow";
        double height = 5.0;
        String material = "rubber";
        String sound = "quack";
        WingsState wingsState = ACTIVE;
        String repetitionCount = "2";
        String soundCount = "5";

        DuckCreate duckling = new DuckCreate().color(color).height(height).material(material).sound(sound).wingsState(wingsState);
        createDuck(runner, duckling);

        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duck1")));
        parityQuack(runner, context, "duck1", repetitionCount, soundCount, duckling, 0);

        DuckSound quacking = new DuckSound().sound(quackMessage(repetitionCount, soundCount, sound).toString());
        validatePayloads(runner, quacking, HttpStatus.OK);
    }


}