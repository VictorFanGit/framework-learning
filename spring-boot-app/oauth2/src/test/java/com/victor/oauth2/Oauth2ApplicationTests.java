package com.victor.oauth2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class Oauth2ApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void test(){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encode = encoder.encode("123");
		System.out.println(encode);
	}

}
