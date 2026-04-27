package br.com.fiap.postech.adapter.input.reporting.controller;

import br.com.fiap.postech.domain.reporting.usecase.CatalogServiceReportingUseCase;
import br.com.fiap.postech.port.api.ReportsApi;
import br.com.fiap.postech.port.reporting.catalogservice.CatalogServiceReportingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ReportsController implements ReportsApi {
    private final CatalogServiceReportingUseCase catalogServiceReportingUseCase;
    private final CatalogServiceReportingPort catalogServiceReportingPort;

    @Override
    public ResponseEntity<Resource> getServiceAverageTime(Long id) {
        final var data = catalogServiceReportingUseCase.calculateAverageTime(id);
        final var bytes = catalogServiceReportingPort.writePDF(data);
        final var contentDisposition = "attachment; filename=\"service-" +
                id +
                "-average-time" +
                LocalDateTime.now() +
                ".pdf\"";
        final var resource = new ByteArrayResource(bytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @Override
    public ResponseEntity<String> getAllServicesAverageTime() {
        final var data = catalogServiceReportingUseCase.calculateAverageTime();
        final var csv = catalogServiceReportingPort.writeCSV(data);
        final var contentDisposition = "attachment; filename=\"services-average-time_" +
                LocalDateTime.now() +
                ".csv\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.valueOf("text/csv"))
                .body(csv);
    }

    // TODO: implementar exception handler de NOT_FOUND

    // TODO: implementar exception handler de NO_CONTENT
}
