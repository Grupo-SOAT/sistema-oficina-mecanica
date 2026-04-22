package br.com.fiap.postech.adapter.output.persistence.helper.scroll;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ScrollPageTest {

    @Test
    void should_build_scroll_page_with_lombok_builder() {
        ScrollPage<String> page = ScrollPage.<String>builder()
                .data(List.of("a", "b"))
                .cursor("b")
                .isLast(false)
                .pageSize(10)
                .build();

        assertThat(page.data()).containsExactly("a", "b");
        assertThat(page.cursor()).isEqualTo("b");
        assertThat(page.isLast()).isFalse();
        assertThat(page.pageSize()).isEqualTo(10);
    }
}
