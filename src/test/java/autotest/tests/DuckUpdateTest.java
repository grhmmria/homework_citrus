package autotest.tests;

import autotest.clients.DuckActionClient;
import autotest.payloads.DuckBody;
import autotest.payloads.DuckMessage;
import autotest.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static autotest.payloads.WingsState.ACTIVE;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@Epic("Тесты duck-controller")
@Feature("Эндпоинт /api/duck/update")
public class DuckUpdateTest extends DuckActionClient {

    @Test(description = "Обновление цвета и высоты уточки")
    @CitrusTest
    public void updateColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {

        String material = "rubber";
        String sound = "quack";

        WingsState wingsState = ACTIVE;

        String id = databaseCreate(runner, "yellow", 8.0, material, sound, wingsState);
        duckUpdate(runner, "white", 8.0, id, material, sound, wingsState);

        DuckMessage message = new DuckMessage().message("Duck with id = " + id + " is updated");

        validateDatabase(runner, id, "white", 8.0, material, sound, wingsState);
        validatePayloads(runner, message, HttpStatus.OK);

    }

    @Test(description = "Обновление цвета и высоты уточки")
    @CitrusTest
    public void updateColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {

        double height = 5.0;
        String material = "rubber";

        WingsState wingsState = ACTIVE;

        String id = databaseCreate(runner, "yellow", height, material, "quack", wingsState);
        duckUpdate(runner, "white", height, "${duckId}", material, "quack!!!!!", wingsState);

        validateDatabase(runner, id, "white", height, material, "quack!!!!!", wingsState);

        DuckMessage message = new DuckMessage().message("Duck with id = " + "${duckId}" + " is updated");
        validatePayloads(runner, message, HttpStatus.OK);
    }

}
