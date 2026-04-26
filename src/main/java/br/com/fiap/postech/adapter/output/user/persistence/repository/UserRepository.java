package br.com.fiap.postech.adapter.output.user.persistence.repository;

import br.com.fiap.postech.adapter.output.user.persistence.entity.UserEntity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    @Transactional
    @Modifying
    @Query("""
                UPDATE UserEntity u
                SET u.username = COALESCE(:username, u.username),
                    u.roles = :roles
                WHERE u.id = :id
            """)
    int updateUser(
            @Param("id") Long id,
            @Param("username") String username,
            @Param("roles") String[] roles);

    @Query("SELECT s FROM UserEntity s WHERE s.id > :cursor ORDER BY s.id ASC")
    List<UserEntity> findAllAfterCursor(
            @Param("cursor") Long cursor,
            Pageable pageable);

    @Query("SELECT s FROM UserEntity s WHERE LOWER(s.username) LIKE LOWER(CONCAT('%', :username, '%')) AND s.id > :cursor ORDER BY s.id ASC")
    List<UserEntity> findByUsernameAfterCursor(
            @Param("username") String username,
            @Param("cursor") Long cursor,
            Pageable pageable);

}