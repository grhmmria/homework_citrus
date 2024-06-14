package autotest.tests;

import autotest.clients.DuckActionClient;
import autotest.payloads.DuckProperties;
import autotest.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.CitrusParameters;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.springframework.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static autotest.payloads.WingsState.*;
import static autotest.payloads.WingsState.FIXED;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

// здесь оставила инициализацию через строки так как вызывается ещё валидатор

@Epic("Тесты duck-controller")
@Feature("Эндпоинт /api/duck/create")
public class DuckCreateTest extends DuckActionClient {

    @Test(description = "Создание утки, rubber")
    @CitrusTest
    public void createRubber(@Optional @CitrusResource TestCaseRunner runner) {

        String color = "yellow";
        double height = 8.0;
        String material = "rubber";
        String sound = "quack";
        WingsState wingsState = ACTIVE;

        DuckProperties duckling = new DuckProperties().color(color).height(height).material(material).sound(sound).wingsState(wingsState);
        createDuck(runner, duckling);
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );

        runner.$(doFinally().actions(context -> dataBaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        validateDatabase(runner, "${duckId}", color, height, material, sound, wingsState);
    }

    @Test(description = "Создание утки, wood")
    @CitrusTest
    public void createWood(@Optional @CitrusResource TestCaseRunner runner) {

        String color = "yellow";
        double height = 5.0;
        String material = "wood";
        String sound = "quack";
        WingsState wingsState = ACTIVE;

        DuckProperties duckling = new DuckProperties().color(color).height(height).material(material).sound(sound).wingsState(wingsState);
        createDuck(runner, duckling);
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );

        runner.$(doFinally().actions(context -> dataBaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));
        validateDatabase(runner, "${duckId}", color, height, material, sound, wingsState);
    }

    @Test(description = "Создание пяти уток параметризованным тестом", dataProvider = "ducklings")

    @CitrusTest
    @CitrusParameters({"payload", "response", "runner"})
    public void createDucklings(Object payload, String response, @Optional @CitrusResource TestCaseRunner runner ) {
        createDuck(runner, payload);
        validateResources(runner, response, HttpStatus.OK);

    }

    DuckProperties albert = new DuckProperties().color("yellow").height(5.0).material("rubber").sound("quack").wingsState(ACTIVE);
    DuckProperties brown = new DuckProperties().color("white").height(8.0).material("steel").sound("kryaak").wingsState(FIXED);
    DuckProperties columbus = new DuckProperties().color("purple").height(11.0).material("wood").sound("heehaw").wingsState(UNDEFINED);
    DuckProperties david = new DuckProperties().color("aquamarine").height(16.0).material("concrete").sound("squeak").wingsState(ACTIVE);
    DuckProperties evlampiy = new DuckProperties().color("baige").height(19.0).material("ceramic").sound("kukareeqqu").wingsState(FIXED);

    @DataProvider(name = "ducklings")
    public Object[][] DuckProvider() {
        return new Object[][]{
                {albert, "duckCreateTest/duckCreateRubber.json", null},
                {brown, "duckCreateTest/duckCreateSteel.json", null},
                {columbus, "duckCreateTest/duckCreateWood.json", null},
                {david, "duckCreateTest/duckCreateConcrete.json", null},
                {evlampiy, "duckCreateTest/duckCreateCeramic.json", null}
        };

    }

}