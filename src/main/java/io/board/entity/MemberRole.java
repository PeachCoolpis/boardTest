package io.board.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class MemberRole extends BaseDate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    
    
    public static MemberRole createMemberRole(Member member , Role role) {
        MemberRole memberRole = new MemberRole();
        memberRole.member = member;
        memberRole.role = role;
        return memberRole;
    }
}
