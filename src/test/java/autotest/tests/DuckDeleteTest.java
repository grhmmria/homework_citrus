package autotest.tests;

import autotest.clients.DuckActionClient;
import autotest.payloads.DuckBody;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static autotest.payloads.WingsState.ACTIVE;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@Epic("Тесты duck-controller")
@Feature("Эндпоинт /api/duck/delete")
public class DuckDeleteTest extends DuckActionClient {

    @Test(description = "Удаление утки")
    @CitrusTest
    public void deleteExisting(@Optional @CitrusResource TestCaseRunner runner) {

        String id = databaseCreate(runner, "yellow", 5.0, "rubber", "quack", ACTIVE);
        deleteDuck(runner, id);

        validateResources(runner, "duckDeleteTest/duckDeleteExisting.json", HttpStatus.OK);

    }


}