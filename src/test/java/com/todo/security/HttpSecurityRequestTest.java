package com.todo.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.todo.model.Task;
import com.todo.model.security.User;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpSecurityRequestTest {

	@LocalServerPort
	private int port;
	private final String URL_TASK = "/todo/tasks";
	private final String URL_REGISTER_USER = "/register/user";
	private String BASE_URL;

	private final int CACHE_SIZE = 50;
	private User user;

	private ObjectMapper mapper;

	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void installEnvironment() {
		BASE_URL = "http://localhost:" + port;

		// Create records for Task
		for (int i = 0; i < CACHE_SIZE; i++) {
			Task t = new Task();
			t.setTask(Integer.toHexString((i + 16) % 256));
			this.restTemplate.withBasicAuth("admin", "admin").postForEntity(BASE_URL + URL_TASK, buildHeader(t), String.class);
		}

		// Create records for 
		user = new User();
		user.setUsername("user");
		user.setPassword("pass");
		this.restTemplate.postForEntity(BASE_URL + URL_REGISTER_USER, buildHeader(user), String.class);

		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	/**
	 * Will build the header for the specified User object
	 * 
	 * @param user
	 * @return
	 */
	private HttpEntity<User> buildHeader(User user) {
		HttpHeaders headers = new HttpHeaders();
		return new HttpEntity<>(user, headers);
	}

	/**
	 * Will build the header for the specified Task object
	 * 
	 * @param task
	 * @return
	 */
	private HttpEntity<Task> buildHeader(Task task) {
		HttpHeaders headers = new HttpHeaders();
		return new HttpEntity<>(task, headers);
	}

	@Test
	public void noExistingUser() throws Exception {
		User u = new User();
		u.setUsername("user2");
		u.setPassword("pass2");

		ResponseEntity<String> result = this.restTemplate.withBasicAuth(u.getUsername(), u.getPassword()).getForEntity(BASE_URL + URL_TASK, String.class);

		// Assert that the call was UN-successful
		assertNotEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	public void registerUser() throws Exception {
		User u = new User();
		u.setUsername("user0");
		u.setPassword("pass0");

		try {
			ResponseEntity<String> result = this.restTemplate.postForEntity(BASE_URL + URL_REGISTER_USER, buildHeader(u), String.class);

			// Assert that the call was successful
			assertEquals(HttpStatus.OK, result.getStatusCode());
		} catch (RuntimeException re) {
			// Error creating user
			assertEquals(0, 1);
		}
	}

	@Test
	public void reRegisterUser() throws Exception {
		User u = new User();
		u.setUsername("user1");
		u.setPassword("pass1");

		ResponseEntity<String> result = this.restTemplate.postForEntity(BASE_URL + URL_REGISTER_USER, buildHeader(u), String.class);
		// Assert that the call was successful
		assertEquals(HttpStatus.OK, result.getStatusCode());
	
		try {
			ResponseEntity<String> result2 = this.restTemplate.postForEntity(BASE_URL + URL_REGISTER_USER, buildHeader(u), String.class);
	
			// Assert that the call was UN-successful
			assertNotEquals(HttpStatus.OK, result2.getStatusCode());
		} catch (RuntimeException re) {
			// Error creating user
			assertEquals(0, 1);
		}

	}

	@Test
	public void updateData_WithoutPermission() throws Exception {
		final String record = "/4";

		// Get a record
		ResponseEntity<String> getResult = this.restTemplate.withBasicAuth(user.getUsername(), user.getPassword())
				.getForEntity(BASE_URL + URL_TASK + record, String.class);

		assertEquals(HttpStatus.OK, getResult.getStatusCode());

		Task t = mapper.readValue(getResult.getBody(), Task.class);
		assertEquals("13", t.getTask());

		t.setTask("Something13");

		// Modify the record
		this.restTemplate.withBasicAuth(user.getUsername(), user.getPassword()).put(BASE_URL + URL_TASK + record, t);

		// Get the record again from the service
		ResponseEntity<String> getAfterResult = this.restTemplate.withBasicAuth(user.getUsername(), user.getPassword())
				.getForEntity(BASE_URL + URL_TASK + record, String.class);

		assertEquals(HttpStatus.OK, getAfterResult.getStatusCode());

		Task tu = mapper.readValue(getAfterResult.getBody(), Task.class);

		// Assert that the new value is still the same
		assertEquals("13", tu.getTask());
	}

	@Test
	public void updateData_WithPermission() throws Exception {
		final String record = "/5";

		// Get a record
		ResponseEntity<String> getResult = this.restTemplate.withBasicAuth(user.getUsername(), user.getPassword())
				.getForEntity(BASE_URL + URL_TASK + record, String.class);

		assertEquals(HttpStatus.OK, getResult.getStatusCode());

		Task t = mapper.readValue(getResult.getBody(), Task.class);
		assertEquals("14", t.getTask());

		t.setTask("Something14");

		// Modify the record
		this.restTemplate.withBasicAuth("admin", "admin").put(BASE_URL + URL_TASK + record, t);

		// Get the record again from the service
		ResponseEntity<String> getAfterResult = this.restTemplate.withBasicAuth(user.getUsername(), user.getPassword())
				.getForEntity(BASE_URL + URL_TASK + record, String.class);

		assertEquals(HttpStatus.OK, getAfterResult.getStatusCode());

		Task tu = mapper.readValue(getAfterResult.getBody(), Task.class);

		// Assert that the new value is still the same
		assertEquals(t.getTask(), tu.getTask());
	}
}
