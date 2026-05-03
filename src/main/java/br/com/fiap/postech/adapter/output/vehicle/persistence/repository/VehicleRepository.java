package br.com.fiap.postech.adapter.output.vehicle.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fiap.postech.adapter.output.vehicle.persistence.entity.VehicleEntity;

public interface VehicleRepository extends JpaRepository<VehicleEntity, Long>{
    Optional<VehicleEntity> findByLicensePlate(String licensePlate);
    
    @Query("SELECT s FROM VehicleEntity s WHERE s.id > :cursor ORDER BY s.id ASC")
    List<VehicleEntity> findAllAfterCursor(
            @Param("cursor") Long cursor,
            Pageable pageable
    );

    @Query("SELECT s FROM VehicleEntity s WHERE LOWER(s.licensePlate) LIKE LOWER(CONCAT(:licensePlate, '%')) AND s.id > :cursor ORDER BY s.id ASC")
    List<VehicleEntity> findByLicensePlateAfterCursor(
            @Param("licensePlate") String licensePlate,
            @Param("cursor") Long cursor,
            Pageable pageable
    );
}
