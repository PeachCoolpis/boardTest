package io.board.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Entity
@Table(name = "ROLE_HIERARCHY")
public class RoleHierarchy {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    
    private String roleName;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id" ,referencedColumnName = "id", insertable = false, updatable = false)
    private RoleHierarchy parent;
    
    
    @OneToMany(mappedBy = "parent" , fetch = FetchType.LAZY)
    private Set<RoleHierarchy> children = new HashSet<>();
    
    
    
}
