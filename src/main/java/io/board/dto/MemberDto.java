package io.board.dto;


import io.board.entity.Member;
import lombok.Data;

import java.util.List;

@Data
public class MemberDto {
    
    private Long id;
    
    
    private String username;
    
    private String password;
    
    
    private List<String> roles;
    
    public static MemberDto createMemberDto(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.id = member.getId();
        memberDto.username = member.getUsername();
        memberDto.password = member.getPassword();
        return memberDto;
    }
    
}
