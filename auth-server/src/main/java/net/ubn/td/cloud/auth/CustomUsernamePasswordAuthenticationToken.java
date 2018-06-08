package net.ubn.td.cloud.auth;

import net.ubn.td.cloud.auth.jsonserver.dto.AccountType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final String className;
    private final AccountType type;


    public CustomUsernamePasswordAuthenticationToken(Object principal, Object credentials,String className,AccountType type) {
        super(principal, credentials);
        this.className=className;
        this.type=type;
    }

    public CustomUsernamePasswordAuthenticationToken(Object principal, Object credentials,String className,AccountType type, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.className=className;
        this.type=type;
    }

    public AccountType getType() {
        return type;
    }

    public String getClassName() {
        return className;
    }
}
