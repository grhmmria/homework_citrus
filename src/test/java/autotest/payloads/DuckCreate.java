package autotest.payloads;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(fluent = true)

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DuckCreate {
    @JsonProperty
    private String color;

    @JsonProperty
    private double height;

    @JsonProperty
    private String material;

    @JsonProperty
    private String sound;

    @JsonProperty
    private WingsState wingsState;
}
