package net.ubn.td.cloud.auth;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import net.ubn.td.cloud.auth.jsonserver.dto.JsonAccountDTO;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private RestTemplate restTemplate = new RestTemplate();

	private Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);
	
	@Value("${jsonserver.domain}")
	private String json_server_domain;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		logger.debug("name:{} password:{}", name, password);
		System.out.println("name:" + name + "password:" + password);
		System.out.println("json_server_domain:"+json_server_domain);
		ResponseEntity<List<JsonAccountDTO>> rateResponse = restTemplate.exchange(
				"http://"+json_server_domain+"/users?studentNumber=" + name, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<JsonAccountDTO>>() {
				});
		List<JsonAccountDTO> accountDTOList = rateResponse.getBody();


		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken= accountDTOList.stream()
				.filter(dto->dto.getPassword().equals(password))
				.findFirst()
				.map(accountDTO->new UsernamePasswordAuthenticationToken(accountDTO.getStudentNumber(), accountDTO.getPassword(), new ArrayList<>()))
				.orElse(null);
		return  usernamePasswordAuthenticationToken;
//		if (accountDTO.getPassword().equals(password)) {
//			return new UsernamePasswordAuthenticationToken(name, password, new ArrayList<>());
//		} else {
//			return null;
//		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
