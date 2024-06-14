package autotest.clients;

import autotest.BaseTest;
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

public class DuckActionClient extends BaseTest {

    @Step("Обновление базы с помощью запроса")
    public void databaseUpdate(TestCaseRunner runner, String sql) {
        runner.$(sql(testDatabase)
                .statement(sql));
    }

    @Step("Создание уточки с помощью запроса к базе данных")
    public String databaseCreate(TestCaseRunner runner, String color, double height, String material, String sound, WingsState wingsState) {
        Random rd = new Random();
        int id;
        do {
            id = rd.nextInt();
        } while (id <= 0);
        runner.variable("duckId", id);

        databaseUpdate(runner, "INSERT INTO DUCK (id, color, height, material, sound, wings_state) " +
                " VALUES (${duckId},'" + color + "'," + height + ",'" + material + "','" + sound + "','" + wingsState.toString() + "');");
        return Integer.toString(id);
    }

    @Step("Создание уточки с чётным/нечётным айди с помощью запроса к базе данных")
    public String createParity(TestCaseRunner runner, String color, double height, String material, String sound, WingsState wingsState, int remainder) {
        Random rd = new Random();
        int id;
        do {
            id = rd.nextInt();
        } while (id <= 0);
        if (id % 2 != remainder) {
            id += 1;
        }
        runner.variable("duckId", Integer.toString(id));
        databaseUpdate(runner, "INSERT INTO DUCK (id, color, height, material, sound, wings_state) " +
                " VALUES (${duckId},'" + color + "'," + height + ",'" + material + "','" + sound + "','" + wingsState.toString() + "');");
        return Integer.toString(id);

    }

    @Step("Эндпоинт для внесения уточки в базу")
    public void createDuck(TestCaseRunner runner, Object body) {
        sendPost(runner, yellowDuckService, body, "api/duck/create");
    }

    @Step("Эндпоинт для удаления уточки из базы")
    public void deleteDuck(TestCaseRunner runner, String id) {
        sendDelete(runner, yellowDuckService, "api/duck/delete", "id", id);
    }

    @Step("Эндпоинт для команды \"Лететь\" уточки")
    public void duckFly(TestCaseRunner runner, String id) {
        sendGet(runner, yellowDuckService, "api/duck/action/fly" + "?id=" + id);
    }

    @Step("Эндпоинт для команды \"Свойства\" уточки")
    public void duckProperties(TestCaseRunner runner, String id) {
        sendGet(runner, yellowDuckService, "api/duck/action/properties" + "?id=" + id);
    }

    @Step("Эндпоинт для команды \"Крякать\" уточки")
    public void duckQuack(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        sendGet(runner, yellowDuckService, "api/duck/action/quack" + "?id=" + id + "&repetitionCount=" + repetitionCount + "&soundCount=" + soundCount);
    }


    @Step("Выведение последовательности звуков издаваемой уточкой")
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

    @Step("Эндпоинт для команды \"Плыть\" уточки")
    public void duckSwim(TestCaseRunner runner, String id) {
        sendGet(runner, yellowDuckService, "api/duck/action/swim" + "?id=" + id);
    }

    @Step("Эндпоинт для команды \"Обновить\" уточку")
    public void duckUpdate(TestCaseRunner runner, String color, double height, String id, String material, String sound, WingsState wingsState) {
        sendPut(runner, yellowDuckService, "api/duck/update" + "?color=" + color + "&height=" + height + "&id=" + id + "&material=" + material + "&sound=" + sound + "&wings_state=" + wingsState);
    }

}