package autotest.tests;

import autotest.clients.DuckActionClient;
import autotest.payloads.DuckBody;
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

@Epic("Тесты duck-action-controller")
@Feature("Эндпоинт /api/duck/action/properties")
public class DuckPropertiesTest extends DuckActionClient {

    @Test(description = "Свойства нечётной")
    @CitrusTest
    public void propertiesOdd(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "yellow";
        double height = 8.0;
        String material = "rubber";
        String sound = "quack";
        WingsState wingsState = ACTIVE;

        String duckId = createOdd(runner, color, height, material, sound, wingsState);
        duckProperties(runner, duckId);

        DuckBody duckProps = new DuckBody().color(color).height(height).material(material).sound(sound).wingsState(wingsState);
        validatePayloads(runner, duckProps, HttpStatus.OK);
    }

    @Test(description = "Свойства чётной")
    @CitrusTest
    public void propertiesEven(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "yellow";
        double height = 8.0;
        String material = "wood";
        String sound = "quack";
        WingsState wingsState = ACTIVE;

        String duckId = createOdd(runner, color, height, material, sound, wingsState);
        duckProperties(runner, duckId);

        DuckBody duckProps = new DuckBody().color(color).height(height).material(material).sound(sound).wingsState(wingsState);
        validatePayloads(runner, duckProps, HttpStatus.OK);
    }

}