package com.maicol1912.microservice.discoveryserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //* tomamos variables de entorno declaradas en el application
    @Value("${eureka.username}")
    private String username;

    @Value("${eureka.password}")
    private String password;
    //* este metodo sobreescribe la authenticacion de la app por medio de un username eureka y un password password
    //* y ademas se asigna un rol de USER
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.inMemoryAuthentication()
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser(username).password(password)
                .authorities("USER");
    }

    //* aca sobreescribe el metodo para quitar la autorizacion csrf
    //* luego configura la autorizacion para las demas rutas , cualquier solicitud que llegue a la aplicación requerirá que el usuario esté autenticado y tenga al menos un rol para poder acceder a ella.
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests().anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }
}
