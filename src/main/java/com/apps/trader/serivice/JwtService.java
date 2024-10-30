package com.apps.trader.serivice;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${secret_key}")
    private String SECRET_KEY;

     @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public String extractUsername(String jwtToken){
        return extractClaim(jwtToken,Claims::getSubject);
    }
    private <T>T extractClaim(String jwtToken,Function<Claims,T> claimsResolver){
      Claims claims = this.extractAllClaims(jwtToken);
      return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts
        .parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(jwtToken)
        .getBody();
    }


    private Key getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    

    public Boolean isTokenValid(String jwtToken,String userEmail){
     Boolean isTokenExpired = isTokenExpired(jwtToken);
       return !isTokenExpired && userEmail.equals(extractUsername(jwtToken));
    }
    private Boolean isTokenExpired(String jtwToken){
        Date token = extractExpiration(jtwToken);
        return token.before(new Date());
    }
    private Date extractExpiration(String jwtToken){
        return extractClaim(jwtToken,Claims::getExpiration);
    }


    public String generateToken(Authentication authentication){
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roles = populateAuthorities(authorities);
         return Jwts
          .builder()
          .claim("authorities",roles)
          .claim("email",authentication.getName())
          .setIssuedAt(new Date(System.currentTimeMillis()))
          .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
          .signWith(getSigningKey(),SignatureAlgorithm.HS256)
          .compact();
    }
         
    

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities ){
        Set<String> auth = new HashSet<>();

        for(GrantedAuthority ga:authorities){
            auth.add(ga.getAuthority());
        }

        return String.join(",",auth);
    }
   
    public String extractEmailJwt(String jwt) {
        Claims claims = extractAllClaims(jwt);
        String userEmail = String.valueOf(claims.get("email"));
        return userEmail;

    }

      public List<GrantedAuthority> extractAuthorityJwt(String jwt) {
        Claims claims = extractAllClaims(jwt);
          String authorites = String.valueOf(claims.get("authorites"));

        return  AuthorityUtils.commaSeparatedStringToAuthorityList(authorites);

    }

    
}
