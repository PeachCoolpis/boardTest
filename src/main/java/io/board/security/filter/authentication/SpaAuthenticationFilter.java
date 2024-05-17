package io.board.security.filter.authentication;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.board.dto.MemberDto;
import io.board.security.token.SpaAuthenticationToken;
import io.board.security.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class SpaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    
    
    private final ObjectMapper mapper = new ObjectMapper();
    
    public SpaAuthenticationFilter(HttpSecurity http) {
        super(new AntPathRequestMatcher("/api/login", "POST"));
        setSecurityContextRepository(getSecurityContextRepository(http));
    }
    
    private SecurityContextRepository getSecurityContextRepository(HttpSecurity http) {
        SecurityContextRepository contextRepository = http.getSharedObject(SecurityContextRepository.class);
        if (contextRepository == null) {
            contextRepository =  new DelegatingSecurityContextRepository(new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository());
        }
        return contextRepository;
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        
        
        if (!HttpMethod.POST.name().equals(request.getMethod()) || !WebUtil.isAjax(request)) {
            throw new IllegalArgumentException("Authentication method not supported");
        }
        
        MemberDto memberDto = mapper.readValue(request.getReader(), MemberDto.class);
        
        
        if (!StringUtils.hasText(memberDto.getUsername()) || !StringUtils.hasText(memberDto.getPassword())) {
            throw new AuthenticationServiceException("Username or Password provided");
        }
        
        SpaAuthenticationToken spaAuthenticationToken = new SpaAuthenticationToken(memberDto.getUsername(), memberDto.getPassword());
        
        return this.getAuthenticationManager().authenticate(spaAuthenticationToken);
    }
}
