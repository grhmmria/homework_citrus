package autotest.tests;

import autotest.clients.DuckActionClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

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
        String wingsState = "ACTIVE";
        String repetitionCount = "2";
        String soundCount = "5";

        createDuck(runner, color, height, material, sound, wingsState);

        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duck1"))
        );
        int intID = Integer.parseInt(context.getVariable("duck1"));
        if (intID % 2 != 0) {
            duckQuack(runner, "${duck1}", repetitionCount, soundCount);
        } else {
            createDuck(runner, color, height, material, sound, wingsState);
            runner.$(http().client(yellowDuckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duck2"))
            );
            duckQuack(runner, "${duck1}", repetitionCount, soundCount);
        }
        validateResponse(runner, "{\"sound\":\"" + quackMessage(repetitionCount, soundCount, sound) + "\"}", HttpStatus.OK);
    }

    @Test(description = "Кряканье с существующим чётным идентификатором")
    @CitrusTest
    public void evenQuack(@Optional @CitrusResource TestCaseRunner runner, @Optional @CitrusResource TestContext context) {
        String color = "yellow";
        double height = 5.0;
        String material = "rubber";
        String sound = "quack";
        String wingsState = "ACTIVE";
        String repetitionCount = "2";
        String soundCount = "5";

        createDuck(runner, color, height, material, sound, wingsState);

        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duck1"))
        );
        int intID = Integer.parseInt(context.getVariable("duck1"));
        if (intID % 2 == 0) {
            duckQuack(runner, "${duck1}", repetitionCount, soundCount);
        } else {
            createDuck(runner, color, height, material, sound, wingsState);
            runner.$(http().client(yellowDuckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duck2"))
            );
            duckQuack(runner, "${duck1}", repetitionCount, soundCount);
        }
        validateResponse(runner, "{\"sound\":\"" + quackMessage(repetitionCount, soundCount, sound) + "\"}", HttpStatus.OK);
    }

}