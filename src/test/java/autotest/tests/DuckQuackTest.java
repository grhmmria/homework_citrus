package autotest.tests;

import autotest.clients.DuckActionClient;
import autotest.payloads.DuckSound;
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

@Epic("Тесты duck-action-controller")
@Feature("Эндпоинт api/duck/action/quack")
public class DuckQuackTest extends DuckActionClient {

    @Test(description = "Кряканье с существующим нечётным идентификатором")
    @CitrusTest
    public void oddQuack(@Optional @CitrusResource TestCaseRunner runner) {

        String sound = "quack";

        String repetitionCount = "2";
        String soundCount = "5";

        String duckId = createParity(runner, "yellow", 5.0, "rubber", sound, ACTIVE, 1);
        runner.$(doFinally().actions(context -> dataBaseUpdate(runner, "DELETE FROM DUCK WHERE ID="+duckId)));

        duckQuack(runner, duckId, repetitionCount, soundCount);

        DuckSound quacking = new DuckSound().sound(quackMessage(repetitionCount, soundCount, sound).toString());
        validatePayloads(runner, quacking, HttpStatus.OK);

    }

    @Test(description = "Кряканье с существующим чётным идентификатором")
    @CitrusTest
    public void evenQuack(@Optional @CitrusResource TestCaseRunner runner) {

        String sound = "quack";

        String repetitionCount = "2";
        String soundCount = "5";


        String duckId = createParity(runner, "yellow", 5.0, "rubber", sound, ACTIVE, 0);
        runner.$(doFinally().actions(context -> dataBaseUpdate(runner, "DELETE FROM DUCK WHERE ID="+duckId)));

        duckQuack(runner, duckId, repetitionCount, soundCount);

        DuckSound quacking = new DuckSound().sound(quackMessage(repetitionCount, soundCount, sound).toString());
        validatePayloads(runner, quacking, HttpStatus.OK);
    }


}