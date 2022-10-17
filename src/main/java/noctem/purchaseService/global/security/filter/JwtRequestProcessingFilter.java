package noctem.purchaseService.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fusionauth.jwt.InvalidJWTException;
import io.fusionauth.jwt.InvalidJWTSignatureException;
import io.fusionauth.jwt.JWTExpiredException;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACVerifier;
import lombok.extern.slf4j.Slf4j;
import noctem.purchaseService.global.common.CommonResponse;
import noctem.purchaseService.global.enumeration.Role;
import noctem.purchaseService.global.security.auth.UserDetailsImpl;
import noctem.purchaseService.global.security.dto.ClientInfoDto;
import noctem.purchaseService.global.security.token.JwtAuthenticationToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class JwtRequestProcessingFilter extends AbstractAuthenticationProcessingFilter {
    public JwtRequestProcessingFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (req.getHeader(JwtAuthenticationToken.JWT_HEADER) == null) {
            chain.doFilter(req, res);
        } else {
            try {
                String encodedJwt = req.getHeader(JwtAuthenticationToken.JWT_HEADER).split(" ")[1];
                Verifier verifier = HMACVerifier.newVerifier(JwtAuthenticationToken.JWT_SIGNER);
                JWT decodedJwt = JWT.getDecoder().decode(encodedJwt, verifier);

                Map<String, Object> allClaims = decodedJwt.getAllClaims();

                if (!allClaims.get("iss").toString().equals(JwtAuthenticationToken.JWT_ISSUER)) {
                    throw new InvalidJWTSignatureException();
                }

                ClientInfoDto clientInfoDto = ClientInfoDto.builder().build();
                // store token일 경우
                if (allClaims.get(JwtAuthenticationToken.JWT_ROLE).toString().equals(Role.ROLE_STORE.toString())) {
                    clientInfoDto.setStoreAccountId(Long.parseLong(allClaims.get(JwtAuthenticationToken.JWT_STORE_ACCOUNT_ID).toString()));
                    clientInfoDto.setStoreId(Long.parseLong(allClaims.get(JwtAuthenticationToken.JWT_STORE_ID).toString()));
                    clientInfoDto.setRole(Role.ROLE_STORE);
                }
                // user token일 경우
                if (allClaims.get(JwtAuthenticationToken.JWT_ROLE).toString().equals(Role.ROLE_USER.toString())) {
                    clientInfoDto.setUserAccountId(Long.parseLong(allClaims.get(JwtAuthenticationToken.JWT_USER_ACCOUNT_ID).toString()));
                    clientInfoDto.setNickname(allClaims.get(JwtAuthenticationToken.JWT_NICKNAME).toString());
                    clientInfoDto.setEmail(allClaims.get(JwtAuthenticationToken.JWT_EMAIL).toString());
                    clientInfoDto.setRole(Role.ROLE_USER);
                }

                List<GrantedAuthority> roles = new ArrayList<>();
                roles.add(new SimpleGrantedAuthority(clientInfoDto.getRole().toString()));

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(new JwtAuthenticationToken(new UserDetailsImpl(clientInfoDto, roles)));
                SecurityContextHolder.setContext(context);
                chain.doFilter(request, response);
            } catch (NullPointerException exception) {
                log.warn("NPE, Method: {}, Request URL: {}", exception.getClass(), req.getMethod(), req.getRequestURL());
                log.warn("NPE: {}", exception.getMessage());
            } catch (JWTExpiredException exception) {
                log.warn("Expired Token, Method: {}, Request URL: {}", exception.getClass(), req.getMethod(), req.getRequestURL());
                log.warn("Expired Token, class: {}", exception.getClass());
                // 만료된 토큰
                // refresh token 요청
                // refresh token 인증 -> access token 재발급
                // <재발급 코드>
                // refresh token도 만료 -> 로그인 페이지 이동
                ObjectMapper objectMapper = new ObjectMapper();
                res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                res.setStatus(HttpStatus.UNAUTHORIZED.value());
                objectMapper.writeValue(res.getWriter(),
                        CommonResponse.builder().errorCode(2018).build());
            } catch (InvalidJWTSignatureException exception) {
                log.warn("Invalid Signature Token, Method: {}, Request URL: {}", exception.getClass(), req.getMethod(), req.getRequestURL());
                log.warn("Invalid Signature Token, class: {}", exception.getClass());
                chain.doFilter(req, res);
            } catch (InvalidJWTException exception) {
                log.warn("Invalid Token, Method: {}, Request URL: {}", exception.getClass(), req.getMethod(), req.getRequestURL());
                log.warn("Invalid Token, class: {}", exception.getClass());
                chain.doFilter(req, res);
            } catch (ArrayIndexOutOfBoundsException exception) {
                log.warn("Invalid Token, Method: {}, Request URL: {}", exception.getClass(), req.getMethod(), req.getRequestURL());
                log.warn("Invalid Token, class: {}", exception.getClass());
                chain.doFilter(req, res);
            } catch (Exception exception) {
                log.error("JwtRequestException, Method: {}, Request URL: {}", exception.getClass(), req.getMethod(), req.getRequestURL());
                log.error("Message: {}, class: {}", exception.getMessage(), exception.getClass());
                chain.doFilter(req, res);
            }
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        return null;
    }
}
