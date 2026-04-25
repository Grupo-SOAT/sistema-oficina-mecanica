package br.com.fiap.postech.adapter.output.reporting.helper;

import br.com.fiap.postech.domain.reporting.model.CatalogServiceCalculatedAverageTime;
import br.com.fiap.postech.port.reporting.catalogservice.CatalogServiceReportingPort;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.server.servlet.context.ServletWebServerApplicationContext;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Component
@AllArgsConstructor
public class PDFWriterAdapter implements CatalogServiceReportingPort {
    private static final String BASE_URL_PROP = "baseUrl";
    private static final String REPORT_GENERATED_AT_PROP = "reportGeneratedAt";
    private static final String LOCALHOST = "http://localhost";
    private static final int BUFFER_SIZE = 100000;
    private static final DateTimeFormatter REPORT_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private static final Map<String, String> TEMPLATE_BY_CLASS_NAME = Map.of(
            CatalogServiceCalculatedAverageTime.class.getSimpleName(), "catalog-service-average-time-report.html"
    );

    private final TemplateEngine templateEngine;
    private final ServletWebServerApplicationContext applicationContext;

    private String getBaseUrl() {
        return LOCALHOST + ':' + Objects.requireNonNull(applicationContext.getWebServer()).getPort();
    }

    private String formatHours(Double value) {
        return BigDecimal.valueOf(value)
                .setScale(1, RoundingMode.DOWN)
                .toPlainString()
                .concat("h");
    }

    private Map<String, Object> toReportVariables(CatalogServiceCalculatedAverageTime input) {
        final var variables = new LinkedHashMap<String, Object>();
        variables.put("id", input.id());
        variables.put("name", input.name());
        variables.put("totalCreated", input.totalCreated());
        variables.put("totalCompleted", input.totalCompleted());
        variables.put("averageTimeBetweenCreateAndComplete", formatHours(input.averageTimeBetweenCreateAndComplete()));
        variables.put("averageTimeBetweenStartAndComplete", formatHours(input.averageTimeBetweenStartAndComplete()));
        variables.put("averageTimeBetweenApproveAndComplete", formatHours(input.averageTimeBetweenApproveAndComplete()));
        variables.put("averageTimeAwaitingBudgetApproval", formatHours(input.averageTimeAwaitingBudgetApproval()));

        return variables;
    }

    private String generateHTML(Map<String, Object> variables, String template) {
        final var context = new Context();
        context.setVariables(variables);
        context.setVariable(BASE_URL_PROP, getBaseUrl());
        context.setVariable(REPORT_GENERATED_AT_PROP, LocalDateTime.now().format(REPORT_DATE_FORMATTER));

        return templateEngine.process(template, context);
    }

    private byte[] writePDF(Map<String, Object> variables, String template) {
        final var html = generateHTML(variables, template);
        final var converterProperties = new ConverterProperties();
        converterProperties.setBaseUri(getBaseUrl());

        final var target = new ByteArrayOutputStream(BUFFER_SIZE);
        HtmlConverter.convertToPdf(html, new BufferedOutputStream(target), converterProperties);

        return target.toByteArray();
    }

    @Override
    public byte[] writePDF(CatalogServiceCalculatedAverageTime input) {
        return this.writePDF(
                toReportVariables(input),
                TEMPLATE_BY_CLASS_NAME.get(input.getClass().getSimpleName())
        );
    }
}
