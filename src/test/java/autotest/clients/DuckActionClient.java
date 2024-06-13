package autotest.clients;

import autotest.EndpointConfig;
import autotest.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;
import com.consol.citrus.http.client.HttpClient;

import java.util.Random;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionClient extends TestNGCitrusSpringSupport {

    @Autowired
    protected HttpClient yellowDuckService;

    @Autowired
    protected SingleConnectionDataSource testDatabase;

    public void databaseUpdate(TestCaseRunner runner, String sql) {
        runner.$(sql(testDatabase)
                .statement(sql));
    }

    public String databaseCreate (TestCaseRunner runner, String color, double height, String material, String sound, WingsState wingsState) {
        Random rd = new Random();
        int id;
        do {
            id = rd.nextInt();
        } while (id<=0);
        runner.variable("duckId", id);
        runner.$(doFinally().actions(context -> databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));

        databaseUpdate(runner, "INSERT INTO DUCK (id, color, height, material, sound, wings_state) " +
                " VALUES (${duckId},'"+color +"',"+height+",'"+material+"','"+sound+"','"+wingsState.toString()+"');");
    return Integer.toString(id);
    }

    public String createOdd (TestCaseRunner runner, String color, double height, String material, String sound, WingsState wingsState) {
        Random rd = new Random();
        int id;
        do {
            id = rd.nextInt();
        } while (id<=0);
        if (id%2==0) {
            id += 1;
        }
        runner.variable("duckId", Integer.toString(id));
        runner.$(doFinally().actions(context -> databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));

        databaseUpdate(runner, "INSERT INTO DUCK (id, color, height, material, sound, wings_state) " +
                " VALUES (${duckId},'"+color +"',"+height+",'"+material+"','"+sound+"','"+wingsState.toString()+"');");
     return Integer.toString(id);

    }

    public String createEven (TestCaseRunner runner, String color, double height, String material, String sound, WingsState wingsState) {
        Random rd = new Random();
        int id;
        do {
            id = rd.nextInt();
        } while (id<=0);
        if (id%2!=0) {
            id += 1;
        }
        runner.variable("duckId", Integer.toString(id));
        runner.$(doFinally().actions(context -> databaseUpdate(runner, "DELETE FROM DUCK WHERE ID=${duckId}")));

        databaseUpdate(runner, "INSERT INTO DUCK (id, color, height, material, sound, wings_state) " +
                " VALUES (${duckId},'"+color +"',"+height+",'"+material+"','"+sound+"','"+wingsState.toString()+"');");
        return Integer.toString(id);
    }

    @Step("Ёндпоинт дл€ внесени€ уточки в базу")
    public void createDuck(TestCaseRunner runner, Object body) {
        runner.$(http().client(yellowDuckService)
                .send()
                .post("api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    @Step("Ёндпоинт дл€ удалени€ уточки из базы")
    public void deleteDuck(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .delete("api/duck/delete")
                .message()
                .queryParam("id", id));
    }

    @Step("Ёндпоинт дл€ команды \"Ћететь\" уточки")
    public void duckFly(TestCaseRunner runner, String id) { //лететь
        runner.$(http().client(yellowDuckService)
                .send()
                .get("api/duck/action/fly")
                .queryParam("id", id));
    }

    @Step("Ёндпоинт дл€ команды \"—войства\" уточки")
    public void duckProperties(TestCaseRunner runner, String id) { //свойства
        runner.$(http().client(yellowDuckService)
                .send()
                .get("api/duck/action/properties")
                .queryParam("id", id));
    }
    @Step("Ёндпоинт дл€ команды \" р€кать\" уточки")
    public void duckQuack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) { //кр€кать
        runner.$(http().client(yellowDuckService)
                .send()
                .get("api/duck/action/quack")
                .queryParam("id", id)
                .queryParam("repetitionCount", repetitionCount)
                .queryParam("soundCount", soundCount));
    }
    @Step("¬ыведение последовательности звуков издаваемой уточкой")
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

    @Step("Ёндпоинт дл€ команды \"ѕлыть\" уточки")
    public void duckSwim(TestCaseRunner runner, String id) { //плыть
        runner.$(http().client(yellowDuckService)
                .send()
                .get("api/duck/action/swim")
                .queryParam("id", id));
    }

    @Step("Ёндпоинт дл€ команды \"ќбновить\" уточку")
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

    @Step("¬алидаци€ ответа на запрос вводимой строкой")
    public void validateResponse(TestCaseRunner runner, String responseMessage, HttpStatus status) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract(fromBody().expression("$.id", "duckId"))
                .body(responseMessage));
    }

    @Step("¬алидаци€ ответа на запрос с payloads")
    public void validatePayloads(TestCaseRunner runner, Object body, HttpStatus status) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message().type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    @Step("¬алидаци€ ответа на запрос с помощью json из папки properties")
    public void validateResources(TestCaseRunner runner01, String expectedPayload, HttpStatus status) {
        runner01.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message().type(MessageType.JSON)
                .body(new ClassPathResource(expectedPayload)));
    }


    @Step("¬алидаци€ ответа на запрос проверкой базы")
    public void validateDatabase(TestCaseRunner runner, String id, String color, double height, String material, String sound, WingsState wingsState) {
        runner.$(query(testDatabase)
                .statement("SELECT * FROM DUCK WHERE ID="+id)
                .validate("COLOR",color)
                .validate("HEIGHT",Double.toString(height))
                .validate("MATERIAL",material)
                .validate("SOUND",sound)
                .validate("WINGS_STATE", wingsState.toString()));
    }
}