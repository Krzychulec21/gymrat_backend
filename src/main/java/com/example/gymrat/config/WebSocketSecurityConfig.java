package com.example.gymrat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WebSocketSecurityConfig implements HandshakeInterceptor {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        String query = request.getURI().getQuery();
        String token = null;

        if (query != null) {
            Map<String, String> queryParams = Arrays.stream(query.split("&"))
                    .map(param -> param.split("=", 2))
                    .filter(kv -> kv.length == 2)
                    .collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));

            token = queryParams.get("token");
        }

        if (token != null) {
            String userEmail = jwtService.extractEmail(token);
            System.out.println("problem z emailem: "+ userEmail);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(token, userDetails)) {
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                attributes.put("user", auth);
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
    }
}
