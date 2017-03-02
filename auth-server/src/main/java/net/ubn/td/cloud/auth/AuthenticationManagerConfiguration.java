package net.ubn.td.cloud.auth;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.ubn.td.cloud.auth.dto.AccountDTO;

@Configuration
public class AuthenticationManagerConfiguration extends GlobalAuthenticationConfigurerAdapter {
	 @Autowired
	 private Environment env;
	 
	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> builder = auth.inMemoryAuthentication();

		String json = FileUtils.readFileToString(new File(env.getProperty("auth.data.path")), "UTF-8");
		ObjectMapper mapper = new ObjectMapper();
		
		AccountDTO[] dataList = mapper.readValue(json.replaceAll("'", "\""), AccountDTO[].class);
		for(AccountDTO data:dataList){
			System.out.println("data="+data.getStudentNumber());
			builder.withUser(data.getStudentNumber()).password(data.getPassword()).roles("USER");
		}

		builder.withUser("min").password("min").roles("USER").and().withUser("cs").password("password").roles("USER");
		builder.withUser("CSLin").password("1").roles("USER");
	}

}
