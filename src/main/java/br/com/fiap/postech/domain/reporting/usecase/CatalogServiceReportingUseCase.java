package br.com.fiap.postech.domain.reporting.usecase;

import br.com.fiap.postech.domain.reporting.model.CatalogServiceCalculatedAverageTime;

public interface CatalogServiceReportingUseCase {
    CatalogServiceCalculatedAverageTime calculateAverageTime(Long catalogServiceId);
}
