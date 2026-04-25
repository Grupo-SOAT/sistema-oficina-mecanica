package br.com.fiap.postech.adapter.output.user.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.Type;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Builder.Default
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id = 0L;

    @Column(name = "user_name", nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Type(ListArrayType.class)
    @Column(name = "role", columnDefinition = "text[]")
    private List<String> roles;
}