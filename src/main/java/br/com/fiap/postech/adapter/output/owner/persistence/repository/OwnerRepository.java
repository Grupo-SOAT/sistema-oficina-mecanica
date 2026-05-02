package br.com.fiap.postech.adapter.output.owner.persistence.repository;

import java.util.List;
import java.util.Optional;

import br.com.fiap.postech.adapter.output.owner.persistence.entity.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

public interface OwnerRepository extends JpaRepository<OwnerEntity, Long>{
    Optional<OwnerEntity> findByDocument(String document);

    @Query("SELECT s FROM OwnerEntity s WHERE s.id > :cursor ORDER BY s.id ASC")
    List<OwnerEntity> findAllAfterCursor(
            @Param("cursor") Long cursor,
            Pageable pageable
    );

    @Query("SELECT s FROM OwnerEntity s WHERE LOWER(s.email) LIKE LOWER(CONCAT('%', :email, '%')) AND s.id > :cursor ORDER BY s.id ASC")
    List<OwnerEntity> findByEmailAfterCursor(
            @Param("email") String email,
            @Param("cursor") Long cursor,
            Pageable pageable
    );
}
