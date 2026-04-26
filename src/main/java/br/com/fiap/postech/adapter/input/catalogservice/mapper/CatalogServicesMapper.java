package br.com.fiap.postech.adapter.input.catalogservice.mapper;

import br.com.fiap.postech.adapter.input.api.model.*;
import br.com.fiap.postech.adapter.output.catalogService.persistence.entity.CatalogServicesEntity;
import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;
import br.com.fiap.postech.domain.catalogServices.model.CatalogServices;
import org.jspecify.annotations.NonNull;

public class CatalogServicesMapper {
    public static CatalogServices fromApiRequest(@NonNull CatalogServiceRequest request) {
        return CatalogServicesEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .basePrice(request.getBasePrice())
                .build();
    }

    public static CatalogServices fromApiData(@NonNull CatalogServiceData data) {
        return CatalogServicesEntity.builder()
                .catalogServiceId(data.getId())
                .name(data.getName())
                .description(data.getDescription())
                .basePrice(data.getBasePrice())
                .build();
    }

    public static CatalogServiceData toApiData(@NonNull CatalogServices catalogServices) {
        return new CatalogServiceData()
                .id(catalogServices.getCatalogServiceId())
                .name(catalogServices.getName())
                .description(catalogServices.getDescription())
                .basePrice(catalogServices.getBasePrice());
    }

    public static PaginatedCatalogServiceResponse toPaginatedResponse(@NonNull ScrollPage<CatalogServices> page) {
        final var result = new PaginatedCatalogServiceResponse()
                .pageSize(page.pageSize())
                .cursor(page.cursor())
                .isLast(page.isLast());

        page.data().forEach(item -> result.addDataItem(CatalogServicesMapper.toApiData(item)));

        return result;
    }
}
