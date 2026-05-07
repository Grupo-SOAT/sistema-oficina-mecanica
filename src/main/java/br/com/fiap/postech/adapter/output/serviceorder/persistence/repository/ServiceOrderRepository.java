package br.com.fiap.postech.adapter.output.serviceorder.persistence.repository;

import br.com.fiap.postech.adapter.output.serviceorder.persistence.entity.ServiceOrderEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceOrderRepository extends JpaRepository<ServiceOrderEntity, Long> {

    List<ServiceOrderEntity> findAllByIdGreaterThanOrderByIdAsc(Long cursor, Pageable pageable);

    List<ServiceOrderEntity> findAllByStatusAndIdGreaterThanOrderByIdAsc(String status, Long cursor, Pageable pageable);
}
