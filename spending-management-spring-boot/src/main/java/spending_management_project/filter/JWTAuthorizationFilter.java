package spending_management_project.filter;

import io.jsonwebtoken.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import spending_management_project.constant.AuthorityConstant;
import spending_management_project.po.User;
import spending_management_project.service.UserService;
import spending_management_project.tools.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    private final String HEADER = AuthorityConstant.HEADER;
    private final String PREFIX = AuthorityConstant.PREFIX;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
//        try {
//            if (checkJWTToken(httpServletRequest, httpServletResponse)) {
//                Claims claims = validateToken(httpServletRequest);
//                if (claims.get("authorities") != null) {
//                    setUpSpringAuthentication(claims);
//                } else {
//                    SecurityContextHolder.clearContext();
//                }
//            } else {
//                SecurityContextHolder.clearContext();
//            }
//            filterChain.doFilter(httpServletRequest, httpServletResponse);
//        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
//            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            ((HttpServletResponse) httpServletResponse).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
//            return;
//        }
        try {
            final String authorizationHeader = request.getHeader(HEADER);

            String username = null;
            String jwtToken = null;
            if(checkJWTToken(request,response)) {
                jwtToken = authorizationHeader.replace(PREFIX, "");
                username = jwtUtil.getUsernameFromToken(jwtToken);
            }
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                User userDetails = this.userService.loadUserByUsername(username);
                if(jwtUtil.validateToken(jwtToken, userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void setUpSpringAuthentication(Claims claims) {

        List<String> authorities = (List<String>) claims.get("authorities");
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private Claims validateToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        String SECRET = AuthorityConstant.SECRET;
        return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
    }

    private boolean checkJWTToken(HttpServletRequest request, HttpServletResponse response) {
        String authenticationHeader = request.getHeader(HEADER);
        return authenticationHeader != null && authenticationHeader.startsWith(PREFIX);
    }
}
