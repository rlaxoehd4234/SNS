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
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String key;
    private final UserService userService;
    private final static List<String> TOKEN_IN_PARAM_URLS = List.of("/api/v1/users/alarm/subscribe");

    // filter 에서 username 을 가져오기 위한 과정
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String token;

        // header



        try{
            if(TOKEN_IN_PARAM_URLS.contains(request.getRequestURI())){
                log.info("Request with {} check the query param", request.getRequestURI());
                token = request.getQueryString().split("=")[1].trim();
            }else{
                final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
                if(header == null || !header.startsWith("Bearer ")){
                    log.error("Error occurs while getting header. header is null or invalid");
                    filterChain.doFilter(request, response);
                    return;
                }
                 token = header.split(" ")[1].trim();
            }



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
