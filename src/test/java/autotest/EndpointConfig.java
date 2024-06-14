package autotest;

import com.consol.citrus.http.client.HttpClient;

import com.consol.citrus.http.client.HttpClientBuilder;

import com.consol.citrus.validation.json.JsonTextMessageValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class EndpointConfig {
    @Bean("yellowDuckService")
    public HttpClient yellowDuckService() {
        return new HttpClientBuilder()
                .requestUrl("http://localhost:2222")
                .build();
    }
@Bean("testDatabase")
    public SingleConnectionDataSource database() {
    SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
    dataSource.setDriverClassName("org.h2.Driver");
    dataSource.setUrl("jdbc:h2:tcp://localhost:9092/mem:ducks");
    dataSource.setUsername("dev");
    dataSource.setPassword("dev");

    return dataSource;
}

}