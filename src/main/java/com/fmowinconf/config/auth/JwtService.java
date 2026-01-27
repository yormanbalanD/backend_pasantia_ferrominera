package com.fmowinconf.config.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET_KEY = "ESDFIJGPIRNijwNIrgnrei64569579056itfoydyhdkrmoyjduoi67695dokhtjdoroipjseyoritdhxom5i6eopdnpoe79076";
    private final long EXPIRATION_TIME = 86400000;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generarToken(ContenidoJWT contenido) {
        return Jwts.builder()
                // .subject() requiere String, usamos String.valueOf()
                .subject(String.valueOf(contenido.getId()))
                .claim("nombre_completo", contenido.getNombre_completo())
                .claim("ficha", contenido.getFicha())
                .claim("permisos", contenido.getPermisos())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    public ContenidoJWT extraerContenido(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        ContenidoJWT contenido = new ContenidoJWT();
        // Convertimos el String del subject de vuelta a long
        contenido.setId(Long.parseLong(claims.getSubject()));
        contenido.setNombre_completo(claims.get("nombre_completo", String.class));
        contenido.setFicha(claims.get("ficha", String.class));
        contenido.setPermisos(claims.get("permisos", String.class));
        // contenido.setExpiration(claims.getExpiration());
        return contenido;
    }

    public boolean isTokenExpired(String token) {
        try {
            return extraerExpiracion(token).before(new Date());
        } catch (Exception e) {
            // Si hay un error al leer el token (como que ya expiró),
            // la librería JJWT suele lanzar una excepción automáticamente.
            return true;
        }
    }

    private Date extraerExpiracion(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }

    public <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(this.getSigningKey()) // Usa la misma llave de firma que creamos antes
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }
}