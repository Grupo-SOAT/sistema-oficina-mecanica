package br.com.fiap.postech.port.reporting.catalogservice;

import br.com.fiap.postech.domain.reporting.model.CatalogServiceCalculatedAverageTime;

import java.util.List;

public interface CatalogServiceReportingPort {
    byte[] writePDF(CatalogServiceCalculatedAverageTime input);

    String writeCSV(List<CatalogServiceCalculatedAverageTime> items);
}
