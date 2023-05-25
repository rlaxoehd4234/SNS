package com.ffsns.sns.conguration.filter;

import com.ffsns.sns.model.User;
import com.ffsns.sns.service.UserService;
import com.ffsns.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String key;
    private final UserService userService;

    // filter 에서 username 을 가져오기 위한 과정
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // header
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(header == null || !header.startsWith("Bearer ")){
            log.error("Error occurs while getting header. header is null or invalid");
            filterChain.doFilter(request, response);
            return;
        }

        try{
            final String token = header.split(" ")[1].trim();
            //TODO : check the token is valid
            if(JwtTokenUtils.isExpired(token , key)){
                log.error("key is Expired");
                filterChain.doFilter(request, response);
                return;
            }


            // TODO : get username from token
            String userName = JwtTokenUtils.getUserName(token,key);
            User user = userService.loadUserByUserName(userName);
            //TODO: check the username is valid
            if (!JwtTokenUtils.validate(token, user.getUsername(), key)) {
                filterChain.doFilter(request, response);
                return;
            }


            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }catch (RuntimeException e){

            log.error("Error occurs while validating. {}" , e.toString());
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);


    }
}
