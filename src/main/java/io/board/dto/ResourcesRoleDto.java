package io.board.dto;

import io.board.entity.Resources;
import io.board.entity.Role;
import lombok.Data;


@Data
public class ResourcesRoleDto {
    
    
    
    private Long id;
    
    
    
    private Role role;
    
    
    
    private Resources resources;
    
}
