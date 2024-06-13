package autotest.tests;

import autotest.clients.DuckActionClient;
import autotest.payloads.DuckMessage;
import autotest.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static autotest.payloads.WingsState.ACTIVE;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты duck-controller")
@Feature("Эндпоинт /api/duck/update")
public class DuckUpdateTest extends DuckActionClient {

    @Test(description = "Обновление цвета и высоты уточки")
    @CitrusTest
    public void updateColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {

        String material = "rubber";
        String sound = "quack";

        WingsState wingsState = ACTIVE;

        String duckId = databaseCreate(runner, "yellow", 8.0, material, sound, wingsState);
        runner.$(doFinally().actions(context -> databaseUpdate(runner, "DELETE FROM DUCK WHERE ID="+duckId)));

        duckUpdate(runner, "white", 5.0, duckId, material, sound, wingsState);
        DuckMessage message = new DuckMessage().message("Duck with id = " + duckId + " is updated");

        validateDatabase(runner, duckId, "white", 5.0, material, sound, wingsState);
        validatePayloads(runner, message, HttpStatus.OK);

    }

    @Test(description = "Обновление цвета и звука уточки")
    @CitrusTest
    public void updateColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {

        double height = 5.0;
        String material = "rubber";

        WingsState wingsState = ACTIVE;

        String duckId = databaseCreate(runner, "yellow", height, material, "quack", wingsState);
        runner.$(doFinally().actions(context -> databaseUpdate(runner, "DELETE FROM DUCK WHERE ID="+duckId)));

        duckUpdate(runner, "white", height, duckId, material, "quack!!!!!", wingsState);
        validateDatabase(runner, duckId, "white", height, material, "quack!!!!!", wingsState);

        DuckMessage message = new DuckMessage().message("Duck with id = " + duckId + " is updated");
        validatePayloads(runner, message, HttpStatus.OK);
    }

}