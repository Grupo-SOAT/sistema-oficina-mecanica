package br.com.fiap.postech.port.reporting.catalogservice;

import br.com.fiap.postech.domain.reporting.model.CatalogServiceCalculatedAverageTime;

public interface CatalogServiceReportingPort {
    byte[] writePDF(CatalogServiceCalculatedAverageTime input);
}
