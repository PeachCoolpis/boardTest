package io.board.security.csrf;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

@Component(value = "spaCsrfTokenRequestHandler")
public class SpaCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler {
    
    private final CsrfTokenRequestAttributeHandler delegate = new XorCsrfTokenRequestAttributeHandler();
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> deferredCsrfToken) {
        delegate.handle(request,response,deferredCsrfToken);
    }
    
    @Override
    public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
        boolean hasText = StringUtils.hasText(request.getHeader(csrfToken.getHeaderName()));
        if (hasText) {
            return super.resolveCsrfTokenValue(request, csrfToken);
        }
        return delegate.resolveCsrfTokenValue(request, csrfToken);
        
    }
}
