package autotest.tests;

import autotest.clients.DuckActionClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

// здесь оставила инициализацию через строки так как вызывается ещё валидатор
public class DuckCreateTest extends DuckActionClient {

    @Test(description = "Создание утки, rubber")
    @CitrusTest
    public void createRubber(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "yellow";
        double height = 8.0;
        String material = "rubber";
        String sound = "quack";
        String wingsState = "ACTIVE";
        createDuck(runner, color, height, material, sound, wingsState);
        validateCreate(runner, color, height, material, sound, wingsState);
    }

    @Test(description = "Создание утки, wood")
    @CitrusTest
    public void createWood(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "yellow";
        double height = 5.0;
        String material = "wood";
        String sound = "quack";
        String wingsState = "ACTIVE";
        createDuck(runner, color, height, material, sound, wingsState);
        validateCreate(runner, color, height, material, sound, wingsState);
    }

}