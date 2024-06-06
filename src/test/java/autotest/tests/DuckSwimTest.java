package autotest.tests;

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

public class DuckSwimTest extends TestNGCitrusSpringSupport {

    @Test(description = "Плавание, существующий id")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "yellow";
        double height = 8.0;
        String material = "rubber";
        String sound = "quack";
        String wingsState = "ACTIVE";
        createDuck(runner, color, height, material, sound, wingsState);
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        duckSwim(runner, "${duckId}");
        validateOK(runner, "{" + "\"message\": \"I’m swimming\"}");
    }

    // так как случая для несуществующего айди не прописано в документации оставила фактический
    @Test(description = "Плавание, несуществующий id")
    @CitrusTest
    public void unsuccessfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "yellow";
        double height = 8.0;
        String material = "rubber";
        String sound = "quack";
        String wingsState = "ACTIVE";
        createDuck(runner, color, height, material, sound, wingsState);
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        deleteDuck(runner, "${duckId}");
        duckSwim(runner, "${duckId}");
        validateNF(runner, "{" + "\"message\": \"Paws are not found ((((\" }");
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

    public void deleteDuck(TestCaseRunner runner, String id) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .delete()
                .message()
                .queryParam("id", id));
    }

    public void duckSwim(TestCaseRunner runner, String id) { //плыть
        runner.$(http().client("http://localhost:2222")
                .send()
                .get("api/duck/action/swim")
                .queryParam("id", id));
    }

    public void validateOK(TestCaseRunner runner, String responseMessage) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage));
    }

    public void validateNF(TestCaseRunner runner, String responseMessage) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.NOT_FOUND)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage));
    }


}
