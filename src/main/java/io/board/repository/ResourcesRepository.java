package io.board.repository;

import io.board.entity.Resources;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourcesRepository extends JpaRepository<Resources,Long> {
}
