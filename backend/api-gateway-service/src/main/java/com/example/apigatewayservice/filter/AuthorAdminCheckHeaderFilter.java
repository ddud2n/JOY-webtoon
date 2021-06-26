package com.example.apigatewayservice.filter;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorAdminCheckHeaderFilter extends AbstractGatewayFilterFactory<AuthorAdminCheckHeaderFilter.Config> {

    Environment env;
    public AuthorAdminCheckHeaderFilter(Environment env){
        super(Config.class);
        this.env = env;
    }

    private static final Logger logger = LoggerFactory.getLogger("jwtValidTest");

    public static class Config{

    }

    @Override
    public GatewayFilter apply(Config config){
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                return onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.substring(7, authorizationHeader.length());

            if(!isJwtValid(jwt)){ //jwt admin author 확인
                return onError(exchange, "Not Author properities", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        });
    }


    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(err);
        return response.setComplete();
    }


    private boolean isJwtValid(String jwt){
        boolean returnValue = true;
        String key = env.getProperty("token.secret");
        String userAuth = null;
        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
            Long userId = Long.parseLong(claims.get("userId").toString());
            userAuth = claims.get("userAuth").toString();
            System.out.println(userAuth);
        } catch (
            SignatureException ex) {
                logger.error("Invalid JWT signature");
            } catch (
            MalformedJwtException ex) {
                logger.error("Invalid JWT token");
            } catch (
            ExpiredJwtException ex) {
                logger.error("Expired JWT token");
            } catch (UnsupportedJwtException ex) {
                logger.error("Unsupported JWT token");
            } catch (IllegalArgumentException ex) {
                logger.error("JWT claims string is empty.");
            }
        if(userAuth.isEmpty() || !(userAuth.contains("ROLE_AUTHOR")||userAuth.contains("ROLE_ADMIN")) ){returnValue= false;}
        return returnValue;
    }



}
