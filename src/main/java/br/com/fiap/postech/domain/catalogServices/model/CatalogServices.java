package br.com.fiap.postech.domain.catalogServices.model;

import java.math.BigDecimal;

public interface CatalogServices {
    Long getCatalogServiceId();

    void setCatalogServiceId(Long catalogServiceId);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    BigDecimal getBasePrice();

    void setBasePrice(BigDecimal basePrice);
}
