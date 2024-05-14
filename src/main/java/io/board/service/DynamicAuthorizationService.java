package io.board.service;

import java.util.Map;

public interface DynamicAuthorizationService {
    public Map<String, String> getUrlRoleMappings();
}
