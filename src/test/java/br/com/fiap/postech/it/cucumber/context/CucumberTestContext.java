package br.com.fiap.postech.it.cucumber.context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CucumberTestContext {
    private static final CucumberTestContext INSTANCE = new CucumberTestContext();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private String currentUser;
    private String currentRole;
    private String currentPassword;
    private String currentApiKey;
    private String authToken;
    private int lastStatusCode;
    private String lastErrorMessage;
    private JsonNode lastResponseBody = NullNode.getInstance();

    public static synchronized CucumberTestContext getInstance() {
        return INSTANCE;
    }

    public void reset() {
        this.currentUser = null;
        this.currentRole = null;
        this.currentPassword = null;
        this.currentApiKey = null;
        this.authToken = null;
        this.lastStatusCode = 0;
        this.lastErrorMessage = null;
        this.lastResponseBody = NullNode.getInstance();
    }

    public void setLastResponseBody(String rawBody) {
        if (rawBody == null) {
            this.lastResponseBody = NullNode.getInstance();
            return;
        }

        try {
            this.lastResponseBody = OBJECT_MAPPER.readTree(rawBody);
        } catch (Exception ignored) {
            this.lastResponseBody = TextNode.valueOf(rawBody);
        }
    }

    public String getLastResponseBodyAsString() {
        if (lastResponseBody == null || lastResponseBody.isNull()) {
            return "";
        }

        if (lastResponseBody.isTextual()) {
            return lastResponseBody.asText();
        }

        return lastResponseBody.toString();
    }
}
