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

public class DuckUpdateTest extends DuckActionClient {

    @Test(description = "Обновление цвета и высоты уточки")
    @CitrusTest
    public void updateColorAndHeight(@Optional @CitrusResource TestCaseRunner runner){
        String material = "rubber";
        String sound = "quack";
        String wingsState = "ACTIVE";
        createDuck(runner, "yellow", 5.0, material, sound, wingsState);
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        duckUpdate(runner, "white", 8.0, "${duckId}", material, sound, wingsState);
        validateResponse(runner, "{\"message\": \"Duck with id = " + "${duckId}" + " is updated\"}", HttpStatus.OK);

    }

    @Test(description = "Обновление цвета и высоты уточки")
    @CitrusTest
    public void updateColorAndSound(@Optional @CitrusResource TestCaseRunner runner){
        double height = 5.0;
        String material = "rubber";
        String wingsState = "ACTIVE";
        createDuck(runner, "yellow", height, material, "quack", wingsState);
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        duckUpdate(runner, "white", height, "${duckId}", material, "quack!!!!!",wingsState);
        validateResponse(runner, "{\"message\": \"Duck with id = " + "${duckId}" + " is updated\"}", HttpStatus.OK);
    }

}
