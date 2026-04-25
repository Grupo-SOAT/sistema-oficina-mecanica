package br.com.fiap.postech.adapter.output.user.persistence.repository;

import br.com.fiap.postech.adapter.output.user.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // 🔍 Buscar por ID (já existe no JpaRepository, mas deixei explícito)
    Optional<UserEntity> findById(Long id);

    // 🔍 Buscar por username (útil pra login)
    Optional<UserEntity> findByUsername(String username);

    // 📋 Listar todos (já existe, mas explícito)
    List<UserEntity> findAll();

    // 🆕 Criar usuário → usar save()

    // ❌ Deletar por ID (já existe, mas explícito)
    void deleteById(Long id);

    // 🔄 Atualizar username e roles
    @Transactional
    @Modifying
    @Query("""
        UPDATE UserEntity u 
        SET u.username = COALESCE(:username, u.username),
            u.roles = COALESCE(:roles, u.roles)
        WHERE u.id = :id
    """)
    int updateUser(Long id, String username, List<String> roles);
}