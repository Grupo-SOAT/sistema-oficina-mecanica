package br.com.fiap.postech.it.cucumber.context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.test.web.servlet.MockMvc;

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
    private Integer pageSize;
    private String cursor;
    private String filterName;
    private String filterValue;
    private String requestBody;
    private String lastResponseContentType;
    private String lastResponseContentDisposition;
    private Long catalogServiceId;
    private MockMvc mockMvc;

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
        this.pageSize = null;
        this.cursor = null;
        this.filterName = null;
        this.filterValue = null;
        this.requestBody = null;
        this.lastResponseContentType = null;
        this.lastResponseContentDisposition = null;
        this.catalogServiceId = null;
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
