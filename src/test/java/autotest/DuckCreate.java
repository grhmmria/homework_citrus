package autotest;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.dsl.MessageSupport.MessageHeaderSupport.fromHeaders;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckCreate extends TestNGCitrusSpringSupport {
    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .post("api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE) // !!!!!!!!
                .body("{" +
                        "\"color\": \"" + color +
                        "\",\n \"height\": \"" + height +
                        "\",\n \"material\": \"" + material +
                        "\",\n \"sound\": \"" + sound +
                        "\", \n \"wingsState\": \"" + wingsState +
                        "\" \n }"));
    }

    /*@Test(description = "Создание утки, rubber")
    @CitrusTest
    public void createRubber(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 5.0, "rubber", "quack", "ACTIVE");
        validateResponse(runner, "{" +
                "\"id\": " + "${duckId}" +
                "\"color\": \"" + "yellow" +
                "\",\n \"height\": \"" + "5.0" +
                "\",\n \"material\": \"" + "rubber" +
                "\",\n \"sound\": \"" + "quack" +
                "\", \n \"wingsState\": \"" + "ACTIVE" +
                "\" \n }");
    }*/

    @Test(description = "Создание утки, wood")
    @CitrusTest
    public void createWood(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.0, "wood", "quack", "ACTIVE");
        /*runner.$(http().client("http://localhost:2222")
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );*/
        validateResponse(runner, "{" +
                "\"id\": " + "66" +
                "\"color\": \"" + "yellow" +
                "\",\n \"height\": \"" + "8.0" +
                "\",\n \"material\": \"" + "wood" +
                "\",\n \"sound\": \"" + "quack" +
                "\", \n \"wingsState\": \"" + "ACTIVE" +
                "\" \n }");
    }

    public void validateResponse(TestCaseRunner runner, String responseMessage) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response()
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage));
    }

}
