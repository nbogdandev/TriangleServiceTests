package common.utils.types.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"url", "authHeaderName", "authHeaderValue"})
public class Profile implements Serializable {
    @JsonProperty("url")
    public String url = null;
    @JsonProperty("authHeaderName")
    public String authHeaderName = null;
    @JsonProperty("authHeaderValue")
    public String authHeaderValue = null;
}
