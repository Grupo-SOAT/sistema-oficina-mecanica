package br.com.fiap.postech.adapter.output.serviceorder.persistence.repository;

import br.com.fiap.postech.adapter.output.serviceorder.persistence.entity.ServiceOrderEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrderEntity, Long> {

    @Query("SELECT so FROM ServiceOrderEntity so WHERE " +
           "(:filterId IS NULL OR so.id = :filterId) AND " +
           "(:status IS NULL OR so.status = :status) AND " +
           "(:clientId IS NULL OR so.clientId = :clientId) AND " +
           "(:vehicleId IS NULL OR so.vehicleId = :vehicleId) AND " +
           "so.id > :cursor ORDER BY so.id ASC")
    List<ServiceOrderEntity> findAllWithFilters(
            @Param("filterId") Long filterId,
            @Param("status") String status,
            @Param("clientId") Long clientId,
            @Param("vehicleId") Long vehicleId,
            @Param("cursor") Long cursor,
            Pageable pageable
    );
}
