package br.com.fiap.postech.adapter.output.service.persistence.repository;

import br.com.fiap.postech.adapter.output.service.persistence.entity.ServiceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    Optional<ServiceEntity> findByIdAndServiceOrderId(Long id, Long serviceOrderId);

    @Query("SELECT s FROM ServiceEntity s WHERE s.serviceOrderId = :serviceOrderId AND s.id > :cursor ORDER BY s.id ASC")
    List<ServiceEntity> findAllByServiceOrderId(
            @Param("serviceOrderId") Long serviceOrderId,
            @Param("cursor") Long cursor,
            Pageable pageable
    );

    @Query("SELECT s FROM ServiceEntity s WHERE s.serviceOrderId = :serviceOrderId AND s.id = :serviceId AND s.id > :cursor ORDER BY s.id ASC")
    List<ServiceEntity> findByServiceOrderIdAndServiceId(
            @Param("serviceOrderId") Long serviceOrderId,
            @Param("serviceId") Long serviceId,
            @Param("cursor") Long cursor,
            Pageable pageable
    );

    @Query("SELECT s FROM ServiceEntity s JOIN br.com.fiap.postech.adapter.output.catalogservice.persistence.entity.CatalogServicesEntity c ON c.id = s.catalogServiceId WHERE s.serviceOrderId = :serviceOrderId AND LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND s.id > :cursor ORDER BY s.id ASC")
    List<ServiceEntity> findByServiceOrderIdAndName(
            @Param("serviceOrderId") Long serviceOrderId,
            @Param("name") String name,
            @Param("cursor") Long cursor,
            Pageable pageable
    );

    @Query("SELECT s FROM ServiceEntity s JOIN br.com.fiap.postech.adapter.output.catalogservice.persistence.entity.CatalogServicesEntity c ON c.id = s.catalogServiceId WHERE s.serviceOrderId = :serviceOrderId AND s.id = :serviceId AND LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND s.id > :cursor ORDER BY s.id ASC")
    List<ServiceEntity> findByServiceOrderIdAndServiceIdAndName(
            @Param("serviceOrderId") Long serviceOrderId,
            @Param("serviceId") Long serviceId,
            @Param("name") String name,
            @Param("cursor") Long cursor,
            Pageable pageable
    );

    List<ServiceEntity> findByCatalogServiceId(Long catalogServiceId);

    @Query("SELECT s FROM ServiceEntity s WHERE s.serviceOrderId = :serviceOrderId")
    List<ServiceEntity> findAllByServiceOrderId(@Param("serviceOrderId") Long serviceOrderId);
}
