package io.board.dto;

import io.board.entity.Member;
import io.board.entity.Role;
import lombok.Data;

@Data
public class MemberRoleDto {
    
    private Long id;
    
    
    private Role role;
    
    
    private Member member;
}
