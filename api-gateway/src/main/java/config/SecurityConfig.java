package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
//* se debe usar webFlux ya que spring cloud esta creado en spring webflux
@EnableWebFluxSecurity
public class SecurityConfig {

    //* se implementa una interfaz y se le da un manejo a esta, con este bean la toma como configuracion de como comportarse
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity){
        //* la primera configuracion desabilitamos el csrf que no es necesario si se usa con JWT
        serverHttpSecurity.csrf()
                          .disable()
                .authorizeExchange(exchange  -> exchange
                         //* aca se definen las rutas que son libres de autenticacion
                        .pathMatchers("/eureka/**")
                        .permitAll()
                         //* aca dice que cualquier ruta que no entre en el patMatcher necesitara estar authenticada
                        .anyExchange()
                        .authenticated())
                //* y aca definimos que se debe de usar JWT como mecanismo de autenticacion
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);
        return serverHttpSecurity.build();
    }
}
