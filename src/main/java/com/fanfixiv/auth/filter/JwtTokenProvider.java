package com.fanfixiv.auth.filter;

import com.fanfixiv.auth.interfaces.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.tokenvalidtime}")
  private Long tokenValidTime;

  @Value("${jwt.refresh.tokenvalidtime}")
  private Long refreshtokenValidTime;

  private final UserDetailsService userDetailsService;

  @PostConstruct
  public void init() {
    secret = Base64.getEncoder().encodeToString(secret.getBytes());
  }

  public String createToken(Long userPk, UserRoleEnum roles) {
    Claims claims = Jwts.claims().setSubject(userPk.toString());
    claims.put("roles", roles);
    Date now = new Date();

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + tokenValidTime))
        .signWith(SignatureAlgorithm.HS256, secret)
        .compact();
  }

  public String createTokenWithInVailedToken(String token) {
    Long pk = -1L;
    UserRoleEnum role = UserRoleEnum.USER;
    try {
      this.getUserPk(token);
    } catch (ExpiredJwtException e) {
      pk = Long.valueOf(e.getClaims().getSubject());
      role = UserRoleEnum.valueOf(e.getClaims().get("roles", String.class));
    }

    return createToken(pk, role);
  }

  public String createRefreshToken() {
    Date now = new Date();

    return Jwts.builder()
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + refreshtokenValidTime))
        .signWith(SignatureAlgorithm.HS256, secret)
        .compact();
  }

  public ResponseCookie createRefreshTokenCookie(String refresh) {
    ResponseCookie cookie =
        ResponseCookie.from("refreshToken", refresh)
            .maxAge(7 * 24 * 60 * 60)
            .path("/")
            .secure(true)
            .sameSite("None")
            .httpOnly(true)
            .build();

    return cookie;
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUserPk(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
  }

  public String getRoles(String token) {
    return (String)
        Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("roles");
  }

  public String resolveToken(ServletRequest request) {
    return ((HttpServletRequest) request).getHeader("Authorization");
  }

  public String resolveRefreshToken(ServletRequest request) {
    Cookie[] cookies = ((HttpServletRequest) request).getCookies();
    if (cookies == null) return null;
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("refreshToken")) return cookie.getValue();
    }
    return null;
  }

  public boolean validateToken(String jwtToken) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken);
      return !claims.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }
}
