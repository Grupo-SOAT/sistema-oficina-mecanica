package br.com.fiap.postech.adapter.output.persistence.helper.scroll;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ScrollerTest {
    @ParameterizedTest
    @MethodSource("cursorCases")
    void should_parse_cursor_and_create_pageable_with_extra_item(String cursor, long expectedCursor, int pageSize) {
        AtomicLong receivedCursor = new AtomicLong(Long.MIN_VALUE);
        AtomicReference<Pageable> receivedPageable = new AtomicReference<>();

        Scroller.scroll(cursor, pageSize, (parsedCursor, pageable) -> {
            receivedCursor.set(parsedCursor);
            receivedPageable.set(pageable);
            return List.of();
        });

        assertThat(receivedCursor.get()).isEqualTo(expectedCursor);
        assertThat(receivedPageable.get()).isNotNull();
        assertThat(receivedPageable.get().getPageNumber()).isZero();
        assertThat(receivedPageable.get().getPageSize()).isEqualTo(pageSize + 1);
    }

    @ParameterizedTest
    @MethodSource("scrollCases")
    void should_build_scroll_page_for_multiple_data_shapes(
            List<Integer> sourceData,
            int pageSize,
            int expectedDataSize,
            boolean expectedIsLast,
            String expectedCursor
    ) {
        ScrollPage<Integer> page = Scroller.scroll("0", pageSize, (parsedCursor, pageable) -> sourceData);

        assertThat(page.pageSize()).isEqualTo(pageSize);
        assertThat(page.data()).hasSize(expectedDataSize);
        assertThat(page.isLast()).isEqualTo(expectedIsLast);
        assertThat(page.cursor()).isEqualTo(expectedCursor);
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> cursorCases() {
        int pageSize = 5;
        return Stream.of(
                arguments(null, 0L, pageSize),
                arguments("", 0L, pageSize),
                arguments("   ", 0L, pageSize),
                arguments("abc", 0L, pageSize),
                arguments("10", 10L, pageSize)
        );
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> scrollCases() {
        return Stream.of(
                arguments(null, 2, 0, true, null),
                arguments(List.of(), 2, 0, true, null),
                arguments(List.of(1), 2, 1, true, null),
                arguments(List.of(1, 2), 2, 2, true, null),
                arguments(List.of(1, 2, 3), 2, 2, false, "2")
        );
    }
}
