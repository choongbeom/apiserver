package com.cbkim.apiserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

//import lombok.extern.slf4j.Slf4j;


//@Slf4j
@SpringBootTest( 
	properties = { "testID=1234", "testName=test" },
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
 )
@AutoConfigureMockMvc
@Transactional
class ApiserverApplicationTests {

	@Value("${testID}") 
	private String testID; 
	@Value("${testName}") 
	private String testName;

/*
	@Autowired
	private MockMvc mvc;

	@Autowired 
	private TestRestTemplate restTemplate;

	@Autowired 
	private UserService userService; 
	
	@Autowired 
	private WebApplicationContext ctx;


	@Test 
	void getMember() throws Exception 
	{ 
		log.info("##### Properties 테스트 #####"); 		
	}
*/
}
