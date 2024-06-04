package autotest;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import java.util.function.ToIntFunction;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
public class DuckProperties extends TestNGCitrusSpringSupport {
    @Test(description = "Свойства чётной" )
    @CitrusTest
    public void propertiesOdd(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.0, "wood", "quack", "ACTIVE");
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duckId"))
        );
        duckProperties(runner, "${duckId}");
        validateResponse(runner, "{}");
    }

    @Test(description = "Свойства нечётной" )
    @CitrusTest
    public void propertiesEven(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 8.0, "rubber", "quack", "ACTIVE");
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duck1"))
        );
        int intID = Integer.parseInt("${duck1}");
        if (intID%2!=0) {
            duckProperties(runner, "${duck1}");
        }
        else {
            createDuck(runner, "yellow", 8.0, "rubber", "quack", "ACTIVE");
            runner.$(http().client("http://localhost:2222")
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duck2"))
            );
            duckProperties(runner, "${duck2}");
        }
        validateResponse(runner, "{\n" + "\"message\":\" \"\n" + "}");
    }

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
    public void duckProperties(TestCaseRunner runner, String id) { //свойства
        runner.$(http().client("http://localhost:2222")
                .send()
                .get("api/duck/action/properties")
                .queryParam("id", id));
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
