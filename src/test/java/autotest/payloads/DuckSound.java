package autotest.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Accessors(fluent = true)

public class DuckSound {

    @JsonProperty
    private String sound;
}