package autotest.tests;

import autotest.clients.DuckActionClient;
import autotest.payloads.DuckBody;
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
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@Epic("Тесты duck-action-controller")
@Feature("Эндпоинт /api/duck/action/fly")
public class DuckFlyTest extends DuckActionClient {
    @Test(description = "Полёт, ACTIVE крылья")
    @CitrusTest
    public void flyActive(@Optional @CitrusResource TestCaseRunner runner) {

        String id = databaseCreate(runner, "yellow", 8.0, "rubber", "quack", ACTIVE);
        duckFly(runner, id);

        validateResources(runner, "duckFlyTest/duckFlyActive.json", HttpStatus.OK);
    }

    @Test(description = "Полёт, FIXED крылья")
    @CitrusTest
    public void flyFixed(@Optional @CitrusResource TestCaseRunner runner) {

        String id = databaseCreate(runner, "yellow", 8.0, "rubber", "quack", FIXED);
        duckFly(runner, id);

        validateResources(runner, "duckFlyTest/duckFlyFixed.json", HttpStatus.OK);
    }

    @Test(description = "Полёт, UNDEFINED крылья")
    @CitrusTest
    public void flyUndefined(@Optional @CitrusResource TestCaseRunner runner) {

        String id = databaseCreate(runner, "yellow", 8.0, "rubber", "quack", UNDEFINED);
        duckFly(runner, id);

        DuckMessage message = new DuckMessage().message("Wings are not detected :(");
        validatePayloads(runner, message, HttpStatus.OK);
    }
}