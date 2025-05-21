/* package fi.haagahelia.sokos.security;

import fi.haagahelia.sokos.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JWTAuth jwtAuthFilter;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JWTAuth jwtAuthFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // Public endpoints (no authentication required)
                .requestMatchers(
                    "/auth/**",
                    "/rooms/types",
                    "/static/**",
                    "/css/**",
                    "/js/**",
                    "/images/**"
                ).permitAll()
                
                // User management (Admin only)
                .requestMatchers(
                    "/users/all",
                    "/users/delete/**"
                ).hasAuthority("ADMIN")
                
                // Room management (Admin only)
                .requestMatchers(
                    "/rooms/add",
                    "/rooms/update/**",
                    "/rooms/delete/**"
                ).hasAuthority("ADMIN")
                
                // Booking management
                .requestMatchers(
                    "/bookings/all",
                    "/bookings/cancel/**"
                ).hasAnyAuthority("USER", "ADMIN")
                
                // User profile endpoints
                .requestMatchers(
                    "/users/get-logged-in-profile-info",
                    "/users/get-user-bookings/**"
                ).hasAnyAuthority("USER", "ADMIN")
                
                // Room viewing endpoints
                .requestMatchers(
                    "/rooms/all",
                    "/rooms/room-by-id/**",
                    "/rooms/all-available-rooms",
                    "/rooms/available-rooms-by-date-and-type"
                ).hasAnyAuthority("USER", "ADMIN")
                
                // Default rule - any other request needs authentication
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
    */