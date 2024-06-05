package autotest;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckUpdateColorSoundTest extends TestNGCitrusSpringSupport {

    @Test(description = "Обновление цвета и высоты уточки")
    @CitrusTest
    public void updateColorAndSound(@Optional @CitrusResource TestCaseRunner runner, @Optional @CitrusResource TestContext context) {
        double height = 5.0;
        String material = "rubber";
        String wingsState = "ACTIVE";
        createDuck(runner, "yellow", height, material, "quack", wingsState);
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        duckUpdate(runner, "white", height, Integer.parseInt(context.getVariable("duckId")), material, "quack!!!!!",wingsState);
        validateResponse(runner, "{ \"message\": \"Duck with id = " + "${duckId}" + " is updated\" }");
    }

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .post("api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{" +
                        "\"color\": \"" + color +
                        "\",\n \"height\": \"" + height +
                        "\",\n \"material\": \"" + material +
                        "\",\n \"sound\": \"" + sound +
                        "\", \n \"wingsState\": \"" + wingsState +
                        "\" \n }"));
    }

    public void duckUpdate(TestCaseRunner runner, String color, double height, int id, String material, String sound, String wingsState) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .put("api/duck/update")
                .queryParam("color", color)
                .queryParam("height", Double.toString(height))
                .queryParam("id", Integer.toString(id))
                .queryParam("material", material)
                .queryParam("sound", sound)
                .queryParam("wingsState", wingsState));

    }

    public void validateResponse(TestCaseRunner runner, String responseMessage) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage));
    }
}
