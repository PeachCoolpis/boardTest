package io.board.security.config;


import io.board.security.csrf.CsrfCookieFilter;
import io.board.security.filter.SpaAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
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
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.authenticationProvider(spaAuthenticationProvider);
        AuthenticationManager manager = managerBuilder.build();
        
        
        
        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api","/api/login","/api/save","/api/findAll").permitAll()
                )
                .addFilterBefore(spaAuthenticationFilter(http,manager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(spaCsrfTokenRequestHandler)
                )
                .cors(cors -> cors.configurationSource(configurationSource()))
                .authenticationManager(manager)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(spaAuthenticationEntryPoint)
                        .accessDeniedHandler(spaAccessDeniedHandler)
                )
        ;
        
        return http.build();
    }
    
    private SpaAuthenticationFilter spaAuthenticationFilter(HttpSecurity http,AuthenticationManager manager) {
        SpaAuthenticationFilter spaAuthenticationFilter = new SpaAuthenticationFilter(http);
        spaAuthenticationFilter.setAuthenticationManager(manager);
        spaAuthenticationFilter.setAuthenticationSuccessHandler(spaAuthenticationSuccessHandler);
        spaAuthenticationFilter.setAuthenticationFailureHandler(spaAuthenticationFailureHandler);
        return spaAuthenticationFilter;
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
