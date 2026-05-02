package br.com.fiap.postech.domain.catalogServices.model;

import br.com.fiap.postech.adapter.output.catalogService.persistence.entity.NeededSupplyEntity;

import java.math.BigDecimal;
import java.util.List;

public interface CatalogServices {
    Long getCatalogServiceId();

    void setCatalogServiceId(Long catalogServiceId);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    BigDecimal getBasePrice();

    void setBasePrice(BigDecimal basePrice);

    List<NeededSupplyEntity> getSupplies();

    void setSupplies(List<NeededSupplyEntity> neededSupplies);
}
