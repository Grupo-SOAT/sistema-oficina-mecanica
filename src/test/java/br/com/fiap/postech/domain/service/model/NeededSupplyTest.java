package br.com.fiap.postech.domain.service.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NeededSupplyTest {

    @Test
    void should_build_with_all_fields() {
        NeededSupply supply = NeededSupply.builder()
                .idSupply(1)
                .note("urgent")
                .quantity(3)
                .build();

        assertThat(supply.getIdSupply()).isEqualTo(1);
        assertThat(supply.getNote()).isEqualTo("urgent");
        assertThat(supply.getQuantity()).isEqualTo(3);
    }

    @Test
    void should_support_no_args_constructor_and_setters() {
        NeededSupply supply = new NeededSupply();
        supply.setIdSupply(2);
        supply.setNote(null);
        supply.setQuantity(1);

        assertThat(supply.getIdSupply()).isEqualTo(2);
        assertThat(supply.getNote()).isNull();
        assertThat(supply.getQuantity()).isEqualTo(1);
    }

    @Test
    void should_support_all_args_constructor() {
        NeededSupply supply = new NeededSupply(3, "check stock", 5);

        assertThat(supply.getIdSupply()).isEqualTo(3);
        assertThat(supply.getNote()).isEqualTo("check stock");
        assertThat(supply.getQuantity()).isEqualTo(5);
    }

    @Test
    void should_equal_another_supply_with_same_fields() {
        NeededSupply a = new NeededSupply(1, "urgent", 3);
        NeededSupply b = new NeededSupply(1, "urgent", 3);

        assertThat(a).isEqualTo(b);
        assertThat(a).hasSameHashCodeAs(b);
    }

    @Test
    void should_not_equal_supply_with_different_id() {
        NeededSupply a = new NeededSupply(1, "urgent", 3);
        NeededSupply b = new NeededSupply(2, "urgent", 3);

        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void should_allow_null_note() {
        NeededSupply supply = NeededSupply.builder()
                .idSupply(4)
                .note(null)
                .quantity(2)
                .build();

        assertThat(supply.getNote()).isNull();
    }
}
