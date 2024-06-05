package autotest;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckCreateTest extends TestNGCitrusSpringSupport {
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

    @Test(description = "Создание утки, rubber")
    @CitrusTest
    public void createRubber(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "yellow";
        double height = 5.0;
        String material = "rubber";
        String sound = "quack";
        String wingsState = "ACTIVE";
        createDuck(runner, color, height, material, sound, wingsState);
        validateResponse(runner, color, height, material, sound, wingsState);
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
        validateResponse(runner, color, height, material, sound, wingsState);
    }

    public void validateResponse(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract(fromBody().expression("$.id", "duckId"))
                .body("{\n" +
                        "\"id\": " + "${duckId}" + ", \n" +
                        "\"color\": \"" + color + "\", \n" +
                        "\"height\": " + height + ", \n" +
                        "\"material\": \"" + material + "\", \n" +
                        "\"sound\": \"" + sound + "\", \n" +
                        "\"wingsState\": \"" + wingsState + "\"\n}"));
    }

}
