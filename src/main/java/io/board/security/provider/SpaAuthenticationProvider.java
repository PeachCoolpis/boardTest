package io.board.security.provider;

import io.board.security.context.MemberContext;
import io.board.security.service.MemberDetailsService;
import io.board.security.token.SpaAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component(value = "spaAuthenticationProvider")
public class SpaAuthenticationProvider implements AuthenticationProvider {
    
    
    private final MemberDetailsService memberDetailsService;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        
        MemberContext memberContext = (MemberContext) memberDetailsService.loadUserByUsername(username);
        String memberPassword = memberContext.getPassword();
        
        if (!passwordEncoder.matches(password, memberPassword)) {
            throw new BadCredentialsException("비밀번호 틀림");
        }
        
        
        return new SpaAuthenticationToken(memberContext.getMemberDto(),null,memberContext.getAuthorities());
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(SpaAuthenticationToken.class);
    }
    
}
