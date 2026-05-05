package br.com.fiap.postech.adapter.output.service.persistence;

import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;
import br.com.fiap.postech.adapter.output.service.persistence.entity.ServiceEntity;
import br.com.fiap.postech.adapter.output.service.persistence.repository.ServiceRepository;
import br.com.fiap.postech.domain.service.model.Service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicePersistenceAdapterTest {

    @Mock
    private ServiceRepository repository;

    @InjectMocks
    private ServicePersistenceAdapter adapter;

    @Test
    void should_scroll_all_services_when_no_filter() {
        ServiceEntity first = ServiceEntity.builder().id(1L).serviceOrderId(10L).build();
        ServiceEntity second = ServiceEntity.builder().id(2L).serviceOrderId(10L).build();
        ServiceEntity third = ServiceEntity.builder().id(3L).serviceOrderId(10L).build();

        when(repository.findAllByServiceOrderId(eq(10L), anyLong(), any(Pageable.class)))
                .thenReturn(List.of(first, second, third));

        ScrollPage<Service> page = adapter.scroll(10L, null, null, 2, "0");

        ArgumentCaptor<Long> cursorCaptor = ArgumentCaptor.forClass(Long.class);
        verify(repository).findAllByServiceOrderId(eq(10L), cursorCaptor.capture(), any(Pageable.class));

        assertThat(cursorCaptor.getValue()).isZero();
        assertThat(page.data()).hasSize(2);
        assertThat(page.isLast()).isFalse();
    }

    @Test
    void should_scroll_by_service_id_when_filter_provided() {
        ServiceEntity entity = ServiceEntity.builder().id(5L).serviceOrderId(10L).build();
        when(repository.findByServiceOrderIdAndServiceId(eq(10L), eq(5L), anyLong(), any(Pageable.class)))
                .thenReturn(List.of(entity));

        ScrollPage<Service> page = adapter.scroll(10L, 5L, null, 10, null);

        verify(repository).findByServiceOrderIdAndServiceId(eq(10L), eq(5L), anyLong(), any(Pageable.class));
        assertThat(page.data()).hasSize(1);
        assertThat(page.isLast()).isTrue();
    }

    @Test
    void should_scroll_by_status_when_filter_provided() {
        ServiceEntity entity = ServiceEntity.builder().id(1L).serviceOrderId(10L).status("IN_PROGRESS").build();
        when(repository.findByServiceOrderIdAndStatus(eq(10L), eq("IN_PROGRESS"), anyLong(), any(Pageable.class)))
                .thenReturn(List.of(entity));

        ScrollPage<Service> page = adapter.scroll(10L, null, "IN_PROGRESS", 10, null);

        verify(repository).findByServiceOrderIdAndStatus(eq(10L), eq("IN_PROGRESS"), anyLong(), any(Pageable.class));
        assertThat(page.data()).hasSize(1);
    }

    @Test
    void should_scroll_by_service_id_and_status_when_both_filters_provided() {
        ServiceEntity entity = ServiceEntity.builder().id(5L).serviceOrderId(10L).status("COMPLETED").build();
        when(repository.findByServiceOrderIdAndServiceIdAndStatus(eq(10L), eq(5L), eq("COMPLETED"), anyLong(), any(Pageable.class)))
                .thenReturn(List.of(entity));

        ScrollPage<Service> page = adapter.scroll(10L, 5L, "COMPLETED", 10, null);

        verify(repository).findByServiceOrderIdAndServiceIdAndStatus(eq(10L), eq(5L), eq("COMPLETED"), anyLong(), any(Pageable.class));
        assertThat(page.data()).hasSize(1);
    }

    @Test
    void should_return_empty_page_when_no_results() {
        when(repository.findAllByServiceOrderId(eq(10L), anyLong(), any(Pageable.class)))
                .thenReturn(List.of());

        ScrollPage<Service> page = adapter.scroll(10L, null, null, 10, null);

        assertThat(page.data()).isEmpty();
        assertThat(page.isLast()).isTrue();
        assertThat(page.cursor()).isNull();
    }

    @Test
    void should_find_by_id_and_service_order_id() {
        ServiceEntity entity = ServiceEntity.builder().id(5L).serviceOrderId(10L).build();
        when(repository.findByIdAndServiceOrderId(5L, 10L)).thenReturn(Optional.of(entity));

        Optional<Service> result = adapter.findByIdAndServiceOrderId(5L, 10L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(5L);
    }

    @Test
    void should_return_empty_when_service_not_found() {
        when(repository.findByIdAndServiceOrderId(99L, 10L)).thenReturn(Optional.empty());

        Optional<Service> result = adapter.findByIdAndServiceOrderId(99L, 10L);

        assertThat(result).isEmpty();
    }

    @Test
    void should_save_entity_directly_when_instance_is_service_entity() {
        ServiceEntity entity = ServiceEntity.builder()
                .id(1L).serviceOrderId(10L).catalogServiceId(5L)
                .price(new BigDecimal("100.00")).status("AWAITING_APPROVAL").build();

        when(repository.save(entity)).thenReturn(entity);

        Service saved = adapter.save(entity);

        verify(repository).save(entity);
        assertThat(saved).isSameAs(entity);
    }

    @Test
    void should_delete_by_id() {
        adapter.deleteById(5L);

        verify(repository).deleteById(5L);
    }
}
