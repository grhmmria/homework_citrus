package autotest.tests;

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

public class DuckQuackTest extends TestNGCitrusSpringSupport {

    @Test(description = "Кряканье с существующим нечётным идентификатором")
    @CitrusTest
    public void oddQuack(@Optional @CitrusResource TestCaseRunner runner,@Optional @CitrusResource TestContext context) {
        String color = "yellow";
        double height = 5.0;
        String material = "rubber";
        String sound = "quack";
        String wingsState = "ACTIVE";
        createDuck(runner, color, height, material, sound, wingsState);
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id", "duck1"))
        );
        int intID = Integer.parseInt(context.getVariable("duck1"));
        if (intID%2!=0) {
            duckQuack(runner, "${duck1}");
        }
        else {
            createDuck(runner, color, height, material, sound, wingsState);
            runner.$(http().client("http://localhost:2222")
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duck2"))
            );
            duckQuack(runner, "${duck2}");
        }
        validateResponse(runner, "{\n" + "\"sound\":\"quack-quack, quack-quack, quack-quack, quack-quack, quack-quack\"\n" + "}");
    }

    @Test(description = "Кряканье с существующим чётным идентификатором")
    @CitrusTest
    public void evenQuack(@Optional @CitrusResource TestCaseRunner runner,@Optional @CitrusResource TestContext context) {
        String color = "yellow";
        double height = 5.0;
        String material = "rubber";
        String sound = "quack";
        String wingsState = "ACTIVE";
        createDuck(runner, color, height, material, sound, wingsState);
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response()
                .message()
                .extract(fromBody().expression("$.id","duck1" ))
        );
        int intID = Integer.parseInt(context.getVariable("duck1"));
        if (intID%2==0) {
            duckQuack(runner, "${duck1}");
        }
        else {
            createDuck(runner, color, height, material, sound, wingsState);
            runner.$(http().client("http://localhost:2222")
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "duck2"))
            );
            duckQuack(runner, "${duck2}");
        }
        validateResponse(runner, "{\n" + "\"sound\":\"quack-quack, quack-quack, quack-quack, quack-quack, quack-quack\"\n" + "}");
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

    public void duckQuack(TestCaseRunner runner, String id) { //крякать
        runner.$(http().client("http://localhost:2222")
                .send()
                .get("api/duck/action/quack")
                .queryParam("id", id)
                .queryParam("repetitionCount", "2")
                .queryParam("soundCount", "5"));
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
