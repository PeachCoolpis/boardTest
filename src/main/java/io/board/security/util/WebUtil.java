package io.board.security.util;

import jakarta.servlet.http.HttpServletRequest;

public class WebUtil {
    
    private final static String XML_HTTP_REQUEST = "XMLHttpRequest";
    private final static String X_REQUESTED_WITH = "XMLHttpRequest";
    
    private final static String CONTENT_TYPE = "Content-type";
    private static final String CONTENT_TYPE_JSON = "application/json";
    
    public static boolean isAjax(HttpServletRequest request) {
        return XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH));
    }
    
    public static boolean isContentTypeJson(HttpServletRequest request) {
        return request.getHeader(CONTENT_TYPE).contains(CONTENT_TYPE_JSON);
    }
    
}