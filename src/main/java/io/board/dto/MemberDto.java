package io.board.dto;


import io.board.entity.Member;
import io.board.entity.MemberRole;
import io.board.entity.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class MemberDto {
    
    private Long id;
    
    
    private String username;
    
    private String password;
    
    
    private List<String> memberRoles;
    
    
    public MemberDto(Long id, String username, String password, List<String> memberRoles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.memberRoles = memberRoles;
    }
    
    public static MemberDto createMemberDto(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.id = member.getId();
        memberDto.username = member.getUsername();
        memberDto.password = member.getPassword();
        memberDto.memberRoles = member.getMemberRoles().stream()
                .map(r -> r.getRole().getRoleName()).collect(Collectors.toList());
        return memberDto;
    }
    
}
