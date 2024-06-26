package io.board.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SpaAuthenticationToken extends AbstractAuthenticationToken {
    
    private final Object principal;
    private final Object credentials;
    
    public SpaAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(true);
    }
    
    public SpaAuthenticationToken( Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }
    
    @Override
    public Object getCredentials() {
        return this.credentials;
    }
    
    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
