package br.com.fiap.postech.domain.user.model;

public record UserPaginationDTO(Long id,
        String username,
        Integer size,
        String cursor) {
    
}
