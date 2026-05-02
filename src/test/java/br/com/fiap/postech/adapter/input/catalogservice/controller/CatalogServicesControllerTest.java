package br.com.fiap.postech.adapter.input.catalogservice.controller;

import br.com.fiap.postech.adapter.input.api.model.PaginatedCatalogServiceResponse;
import br.com.fiap.postech.adapter.input.api.model.PaginatedSupplyResponse;
import br.com.fiap.postech.adapter.input.api.model.SupplyData;
import br.com.fiap.postech.adapter.output.catalogService.persistence.entity.CatalogServicesEntity;
import br.com.fiap.postech.adapter.output.catalogService.persistence.entity.NeededSupplyEntity;
import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;
import br.com.fiap.postech.adapter.output.supply.persistence.entity.SupplyEntity;
import br.com.fiap.postech.domain.catalogServices.model.CatalogServices;
import br.com.fiap.postech.domain.catalogServices.usecase.CatalogServicesUseCase;
import br.com.fiap.postech.domain.supply.model.Supply;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CatalogServicesControllerTest {
    @Mock
    private CatalogServicesUseCase catalogServicesUseCase;

    @InjectMocks
    private CatalogServicesController controller;

    @Test
    void should_return_ok_with_empty_payload_when_scroll_result_is_empty() {
        ScrollPage<CatalogServices> emptyPage = ScrollPage.<CatalogServices>builder()
                .data(List.of())
                .isLast(true)
                .cursor(null)
                .pageSize(10)
                .build();
        PaginatedCatalogServiceResponse expectedResponseBody = new PaginatedCatalogServiceResponse()
                .pageSize(10)
                .cursor(null)
                .isLast(true);
        when(catalogServicesUseCase.scroll("Pneu", 10, "0")).thenReturn(emptyPage);

        ResponseEntity<PaginatedCatalogServiceResponse> response = controller.listCatalogServices(Long.valueOf(2),"Pneu", 10, "0");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponseBody);
    }

    @Test
    void should_return_ok_with_paginated_data_when_scroll_has_content() {
        List<NeededSupplyEntity> neededSupplyEntitiesOne = new ArrayList<>();
        neededSupplyEntitiesOne.add(NeededSupplyEntity.builder().)
        CatalogServices one = CatalogServicesEntity.builder()
                .catalogServiceId(Long.valueOf(1)).name("Troca de Pneus").description("Troca dos 4 pneus de um carro")
                .basePrice(BigDecimal.valueOf(234.6)).supplies(new ArrayList<>())
                .build();


        CatalogServices two = CatalogServicesEntity.builder()
                .id(2L).sku("SKU-2").name("B").description("B").unitPrice(new BigDecimal("11.00"))
                .reservedQuantity(0).availableQuantity(10)
                .build();

        ScrollPage<CatalogServices> page = ScrollPage.<CatalogServices>builder()
                .data(List.of(one, two))
                .cursor("2")
                .isLast(false)
                .pageSize(2)
                .build();
        PaginatedCatalogServiceResponse expectedResponseBody = new PaginatedCatalogServiceResponse()
                .pageSize(2)
                .cursor("2")
                .isLast(false)
                .data(List.of(
                        new SupplyData()
                                .id(1L)
                                .sku("SKU-1")
                                .name("A")
                                .description("A")
                                .unitPrice(new BigDecimal("10.00"))
                                .suppliedBy(null)
                                .reservedQuantity(1)
                                .availableQuantity(9),
                        new SupplyData()
                                .id(2L)
                                .sku("SKU-2")
                                .name("B")
                                .description("B")
                                .unitPrice(new BigDecimal("11.00"))
                                .suppliedBy(null)
                                .reservedQuantity(0)
                                .availableQuantity(10)
                ));
        when(catalogServicesUseCase.scroll(null, 2, null)).thenReturn(page);

        ResponseEntity<PaginatedCatalogServiceResponse> response = controller.listCatalogServices(null, 2, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponseBody);
    }

}
