package com.vendora.cart_service.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class    JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    @Value("${jwt.auth.converter.principalAttribute}")
    private String principalAttribute;

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                        extractRealmRoles(jwt).stream()
        )
                .collect(Collectors.toSet());

        return new JwtAuthenticationToken(
                jwt,
                authorities ,
                getPrincipalClaimName(jwt)
        );
    }

    private String getPrincipalClaimName(Jwt jwt){
        String claimName = JwtClaimNames.SUB;
        if (principalAttribute != null){
            claimName = principalAttribute;
        }
        return jwt.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractRealmRoles(Jwt jwt){
        Map<String, Object> realmAccess;
        Collection<String> resourceRoles;

        if (jwt.getClaim("realm_access") == null){
            return Set.of();
        }
        realmAccess = jwt.getClaim("realm_access");

        if (realmAccess.get("roles") == null){
            return Set.of();
        }

        resourceRoles = (Collection<String>) realmAccess.get("roles");
        return resourceRoles
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}
