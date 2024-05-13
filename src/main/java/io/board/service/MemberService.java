package io.board.service;

import io.board.dto.MemberDto;

import java.util.List;

public interface MemberService {
    
    
    List<MemberDto> findAllMember();
    
    Long createMember(MemberDto memberDto);
    
    MemberDto findMember(Long id);
    
    
    Long modifyMember(Long Id , MemberDto memberDto);
    
}
