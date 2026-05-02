package br.com.fiap.postech.domain.catalogServices.model;

import br.com.fiap.postech.adapter.output.catalogService.persistence.entity.CatalogServicesEntity;
import br.com.fiap.postech.adapter.output.supply.persistence.entity.SupplyEntity;

public interface NeededSupply {
    Long getServicesSuppliesId();

    void setServicesSuppliesId(Long servicesSuppliesId);

    CatalogServices getCatalogServices();

    void setCatalogServices(CatalogServicesEntity catalogServices);

    SupplyEntity getSupply();

    void setSupply(SupplyEntity supplyId);

    Integer getSupplyAmount();

    void setSupplyAmount(Integer supplyAmount);
}
