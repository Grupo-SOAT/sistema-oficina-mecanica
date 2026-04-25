package br.com.fiap.postech.adapter.input.reporting.controller;

import br.com.fiap.postech.domain.reporting.usecase.CatalogServiceReportingUseCase;
import br.com.fiap.postech.port.api.ReportsApi;
import br.com.fiap.postech.port.reporting.catalogservice.CatalogServiceReportingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportsController implements ReportsApi {
    private final CatalogServiceReportingUseCase catalogServiceReportingUseCase;
    private final CatalogServiceReportingPort catalogServiceReportingPort;

    @Override
    public ResponseEntity<Resource> getServiceAverageTime(Long id) {
        final var data = catalogServiceReportingUseCase.calculateAverageTime(id);
        final var bytes = catalogServiceReportingPort.writePDF(data);
        final var resource = new ByteArrayResource(bytes);

        return ResponseEntity.ok(resource);
    }
}
