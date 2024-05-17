package io.board.security.config;


import io.board.security.csrf.CsrfCookieFilter;
import io.board.security.filter.authentication.JwtAuthenticationFilter;
import io.board.security.filter.authentication.SpaAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    
    
    @Value("${cors.addr}")
    private String frontAddr;
    private final AuthenticationProvider spaAuthenticationProvider;
    private final AuthenticationSuccessHandler spaAuthenticationSuccessHandler;
    private final AuthenticationFailureHandler spaAuthenticationFailureHandler;
    private final AuthenticationEntryPoint spaAuthenticationEntryPoint;
    private final AccessDeniedHandler spaAccessDeniedHandler;
    private final CsrfTokenRequestAttributeHandler spaCsrfTokenRequestHandler;
    private final AuthorizationManager<RequestAuthorizationContext> spaAuthorizationManager;
   
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.authenticationProvider(spaAuthenticationProvider);
        AuthenticationManager manager = managerBuilder.build();
        
        
        
        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().access(spaAuthorizationManager))
                //.addFilterBefore(spaAuthenticationFilter(http,manager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(manager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable
                        //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        //.csrfTokenRequestHandler(spaCsrfTokenRequestHandler)
                        )
                .cors(cors -> cors.configurationSource(configurationSource()))
                .authenticationManager(manager)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(spaAuthenticationEntryPoint)
                        .accessDeniedHandler(spaAccessDeniedHandler));
        
        return http.build();
    }
    
    private SpaAuthenticationFilter spaAuthenticationFilter(HttpSecurity http,AuthenticationManager manager) {
        SpaAuthenticationFilter spaAuthenticationFilter = new SpaAuthenticationFilter(http);
        spaAuthenticationFilter.setAuthenticationManager(manager);
        spaAuthenticationFilter.setAuthenticationSuccessHandler(spaAuthenticationSuccessHandler);
        spaAuthenticationFilter.setAuthenticationFailureHandler(spaAuthenticationFailureHandler);
        return spaAuthenticationFilter;
    }
    private JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager manager) {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
        jwtAuthenticationFilter.setAuthenticationManager(manager);
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(spaAuthenticationSuccessHandler);
        jwtAuthenticationFilter.setAuthenticationFailureHandler(spaAuthenticationFailureHandler);
        return jwtAuthenticationFilter;
    }
    
    
    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(frontAddr);
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod(HttpMethod.GET);
        configuration.addAllowedMethod(HttpMethod.POST);
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", configuration);
        return configurationSource;
    }
    
}
