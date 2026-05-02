package br.com.fiap.postech.adapter.output.persistence.owner.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.postech.adapter.input.api.model.DocumentType;
import br.com.fiap.postech.adapter.output.owner.persistence.OwnerPersistenceAdapter;
import br.com.fiap.postech.adapter.output.owner.persistence.entity.OwnerEntity;
import br.com.fiap.postech.adapter.output.owner.persistence.repository.OwnerRepository;
import br.com.fiap.postech.adapter.output.persistence.helper.scroll.ScrollPage;
import br.com.fiap.postech.domain.owner.model.Owner;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class OwnerPersistenceAdapterTest {
    @Mock
    private OwnerRepository repository;

    @InjectMocks
    private OwnerPersistenceAdapter adapter;
    
    @Test
    void should_scroll_without_email_filter_using_find_all_after_cursor() {
        OwnerEntity first = OwnerEntity.builder().id(11L).email("A@email.com").build();
        OwnerEntity second = OwnerEntity.builder().id(12L).email("B@email.com").build();
        OwnerEntity third = OwnerEntity.builder().id(13L).email("C@email.com").build();

        when(repository.findAllAfterCursor(anyLong(), any(Pageable.class)))
                .thenReturn(List.of(first, second, third));

        ScrollPage<Owner> page = adapter.scroll(null, 2, "10");

        ArgumentCaptor<Long> cursorCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(repository).findAllAfterCursor(cursorCaptor.capture(), pageableCaptor.capture());
        verify(repository, never()).findByEmailAfterCursor(any(), anyLong(), any(Pageable.class));

        assertThat(cursorCaptor.getValue()).isEqualTo(10L);
        assertThat(pageableCaptor.getValue().getPageNumber()).isZero();
        assertThat(pageableCaptor.getValue().getPageSize()).isEqualTo(3);

        assertThat(page.data()).hasSize(2);
        assertThat(page.isLast()).isFalse();
        assertThat(page.cursor()).isEqualTo(second.toString());
    }

    @Test
    void should_scroll_with_email_filter_using_find_by_email_after_cursor() {
        OwnerEntity only = OwnerEntity.builder().id(1L).email("testex@email.com").build();
        when(repository.findByEmailAfterCursor(eq("testex@email.com"), anyLong(), any(Pageable.class)))
                .thenReturn(List.of(only));

        ScrollPage<Owner> page = adapter.scroll("testex@email.com", 2, "invalid-cursor");

        ArgumentCaptor<Long> cursorCaptor = ArgumentCaptor.forClass(Long.class);
        verify(repository).findByEmailAfterCursor(eq("testex@email.com"), cursorCaptor.capture(), any(Pageable.class));

        assertThat(cursorCaptor.getValue()).isZero();
        assertThat(page.data()).hasSize(1);
        assertThat(page.isLast()).isTrue();
        assertThat(page.cursor()).isNull();
    }

    @Test
    void should_map_find_by_id() {
        OwnerEntity entity = OwnerEntity.builder().id(5L).build();
        when(repository.findById(5L)).thenReturn(Optional.of(entity));

        Optional<Owner> found = adapter.findById(5L);

        assertThat(found).containsSame(entity);
    }

    @Test
    void should_map_find_by_document() {
        OwnerEntity entity = OwnerEntity.builder().id(8L).document("31058167049").build();
        when(repository.findByDocument("31058167049")).thenReturn(Optional.of(entity));

        Optional<Owner> found = adapter.findByDocument("31058167049");

        assertThat(found).containsSame(entity);
    }

    @Test
    void should_save_when_owner_already_entity() {
        OwnerEntity entity = OwnerEntity.builder().id(9L).build();
        when(repository.save(entity)).thenReturn(entity);

        Owner saved = adapter.save(entity);

        assertThat(saved).isSameAs(entity);
    }

    @Test
    void should_save_when_owner_is_not_entity() {
        Owner owner = new TestOwner(40L, "Nome", "31058167049", DocumentType.CPF, "12314153", "teste40@email.com");
        OwnerEntity expected = OwnerEntity.builder()
            .id(40L)
            .name("Nome")
            .document("31058167049")
            .documentType(DocumentType.CPF)
            .phone("12314153")
            .email("teste40@email.com")
            .build();

        when(repository.save(any(OwnerEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Owner saved = adapter.save(owner);

        assertThat(saved)
            .isNotNull()
            .isInstanceOf(OwnerEntity.class)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Test
    void should_delegate_delete_by_id() {
        adapter.deleteById(77L);
        verify(repository).deleteById(77L);
    }

    @Test
    void should_delegate_exists_by_id() {
        when(repository.existsById(55L)).thenReturn(true);

        boolean exists = adapter.existsById(55L);

        assertThat(exists).isTrue();
    }

    private static final class TestOwner implements Owner {

        private Long id;
        private String name;
        private String document;
        private DocumentType documentType;
        private String phone;
        private String email;

        private TestOwner(Long id,
                        String name,
                        String document,
                        DocumentType documentType,
                        String phone,
                        String email) {
            this.id = id;
            this.name = name;
            this.document = document;
            this.documentType = documentType;
            this.phone = phone;
            this.email = email;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getDocument() {
            return document;
        }

        @Override
        public void setDocument(String document) {
            this.document = document;
        }

        @Override
        public DocumentType getDocumentType() {
            return documentType;
        }

        @Override
        public void setDocumentType(DocumentType documentType) {
            this.documentType = documentType;
        }

        @Override
        public String getPhone() {
            return phone;
        }

        @Override
        public void setPhone(String phone) {
            this.phone = phone;
        }

        @Override
        public String getEmail() {
            return email;
        }

        @Override
        public void setEmail(String email) {
            this.email = email;
        }
    }

}
