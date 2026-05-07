package br.com.fiap.postech.adapter.output.catalogservice.persistence.repository;

import br.com.fiap.postech.adapter.output.catalogservice.persistence.entity.CatalogServicesEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CatalogServicesRepository extends JpaRepository<CatalogServicesEntity, Long>, JpaSpecificationExecutor<CatalogServicesEntity> {
    @EntityGraph(attributePaths = "supplies")
    Optional<CatalogServicesEntity> findWithSuppliesById(Long id);

    @EntityGraph(attributePaths = "supplies")
    Optional<CatalogServicesEntity> findByName(String name);

    @EntityGraph(attributePaths = "supplies")
    @Query("SELECT s FROM CatalogServicesEntity s WHERE s.id > :cursor ORDER BY s.id ASC")
    List<CatalogServicesEntity> findAllAfterCursor(
            @Param("cursor") Long cursor,
            Pageable pageable
    );

    @EntityGraph(attributePaths = "supplies")
    @Query("SELECT s FROM CatalogServicesEntity s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) AND s.id > :cursor ORDER BY s.id ASC")
    List<CatalogServicesEntity> findByNameAfterCursor(
            @Param("name") String name,
            @Param("cursor") Long cursor,
            Pageable pageable
    );
}
