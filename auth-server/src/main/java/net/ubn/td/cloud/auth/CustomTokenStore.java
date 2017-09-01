package net.ubn.td.cloud.auth;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * 
 * 新 Token 儲存
 * >> getAccessToken >> storeAccessToken >> storeRefreshToken
 * <p>
 * 更新 Token
 * >> readRefreshToken >> readAuthenticationForRefreshToken >> removeAccessTokenUsingRefreshToken >> storeAccessToken
 * <p>
 *  參考：http://samchu.logdown.com/posts/1433379
 *  我們的用意是想讓每次存擋access_token都不一樣
 */
public class CustomTokenStore extends InMemoryTokenStore {
	private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

	private AtomicInteger flushCounter = new AtomicInteger(0);
	

	@Override
	public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
		String key = authenticationKeyGenerator.extractKey(authentication);
		return super.getAccessToken(authentication);
	}
}
