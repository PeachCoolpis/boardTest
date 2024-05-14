package io.board.service.impl;

import io.board.security.role.UrlRoleMapper;
import io.board.service.DynamicAuthorizationService;

import java.util.Map;

public class DynamicAuthorizationServiceImpl implements DynamicAuthorizationService {
    
    private final UrlRoleMapper delegate;
    
    public DynamicAuthorizationServiceImpl(UrlRoleMapper delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public Map<String, String> getUrlRoleMappings() {
        return delegate.getRoleMappings();
    }
}
