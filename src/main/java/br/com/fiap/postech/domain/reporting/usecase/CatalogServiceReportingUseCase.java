package br.com.fiap.postech.domain.reporting.usecase;

import br.com.fiap.postech.domain.reporting.model.CatalogServiceCalculatedAverageTime;

import java.util.List;

public interface CatalogServiceReportingUseCase {
    CatalogServiceCalculatedAverageTime calculateAverageTime(Long catalogServiceId);

    List<CatalogServiceCalculatedAverageTime> calculateAverageTime();
}
