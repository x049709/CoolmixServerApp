package com.tellem.microservices.api.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.tellem.microservices.api.user.UserApiServiceApplication;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserApiServiceApplication.class)
@WebAppConfiguration
public class UserApiServiceApplicationTests {

	@Test
	public void contextLoads() {
	}

}
