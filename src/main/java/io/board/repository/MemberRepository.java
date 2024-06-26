package io.board.repository;

import io.board.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
    
    Member findByUsername(String username);
}
