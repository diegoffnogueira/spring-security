package com.diego.api.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_ROLE = "role";
    static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Obtem o userName(email) contido no token JWT
     *
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token){
        String userName;
        try {
            Claims claims =getClaimsFromToken(token);
            userName = claims.getSubject();
        }catch (Exception e){
            userName = null;
        }
        return userName;
    }

    /**
     * Retorna a data de expiração de um token JWT
     *
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token){
        Date expiration;
        try {
            Claims claims =getClaimsFromToken(token);
            expiration = claims.getExpiration();
        }catch (Exception e){
            expiration = null;
        }
        return expiration;
    }

    /**
     * Cria um novo token (refresh)
     *
     * @param token
     * @return
     */
    public String refreshToken(String token){
        String refreshedToken;
        try {
            Claims claims =getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = gerarToken(claims);
        }catch (Exception e){
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * Verifica e retorna se um token JWT é válido
     *
     * @param token
     * @return
     */
    public boolean tokenValido(String token){
        return !tokenExpirado(token);
    }

    /**
     * Retorna um novo token JWT com base nos dados do usuário
     * @param userDetails
     * @return
     */
    public String obterToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        userDetails.getAuthorities().forEach(authority -> claims.put(CLAIM_KEY_ROLE, authority.getAuthority()));
        claims.put(CLAIM_KEY_CREATED, new Date());
        return gerarToken(claims);
    }

    /**
     * Realiza o parse do token JWT para extrair as informações contidas no body dele
     *
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token){
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        }catch (Exception e){
            claims = null;
        }
        return claims;
    }

    /**
     * Retorna a data de expiração com base na data atual
     *
     * @return
     */
    private Date gerarDataExpiracao(){
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * Verifica se um token JWT está expirado
     *
     * @param token
     * @return
     */
    private boolean tokenExpirado(String token){
        Date dataExpiracao = this.getExpirationDateFromToken(token);
        if(dataExpiracao == null){
            return false;
        }
        return dataExpiracao.before(new Date());
    }


    /**
     * Gera um novo token JWT contendo os dados (claims) fornecidos
     *
     * @param claims
     * @return
     */
    private String gerarToken(Map<String, Object> claims){
        return Jwts.builder().setClaims(claims).setExpiration(gerarDataExpiracao())
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

}
