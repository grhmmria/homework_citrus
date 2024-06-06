package autotest.tests;

import autotest.clients.DuckActionClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckPropertiesTest extends DuckActionClient {
    // тоже оставила инициализацию строк так как используется не единожды:)

   @Test(description = "Свойства нечётной" )
    @CitrusTest
    public void propertiesOdd(@Optional @CitrusResource TestCaseRunner runner,  @Optional @CitrusResource TestContext context) {
       String color = "yellow";
       double height = 8.0;
       String material = "rubber";
       String sound = "quack";
       String wingsState = "ACTIVE";
       createDuck(runner, color, height, material, sound, wingsState);
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duck1"))
        );
        int intID = Integer.parseInt(context.getVariable("duck1"));
        if (intID%2!=0) {
            duckProperties(runner, "${duck1}");
        }
        else {
            createDuck(runner, color, height, material, sound, wingsState);
            runner.$(http().client(yellowDuckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duck2"))
            );
            duckProperties(runner, "${duck2}");
        }
        validateResponse(runner, "{" +
                "\"color\": \"" + color +
                "\",\n \"height\": \"" + height +
                "\",\n \"material\": \"" + material +
                "\",\n \"sound\": \"" + sound +
                "\", \n \"wingsState\": \"" + wingsState +
                "\" \n }", HttpStatus.OK);
    }

    @Test(description = "Свойства чётной" )
    @CitrusTest
    public void propertiesEven(@Optional @CitrusResource TestCaseRunner runner, @Optional @CitrusResource TestContext context) {
        String color = "yellow";
        double height = 8.0;
        String material = "wood";
        String sound = "quack";
        String wingsState = "ACTIVE";
        createDuck(runner, color, height, material, sound, wingsState);
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duck1"))
        );

        int intID = Integer.parseInt(context.getVariable("duck1"));
        if (intID%2==0) {
            duckProperties(runner, "${duck1}");
        }
        else {
            createDuck(runner, color, height, material, sound, wingsState);
            runner.$(http().client(yellowDuckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duck2"))
            );
            duckProperties(runner, "${duck2}");
        }
        validateResponse(runner, "{" +
                "\"color\": \"" + color +
                "\",\n \"height\": \"" + height +
                "\",\n \"material\": \"" + material +
                "\",\n \"sound\": \"" + sound +
                "\", \n \"wingsState\": \"" + wingsState +
                "\" \n }", HttpStatus.OK);
    }


}
