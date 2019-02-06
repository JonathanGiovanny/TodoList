package com.todo;

import static org.junit.Assert.assertEquals;

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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpTasksRequestTest {

	@LocalServerPort
	private int port;
	private String BASE_URL;

	private final int CACHE_SIZE = 50;

	private ObjectMapper mapper;

	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void installEnvironment() {
		BASE_URL = "http://localhost:" + port + "/todo/tasks";

		for (int i = 0; i < CACHE_SIZE; i++) {
			Task t = new Task();
			t.setTask(Integer.toHexString((i + 16) % 256));
			this.restTemplate.withBasicAuth("user1", "user1Pass").postForEntity(BASE_URL, buildHeader(t), String.class);
		}

		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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
	public void insertOneRecord() throws Exception {
		Task t = new Task();
		t.setTask("Something1");

		ResponseEntity<String> result = this.restTemplate.withBasicAuth("user1", "user1Pass").postForEntity(BASE_URL,
				buildHeader(t), String.class);

		// Assert that the call was successful
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	public void getOneRecord() throws Exception {
		ResponseEntity<String> result = this.restTemplate.withBasicAuth("user1", "user1Pass")
				.getForEntity(BASE_URL + "/2", String.class);

		// Assert that we got a result
		assertEquals(HttpStatus.OK, result.getStatusCode());

		// Assert the value of the result
		Task t = mapper.readValue(result.getBody(), Task.class);
		assertEquals("11", t.getTask());
	}

	@Test
	public void getAllRecords() throws Exception {
		// Get all the created values
		final String pagination = "?page=0&size=" + CACHE_SIZE;
		ResponseEntity<String> result = this.restTemplate.withBasicAuth("user1", "user1Pass").getForEntity(BASE_URL + pagination,
				String.class);
		
		// Assert that we got a result
		assertEquals(HttpStatus.OK, result.getStatusCode());
		
		// Assert the amount of the values to be the cache size (All)
		Task[] lt = mapper.readValue(result.getBody(), Task[].class);
		assertEquals(CACHE_SIZE, lt.length);
	}

	@Test
	public void getAllRecordsPaginated() throws Exception {
		final int page = 1;
		final int size = 5;
		final String pagination = "?page=" + page + "&size=" + size;
		ResponseEntity<String> result = this.restTemplate.withBasicAuth("user1", "user1Pass").getForEntity(BASE_URL + pagination,
				String.class);

		// Assert that we got a result
		assertEquals(HttpStatus.OK, result.getStatusCode());

		// Assert the amount of the values to be the cache size (All)
		Task[] lt = mapper.readValue(result.getBody(), Task[].class);
		for (int i = page * size, j = 0; i < size; i++, j++) {
			assertEquals(new Long(i), lt[j].getId());
		}
	}

	@Test
	public void updateOneRecord() throws Exception {
		final String record = "/1";

		// Get a record
		ResponseEntity<String> getResult = this.restTemplate.withBasicAuth("user1", "user1Pass")
				.getForEntity(BASE_URL + record, String.class);

		assertEquals(HttpStatus.OK, getResult.getStatusCode());

		Task t = mapper.readValue(getResult.getBody(), Task.class);
		assertEquals("10", t.getTask());

		t.setTask("Something");

		// Modify the record
		this.restTemplate.withBasicAuth("user1", "user1Pass").put(BASE_URL + record, t);

		// Get the record again from the service
		ResponseEntity<String> getAfterResult = this.restTemplate.withBasicAuth("user1", "user1Pass")
				.getForEntity(BASE_URL + record, String.class);

		assertEquals(HttpStatus.OK, getAfterResult.getStatusCode());

		Task tu = mapper.readValue(getAfterResult.getBody(), Task.class);

		// Assert that the new value is equal to the sent one
		assertEquals(t.getTask(), tu.getTask());
	}

	@Test
	public void validateCreationDateIsNotModified() throws Exception {
		final String record = "/3";

		// Get a record
		ResponseEntity<String> getResult = this.restTemplate.withBasicAuth("user1", "user1Pass")
				.getForEntity(BASE_URL + record, String.class);

		assertEquals(HttpStatus.OK, getResult.getStatusCode());

		Task t = mapper.readValue(getResult.getBody(), Task.class);
		assertEquals("12", t.getTask());

		t.setTask("Something");

		// Modify the record
		this.restTemplate.withBasicAuth("user1", "user1Pass").put(BASE_URL + record, t);

		// Get the record again from the service
		ResponseEntity<String> getAfterResult = this.restTemplate.withBasicAuth("user1", "user1Pass")
				.getForEntity(BASE_URL + record, String.class);

		assertEquals(HttpStatus.OK, getAfterResult.getStatusCode());

		Task tu = mapper.readValue(getAfterResult.getBody(), Task.class);

		// Assert that the new value is equal to the sent one
		assertEquals(t.getCreationDate(), tu.getCreationDate());
	}
}
