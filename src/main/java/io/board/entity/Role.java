package io.board.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
public class Role {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;
    
    private String roleName;
    

    
    public static Role createRole(String name) {
        Role role = new Role();
        role.roleName = name;
        return role;
    }
}
