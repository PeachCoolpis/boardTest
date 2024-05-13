package io.board.repository;

import io.board.entity.RoleHierarchy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleHierarchRepository extends JpaRepository<RoleHierarchy,Long> {
}
