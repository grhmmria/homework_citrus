package autotest.clients;

import autotest.EndpointConfig;
import autotest.payloads.DuckCreate;
import autotest.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import com.consol.citrus.http.client.HttpClient;
import org.testng.annotations.Optional;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionClient extends TestNGCitrusSpringSupport {

    @Autowired
    protected HttpClient yellowDuckService;

    /* public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(http().client(yellowDuckService)
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
    } */

    public void createDuck(TestCaseRunner runner, Object body) {
        runner.$(http().client(yellowDuckService)
                .send()
                .post("api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    public void deleteDuck(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .delete("api/duck/delete")
                .message()
                .queryParam("id", id));
    }

    public void duckFly(TestCaseRunner runner, String id) { //лететь
        runner.$(http().client(yellowDuckService)
                .send()
                .get("api/duck/action/fly")
                .queryParam("id", id));
    }


    public void parityProperties(@Optional @CitrusResource TestCaseRunner runner, @Optional @CitrusResource TestContext context, String id1, DuckCreate duckling, int remainder) {
        int intID = Integer.parseInt(context.getVariable(id1));
        if (intID % 2 == remainder) {
            duckProperties(runner, "${" + id1 + "}");
        } else {
            createDuck(runner, duckling);
            runner.$(http().client(yellowDuckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "id2"))
            );
            duckProperties(runner, "${id2}");
        }
    }

    public void parityQuack(@Optional @CitrusResource TestCaseRunner runner, @Optional @CitrusResource TestContext context, String id1, String repetitionCount, String soundCount, DuckCreate duckling, int remainder) {
        int intID = Integer.parseInt(context.getVariable(id1));
        if (intID % 2 == remainder) {
            duckQuack(runner, "${" + id1 + "}", repetitionCount, soundCount);
        } else {
            createDuck(runner, duckling);
            runner.$(http().client(yellowDuckService)
                    .receive()
                    .response()
                    .message()
                    .extract(fromBody().expression("$.id", "id2"))
            );
            duckQuack(runner, "${id2}", repetitionCount, soundCount);
        }
    }

    public void duckProperties(TestCaseRunner runner, String id) { //свойства
        runner.$(http().client(yellowDuckService)
                .send()
                .get("api/duck/action/properties")
                .queryParam("id", id));
    }

    public void duckQuack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) { //крякать
        runner.$(http().client(yellowDuckService)
                .send()
                .get("api/duck/action/quack")
                .queryParam("id", id)
                .queryParam("repetitionCount", repetitionCount)
                .queryParam("soundCount", soundCount));
    }

    public static StringBuilder quackMessage(String repetitionCount, String soundCount, String sound) {

        StringBuilder message = new StringBuilder();

        int rc = Integer.valueOf(repetitionCount);
        int sc = Integer.valueOf(soundCount);

        if (rc < 0 || sc < 0) {
            message.append("Invalid parameter value");
            return message;
        }

        if ((rc == 0) || (sc == 0)) {
            return message;
        }

        for (int i = 0; i < sc; i++) {
            for (int j = 0; j < rc - 1; j++) {
                message.append(sound);
                message.append("-");
            }
            message.append(sound);
            if (i != sc - 1) {
                message.append(", ");
            }
        }
        return message;
    }

    public void duckSwim(TestCaseRunner runner, String id) { //плыть
        runner.$(http().client(yellowDuckService)
                .send()
                .get("api/duck/action/swim")
                .queryParam("id", id));
    }

    public void duckUpdate(TestCaseRunner runner, String color, double height, String id, String material, String sound, WingsState wingsState) {
        runner.$(http().client(yellowDuckService)
                .send()
                .put("api/duck/update")
                .queryParam("color", color)
                .queryParam("height", Double.toString(height))
                .queryParam("id", id)
                .queryParam("material", material)
                .queryParam("sound", sound)
                .queryParam("wingsState", wingsState.toString()));

    }

    public void validateResponse(TestCaseRunner runner, String responseMessage, HttpStatus status) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage));
    }

    public void validatePayloads(TestCaseRunner runner, Object body, HttpStatus status) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message().type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    public void validateResources(TestCaseRunner runner, String expectedPayload, HttpStatus status) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message().type(MessageType.JSON)
                .body(new ClassPathResource(expectedPayload)));
    }

    public void validateCreate(TestCaseRunner runner, String color, double height, String material, String sound, WingsState wingsState) {
        runner.$(http().client(yellowDuckService)
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