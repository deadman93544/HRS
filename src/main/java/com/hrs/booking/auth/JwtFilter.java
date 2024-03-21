package com.hrs.booking.auth;

import com.hrs.booking.entity.CurrentUser;
import com.hrs.booking.service.UserService;
import com.hrs.booking.util.EncoderDecoder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// A UsernamePasswordAuthenticationFilter Filter on APIs to Securely Authorize / deny them gracefully before letting them enter the Controllers.
public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Value("${hrs.auth.apiprefix}")
    private String apiprefix;                    // A Prefix which is used as starting endpoint of all Authorized APIs

    @Value("${hrs.auth.secretkey}")
    private String secretkey;                   // Secret Key for JWT Tokens Signing

    @Autowired
    private UserService userService;

    @Autowired
    private AuthCacheProvider authCache;

    @Autowired
    private EncoderDecoder encoderDecoder;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretkey.getBytes());
    }

    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (!request.getRequestURI().startsWith(apiprefix)) {
//            The URI does not contains the Prefix for Authorized Endpoint, Passing them.
            chain.doFilter(req, res);
            return;
        }

        // Fetching Authorization from Header
        String authHeader = request.getHeader("Authorization");
        final String tokenParam = request.getParameter("token");

        if (StringUtils.isEmpty(authHeader) && !StringUtils.isEmpty(tokenParam)) {
            authHeader = tokenParam;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            If Token not found or Token wasn't bearer, Setting request as UNAUTHORIZED
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        final String token = authHeader.substring(7); // The part after "Bearer "

        try {
            Claims claims;
            try {
//                Fething Claims from the token.
                claims = Jwts.parser().setSigningKey(key).parseClaimsJws(encoderDecoder.decryptUsingSecretKey(token)).getBody();
            } catch (IllegalArgumentException | JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            request.setAttribute("claims", claims);

//            Fetching Authentication from Auth Cache for the token, If Present.
            Authentication result = authCache.getAuthCache().getIfPresent(token);

            if (result == null || ((JwtAuthenticationToken) result).isStale()) {
                logger.debug("User {} not found in cache", claims.getSubject());

                CurrentUser user;

                if (claims.getSubject() != null) {
                    try {
//                        Authentication for User not found in Auth Cache,
//                        Checked that the Authentication wasn't stale. i.e. token wasn't expired.
//                        Fetching the User details from the DB using claims subject...
                        user = (CurrentUser) userService.loadUserByUsername(claims.getSubject());
                    } catch (UsernameNotFoundException e) {
                        logger.debug(e.getMessage());
                        user = null;
                    }

                    if (user == null || user.getUser() == null) {
//                        No User found in the DB, setting request as UNAUTHORIZED
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }

//                    Creating User Authorities for Spring Security Authentication Super class.
                    Set<String> userPrivileges = user.getUser().getPrivileges();
                    List<GrantedAuthority> authorities = userPrivileges.stream().map(up -> new SimpleGrantedAuthority("ROLE_" + up)).collect(Collectors.toList());

                    result = new JwtAuthenticationToken(
                            user,
                            claims.getSubject(),
                            authorities);
//                    Adding the Created New Authentication using token passed in request to Auth Cache.
                    authCache.getAuthCache().put(token, result);
                }
            }
//            Adding the Authentication in Spring Security Context for further filter...
            SecurityContextHolder.getContext().setAuthentication(result);

        } catch (JwtException e) {
            logger.error("Error parsing token", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(req, res);
    }

}
