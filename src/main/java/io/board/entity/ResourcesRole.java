package io.board.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ResourcesRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Long id;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resources_id")
    private Resources resources;
    
    
    
}
