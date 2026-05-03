package br.com.fiap.postech.adapter.output.service.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NeededSupplyEntityTest {

    @Test
    void should_build_with_all_fields() {
        NeededSupplyEntity entity = NeededSupplyEntity.builder()
                .sku("SKU-001")
                .note("check stock")
                .quantity(2)
                .build();

        assertThat(entity.getSku()).isEqualTo("SKU-001");
        assertThat(entity.getNote()).isEqualTo("check stock");
        assertThat(entity.getQuantity()).isEqualTo(2);
    }

    @Test
    void should_support_no_args_constructor_and_setters() {
        NeededSupplyEntity entity = new NeededSupplyEntity();
        entity.setSku("SKU-002");
        entity.setNote(null);
        entity.setQuantity(5);

        assertThat(entity.getSku()).isEqualTo("SKU-002");
        assertThat(entity.getNote()).isNull();
        assertThat(entity.getQuantity()).isEqualTo(5);
    }

    @Test
    void should_support_all_args_constructor() {
        NeededSupplyEntity entity = new NeededSupplyEntity("SKU-003", "urgent", 10);

        assertThat(entity.getSku()).isEqualTo("SKU-003");
        assertThat(entity.getNote()).isEqualTo("urgent");
        assertThat(entity.getQuantity()).isEqualTo(10);
    }

    @Test
    void should_allow_null_note() {
        NeededSupplyEntity entity = NeededSupplyEntity.builder()
                .sku("SKU-004")
                .note(null)
                .quantity(1)
                .build();

        assertThat(entity.getNote()).isNull();
    }
}
