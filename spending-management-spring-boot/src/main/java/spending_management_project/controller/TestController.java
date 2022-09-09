package spending_management_project.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import spending_management_project.annotation.CurrentUser;
import spending_management_project.constant.AuthorityConstant;
import spending_management_project.param.JwtParam;
import spending_management_project.param.UserDTO;
import spending_management_project.po.Test;
import spending_management_project.po.User;
import spending_management_project.service.UserService;
import spending_management_project.tools.JwtUtil;
import spending_management_project.vo.JwtVO;
import spending_management_project.vo.Response;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/test")
public class TestController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping(path = "/hello")
    public ResponseEntity<Response> getHello(@CurrentUser UserDetails user) {
//        System.out.println("authentication"+authentication);
//        System.out.println("principal"+principal);
//        System.out.println("current user"+user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Response result = new Response(user, null,"success", HttpStatus.OK, HttpStatus.OK.value());
        System.out.println(result);
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    @PostMapping(path = "/token")
    public ResponseEntity<Response> getTestToken(@RequestBody UserDTO dto) {
        String token = getJWTToken(dto.getUser());
//        Test user = new Test(dto.getUser(), token);
        return new ResponseEntity<>(new Response(null, null,null, HttpStatus.OK, HttpStatus.OK.value()), HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    private JwtVO login(@RequestBody JwtParam jwtParam) throws Exception {
        System.out.printf("param " + jwtParam);
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    jwtParam.getUsername(),
                    jwtParam.getPassword()
            );
//            authenticationManager.authenticate(token);
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid Credentials", e);
        }

        final UserDetails userDetails
                = userService.loadUserByUsername(jwtParam.getUsername());

        final String token =
                jwtUtil.generateToken(userDetails);

        return new JwtVO(AuthorityConstant.PREFIX+token);
    }

    @GetMapping(path = "/name")
    public ResponseEntity<Response> getName(@AuthenticationPrincipal User user, Authentication authentication, Principal principal) {
        System.out.println(user);
        System.out.println(authentication.getName());
        System.out.println(principal.getName());
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(new Response(auth, null,null, HttpStatus.OK, HttpStatus.OK.value()), HttpStatus.OK);
    }

    private String getJWTToken(String username) {
        String secretKey = AuthorityConstant.SECRET;
        List<GrantedAuthority> grantedAuthorityList = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorityList.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 6000000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();
        return AuthorityConstant.PREFIX + token;
    }
}
