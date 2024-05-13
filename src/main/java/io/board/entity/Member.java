package io.board.entity;


import io.board.dto.MemberDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Comment;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
public class Member extends BaseDate{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    
    @Comment(value = "로그인 아이디")
    private String username;
    @Comment(value = "비밀 번호")
    private String password;
    

    


    public static Member createMember(MemberDto memberDto) {
        Member member = new Member();
        member.username = memberDto.getUsername();
        return member;
    }
    
    public void passSetting(String password) {
        this.password = password;
    }
}
