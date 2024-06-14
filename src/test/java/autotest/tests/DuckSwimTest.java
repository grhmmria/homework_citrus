package autotest.tests;

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

import autotest.clients.DuckActionClient;

@Epic("Тесты duck-action-controller")
@Feature("Эндпоинт api/duck/action/swim")

public class DuckSwimTest extends DuckActionClient {


    @Test(description = "Плавание, существующий id")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {

        String duckId = databaseCreateDuck(runner, "yellow", 8.0, "rubber", "quack", ACTIVE);
        runner.$(doFinally().actions(context -> dataBaseUpdate(runner, "DELETE FROM DUCK WHERE ID="+duckId)));

        duckSwim(runner, duckId);

        validatePayloads(runner, "I'm swimming", HttpStatus.OK);
    }

    @Test(description = "Плавание, несуществующий id")
    @CitrusTest
    public void unsuccessfulSwim(@Optional @CitrusResource TestCaseRunner runner) {

        String duckId = databaseCreateDuck(runner, "yellow", 8.0, "rubber", "quack", ACTIVE);
        deleteDuck(runner, duckId);

        duckSwim(runner, duckId);
        validateResources(runner, "duckSwimTest/duckSwimNotExisting.json", HttpStatus.NOT_FOUND);
    }

}