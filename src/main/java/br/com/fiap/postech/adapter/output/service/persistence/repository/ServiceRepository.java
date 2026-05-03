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

    @Query("SELECT s FROM ServiceEntity s WHERE s.serviceOrderId = :serviceOrderId AND s.status = :status AND s.id > :cursor ORDER BY s.id ASC")
    List<ServiceEntity> findByServiceOrderIdAndStatus(
            @Param("serviceOrderId") Long serviceOrderId,
            @Param("status") String status,
            @Param("cursor") Long cursor,
            Pageable pageable
    );

    @Query("SELECT s FROM ServiceEntity s WHERE s.serviceOrderId = :serviceOrderId AND s.id = :serviceId AND s.status = :status AND s.id > :cursor ORDER BY s.id ASC")
    List<ServiceEntity> findByServiceOrderIdAndServiceIdAndStatus(
            @Param("serviceOrderId") Long serviceOrderId,
            @Param("serviceId") Long serviceId,
            @Param("status") String status,
            @Param("cursor") Long cursor,
            Pageable pageable
    );
}
