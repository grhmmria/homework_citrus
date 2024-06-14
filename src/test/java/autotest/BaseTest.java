package autotest;

import autotest.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
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
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class BaseTest extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient yellowDuckService;

    @Autowired
    protected SingleConnectionDataSource testDatabase;

    protected void sendGet (TestCaseRunner runner, HttpClient URL, String path)  {
            runner.$(http().client(URL)
                    .send()
                    .get(path));
    }

    protected void sendPost (TestCaseRunner runner, HttpClient URL, Object body, String path) {
        runner.$(http().client(URL)
                .send()
                .post(path)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    protected void sendDelete (TestCaseRunner runner,HttpClient URL, String path, String queName, String queValue) {
        runner.$(http().client(URL)
                .send()
                .delete(path)
                .message()
                .queryParam(queName, queValue));
    }

    public void sendPut(TestCaseRunner runner, HttpClient URL, String path) {
        runner.$(http().client(URL)
                .send()
                .put(path));

    }

    @Step("Валидация ответа на запрос вводимой строкой")
    public void validateResponse(TestCaseRunner runner, String responseMessage, HttpStatus status) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract(fromBody().expression("$.id", "duckId"))
                .body(responseMessage));
    }

    @Step("Валидация ответа на запрос с payloads")
    public void validatePayloads(TestCaseRunner runner, Object body, HttpStatus status) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message().type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    @Step("Валидация ответа на запрос с помощью json из папки resources")
    public void validateResources(TestCaseRunner runner, String expectedPayload, HttpStatus status) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response(status)
                .message().type(MessageType.JSON)
                .body(new ClassPathResource(expectedPayload)));
    }

    public void validateDelete(TestCaseRunner runner, String id) {
        runner.$(query(testDatabase)
                .statement("SELECT COUNT(1) AS COUNT FROM DUCK WHERE ID=" + id)
                .validate("COUNT", "0"));

    }

    @Step("Валидация ответа на запрос проверкой базы")
    public void validateDatabase(TestCaseRunner runner, String id, String color, double height, String material, String sound, WingsState wingsState) {
        runner.$(query(testDatabase)
                .statement("SELECT * FROM DUCK WHERE ID=" + id)
                .validate("COLOR", color)
                .validate("HEIGHT", Double.toString(height))
                .validate("MATERIAL", material)
                .validate("SOUND", sound)
                .validate("WINGS_STATE", wingsState.toString()));
    }

}
