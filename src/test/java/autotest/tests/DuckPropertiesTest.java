package autotest.tests;

import autotest.clients.DuckActionClient;
import autotest.payloads.DuckCreate;
import autotest.payloads.DuckProperties;
import autotest.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static autotest.payloads.WingsState.ACTIVE;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckPropertiesTest extends DuckActionClient {
    // тоже оставила инициализацию строк так как используется не единожды:)

    @Test(description = "Свойства нечётной")
    @CitrusTest
    public void propertiesOdd(@Optional @CitrusResource TestCaseRunner runner, @Optional @CitrusResource TestContext context) {
        String color = "yellow";
        double height = 8.0;
        String material = "rubber";
        String sound = "quack";
        WingsState wingsState = ACTIVE;
        DuckCreate duckling = new DuckCreate().color(color).height(height).material(material).sound(sound).wingsState(wingsState);
        createDuck(runner, duckling);
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duck1"))
        );
        parityProperties(runner, context, "duck1", duckling, 1);
        DuckProperties duckProps = new DuckProperties().color(color).height(height).material(material).sound(sound).wingsState(wingsState);
        validatePayloads(runner, duckProps, HttpStatus.OK);
    }

    @Test(description = "Свойства чётной")
    @CitrusTest
    public void propertiesEven(@Optional @CitrusResource TestCaseRunner runner, @Optional @CitrusResource TestContext context) {
        String color = "yellow";
        double height = 8.0;
        String material = "wood";
        String sound = "quack";
        WingsState wingsState = ACTIVE;
        DuckCreate duckling = new DuckCreate().color(color).height(height).material(material).sound(sound).wingsState(wingsState);
        createDuck(runner, duckling);
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duck1"))
        );
        parityProperties(runner, context, "duck1", duckling, 0);
        validateResponse(runner, "{" +
                "\"color\": \"" + color +
                "\",\n \"height\": \"" + height +
                "\",\n \"material\": \"" + material +
                "\",\n \"sound\": \"" + sound +
                "\", \n \"wingsState\": \"" + wingsState +
                "\" \n }", HttpStatus.OK);

    }

}