package br.com.fiap.postech.adapter.output.catalogService.persistence.entity;

import br.com.fiap.postech.domain.catalogServices.model.CatalogServices;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "catalog_services")
public class CatalogServicesEntity implements CatalogServices {
    @Builder.Default
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catalog_service_id")
    private Long catalogServiceId = 0L;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;

}
