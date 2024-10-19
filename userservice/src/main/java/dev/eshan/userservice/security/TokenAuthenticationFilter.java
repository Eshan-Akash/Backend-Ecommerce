package dev.eshan.userservice.security;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import dev.eshan.userservice.models.Permissions;
import dev.eshan.userservice.security.users.AuthUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

@Slf4j
public class TokenAuthenticationFilter extends GenericFilterBean {

    private static final String AUTHORIZATION = "Authorization";
    public static final String BEARER_PREFIX = "Bearer";
    public static final String API_USER_ID = "apiUser";
    private final String key;

    TokenAuthenticationFilter(String key) {
        this.key = key;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        log.info("Inside :: TokenAuthenticationFilter -- doFilter");
        List<GrantedAuthority> apiAuthorityList = AuthorityUtils.createAuthorityList(Permissions.API_USER.name(),
                Permissions.PARTNER_ACTION.name());

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String token = request.getHeader(AUTHORIZATION);
        if (!Strings.isNullOrEmpty(token)) {
            token = StringUtils.removeStart(token, BEARER_PREFIX).trim();
            if (key.equals(token)) {
                AuthUserDetails userDetails = new AuthUserDetails(API_USER_ID, "", apiAuthorityList,
                        Lists.newArrayList());
                Authentication authentication = new PreAuthenticatedAuthenticationToken(userDetails, "",
                        apiAuthorityList);
                log.info("Authenticated {}", authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}