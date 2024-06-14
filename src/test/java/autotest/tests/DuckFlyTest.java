package autotest.tests;

import autotest.clients.DuckActionClient;
import autotest.payloads.DuckMessage;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static autotest.payloads.WingsState.*;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты duck-action-controller")
@Feature("Эндпоинт /api/duck/action/fly")
public class DuckFlyTest extends DuckActionClient {
    @Test(description = "Полёт, ACTIVE крылья")
    @CitrusTest
    public void flyActive(@Optional @CitrusResource TestCaseRunner runner) {

        String duckId = databaseCreateDuck(runner, "yellow", 8.0, "rubber", "quack", ACTIVE);
        runner.$(doFinally().actions(context -> dataBaseUpdate(runner, "DELETE FROM DUCK WHERE ID="+duckId)));

        duckFly(runner, duckId);

        validateResources(runner, "duckFlyTest/duckFlyActive.json", HttpStatus.OK);
    }

    @Test(description = "Полёт, FIXED крылья")
    @CitrusTest
    public void flyFixed(@Optional @CitrusResource TestCaseRunner runner) {

        String duckId = databaseCreateDuck(runner, "yellow", 8.0, "rubber", "quack", FIXED);
        runner.$(doFinally().actions(context -> dataBaseUpdate(runner, "DELETE FROM DUCK WHERE ID="+duckId)));

        duckFly(runner, duckId);

        validateResources(runner, "duckFlyTest/duckFlyFixed.json", HttpStatus.OK);
    }

    @Test(description = "Полёт, UNDEFINED крылья")
    @CitrusTest
    public void flyUndefined(@Optional @CitrusResource TestCaseRunner runner) {

        String duckId = databaseCreateDuck(runner, "yellow", 8.0, "rubber", "quack", UNDEFINED);
        runner.$(doFinally().actions(context -> dataBaseUpdate(runner, "DELETE FROM DUCK WHERE ID="+duckId)));

        duckFly(runner, duckId);

        DuckMessage message = new DuckMessage().message("Wings are not detected :(");
        validatePayloads(runner, message, HttpStatus.OK);
    }
}