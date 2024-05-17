package io.board.security.filter.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.board.dto.MemberDto;
import io.board.security.token.SpaAuthenticationToken;
import io.board.security.util.WebUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;



public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper mapper = new ObjectMapper();
    public JwtAuthenticationFilter() {
        super(new AntPathRequestMatcher("/api/login", "POST"));
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
    
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(authResult);
        
        getSuccessHandler().onAuthenticationSuccess(request,response,authResult);
        
    }
}
