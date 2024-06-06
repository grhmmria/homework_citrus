package autotest.clients;

import autotest.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import com.consol.citrus.http.client.HttpClient;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes={EndpointConfig.class})
public class DuckActionClient extends TestNGCitrusSpringSupport {

    @Autowired
    protected HttpClient yellowDuckService;

    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
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

        for (int i=0; i<sc;i++) {
            for (int j = 0; j < rc-1; j++) {
                message.append(sound);
                message.append("-");
            }
            message.append(sound);
            if (i!=sc-1) {
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

    public void duckUpdate(TestCaseRunner runner, String color, double height, String id, String material, String sound, String wingsState) {
        runner.$(http().client(yellowDuckService)
                .send()
                .put("api/duck/update")
                .queryParam("color", color)
                .queryParam("height", Double.toString(height))
                .queryParam("id", id)
                .queryParam("material", material)
                .queryParam("sound", sound)
                .queryParam("wingsState", wingsState));

    }

    public void validateResponse(TestCaseRunner runner, String responseMessage, HttpStatus status) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage));
    }

    public void validateCreate(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
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
