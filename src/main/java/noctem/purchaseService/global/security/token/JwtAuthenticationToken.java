package noctem.purchaseService.global.security.token;

import noctem.purchaseService.global.security.auth.UserDetailsImpl;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal; // UserDetailsImpl객체 저장
    private Object credentials;
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_SIGNER = System.getenv("NOCTEM_JWT_SIGNER");
    public static final String JWT_ISSUER = "Cafe Noctem";
    public static final String JWT_USER_ACCOUNT_ID = "userAccountId";
    public static final String JWT_NICKNAME = "nickname";
    public static final String JWT_EMAIL = "email";
    public static final String JWT_STORE_ACCOUNT_ID = "storeAccountId";
    public static final String JWT_STORE_ID = "storeId";
    public static final String JWT_ROLE = "role";
    public static final String JWT_LOGIN_DTTM = "loginDateTime";

    public JwtAuthenticationToken(UserDetailsImpl userDetails) {
        super(userDetails.getAuthorities());
        this.principal = userDetails;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
