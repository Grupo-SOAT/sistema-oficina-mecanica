package br.com.fiap.postech.adapter.output.catalogService.persistence.repository;

import br.com.fiap.postech.adapter.output.catalogService.persistence.entity.CatalogServicesEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CatalogServicesRepository extends JpaRepository<CatalogServicesEntity, Long> {
    Optional<CatalogServicesEntity> findByName(String name);

    @Query("SELECT s FROM CatalogServicesEntity s WHERE s.catalogServiceId > :cursor ORDER BY s.catalogServiceId ASC")
    List<CatalogServicesEntity> findAllAfterCursor(
            @Param("cursor") Long cursor,
            Pageable pageable
    );

    @Query("SELECT s FROM CatalogServicesEntity s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) AND s.catalogServiceId > :cursor ORDER BY s.catalogServiceId ASC")
    List<CatalogServicesEntity> findByNameAfterCursor(
            @Param("name") String name,
            @Param("cursor") Long cursor,
            Pageable pageable
    );
}
