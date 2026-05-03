package br.com.fiap.postech.it.cucumber.steps;

import io.cucumber.java.pt.E;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReportingStepDefinitions extends BaseStepDefinition {
    private static final Pattern ISO_LOCAL_DATE_TIME_IN_FILENAME = Pattern.compile(
            "(\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}-\\d{2})"
    );

    @E("o content-type da resposta deve ser {string}")
    public void shouldHaveExpectedContentType(String expectedContentType) {
        String actualContentType = context.getLastResponseContentType();

        assertNotNull(actualContentType, "Content-Type ausente na resposta");
        assertTrue(actualContentType.toLowerCase().contains(expectedContentType.toLowerCase()),
                "Content-Type inesperado. Esperado conter: " + expectedContentType + " | Atual: " + actualContentType);
    }

    @E("o body da resposta não deve ser vazio")
    public void shouldHaveNonEmptyBody() {
        String body = context.getLastResponseBodyAsString();

        assertNotNull(body, "Body da resposta é nulo");
        assertFalse(body.isBlank(), "Body da resposta está vazio");
    }

    @E("o nome do arquivo deve indicar exportação recente")
    public void shouldMatchRecentReportFileName() {
        String contentDisposition = context.getLastResponseContentDisposition();
        assertNotNull(contentDisposition, "Header Content-Disposition ausente na resposta");

        var matcher = ISO_LOCAL_DATE_TIME_IN_FILENAME.matcher(contentDisposition);
        assertTrue(matcher.find(), "Nome do arquivo sem timestamp ISO. Header: " + contentDisposition);

        LocalDateTime exportedAt = LocalDateTime.parse(
                matcher.group(1),
                DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
        );
        LocalDateTime now = LocalDateTime.now();

        assertFalse(
                exportedAt.isBefore(now.minusMinutes(10)),
                "Timestamp do arquivo é mais antigo que 10 minutos: " + exportedAt
        );
        assertFalse(
                exportedAt.isAfter(now.plusSeconds(5)),
                "Timestamp do arquivo está no futuro: " + exportedAt
        );
    }
}
