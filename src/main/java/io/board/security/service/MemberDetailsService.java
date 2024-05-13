package io.board.security.service;


import io.board.dto.MemberDto;
import io.board.entity.Member;
import io.board.repository.MemberRepository;
import io.board.security.context.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class MemberDetailsService implements UserDetailsService {
    
    private final MemberRepository memberRepository;
    
    
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Member member = memberRepository.findByUsername(username);
        
        if (member == null) {
            throw new UsernameNotFoundException("회원 없음");
        }
        
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        
        MemberDto memberDto = MemberDto.createMemberDto(member);
        
        return new MemberContext(memberDto,authorities);
    }
}
