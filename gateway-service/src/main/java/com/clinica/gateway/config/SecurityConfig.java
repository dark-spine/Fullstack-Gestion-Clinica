package com.clinica.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, JwtUtil jwtUtil) {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers(
                    "/actuator/health",
                    "/actuator/info",
                    "/api/auth/**",
                    "/doc/**",
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()
                .anyExchange().authenticated()
            )
            .addFilterAt(new JwtWebFilter(jwtUtil), SecurityWebFiltersOrder.AUTHENTICATION)
            .httpBasic().disable();

        return http.build();
    }

    static class JwtWebFilter implements WebFilter {

        private final JwtUtil jwtUtil;

        public JwtWebFilter(JwtUtil jwtUtil) {
            this.jwtUtil = jwtUtil;
        }

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
            String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (auth != null && auth.startsWith("Bearer ")) {
                String token = auth.substring(7);
                try {
                    var claims = jwtUtil.parseClaims(token);
                    String username = claims.getSubject();
                    Object rolesObj = claims.get("roles");
                    var authorities = new java.util.ArrayList<org.springframework.security.core.authority.SimpleGrantedAuthority>();
                    if (rolesObj != null) {
                        String roles = rolesObj.toString();
                        for (String r : roles.split(",")) {
                            authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority(r.trim()));
                        }
                    }
                    var authToken = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(username, null, authorities);
                    var securityContext = new org.springframework.security.core.context.SecurityContextImpl(authToken);
                    return chain.filter(exchange).contextWrite(org.springframework.security.core.context.ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
                } catch (Exception e) {
                    exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }
            return chain.filter(exchange);
        }
    }
}
