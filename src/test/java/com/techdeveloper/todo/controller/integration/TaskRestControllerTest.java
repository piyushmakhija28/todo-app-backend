package com.techdeveloper.todo.controller.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.techdeveloper.todo.dto.ApiResponseDto;
import com.techdeveloper.todo.dto.TaskDto;
import com.techdeveloper.todo.form.TaskForm;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles(profiles = "test")
@TestMethodOrder(OrderAnnotation.class)
class TaskRestControllerTest {

	private RestTemplate restTemplate;

	@BeforeEach
	void init() {
		restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new ResponseErrorHandler() {
			
			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				return response.getStatusCode().isError();
			}
			
			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
				
			}
		});
	}

	@Test
	@Order(10)
	void getTaskListApiSuccessTest() throws RestClientException, URISyntaxException {
		ResponseEntity<ApiResponseDto<List<TaskDto>>> responseEntity = getTaskList();
		assertEquals(200, responseEntity.getStatusCode().value());
	}

	private ResponseEntity<ApiResponseDto<List<TaskDto>>> getTaskList() throws URISyntaxException {
		RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.GET,
				new URI("http://localhost:8081/api/v1/tasks"));
		ResponseEntity<ApiResponseDto<List<TaskDto>>> responseEntity = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<ApiResponseDto<List<TaskDto>>>() {
				});
		return responseEntity;
	}
	
	@Test
	@Order(11)
	void getTaskByIdApiSuccessTest() throws RestClientException, URISyntaxException {
		Long id = getTaskList().getBody().getData().get(0).getId();
		RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.GET,
				new URI("http://localhost:8081/api/v1/tasks/"+id));
		ResponseEntity<ApiResponseDto<TaskDto>> responseEntity = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<ApiResponseDto<TaskDto>>() {
				});
		assertEquals(200, responseEntity.getStatusCode().value());
	}
	
	@Test
	@Order(13)
	void getTaskByIdApiFailureTest() throws RestClientException, URISyntaxException {
		RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.GET,
				new URI("http://localhost:8081/api/v1/tasks/0"));
		ResponseEntity<ApiResponseDto<TaskDto>> responseEntity = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<ApiResponseDto<TaskDto>>() {
				});
		assertEquals(404, responseEntity.getStatusCode().value());
	}
	
	@Test
	@Order(12)
	void deleteTaskByIdApiSuccessTest() throws RestClientException, URISyntaxException {
		Long id = getTaskList().getBody().getData().get(0).getId();
		RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.DELETE,
				new URI("http://localhost:8081/api/v1/tasks/"+id));
		ResponseEntity<ApiResponseDto<Void>> responseEntity = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<ApiResponseDto<Void>>() {
				});
		assertEquals(200, responseEntity.getStatusCode().value());
	}

	@Test
	@Order(1)
	void addTaskApiBadRequetForTitleIsNullTest() throws RestClientException, URISyntaxException {
		TaskForm taskForm = new TaskForm();
		taskForm.setDescription("Description Test");
		RequestEntity<TaskForm> requestEntity = new RequestEntity<>(taskForm,HttpMethod.POST,
				new URI("http://localhost:8081/api/v1/tasks"));
		ResponseEntity<ApiResponseDto<Void>> responseEntity = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<ApiResponseDto<Void>>() {
				});
		assertEquals(400, responseEntity.getStatusCode().value());
	}
	
	@Test
	@Order(2)
	void addTaskApiBadRequetForTitleIsEmptyTest() throws RestClientException, URISyntaxException {
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("");
		taskForm.setDescription("Description Test");
		RequestEntity<TaskForm> requestEntity = new RequestEntity<>(taskForm,HttpMethod.POST,
				new URI("http://localhost:8081/api/v1/tasks"));
		ResponseEntity<ApiResponseDto<Void>> responseEntity = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<ApiResponseDto<Void>>() {
				});
		assertEquals(400, responseEntity.getStatusCode().value());
	}
	
	@Test
	@Order(3)
	void addTaskApiBadRequetForTitleIsBlankTest() throws RestClientException, URISyntaxException {
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle(" ");
		taskForm.setDescription("Description Test");
		RequestEntity<TaskForm> requestEntity = new RequestEntity<>(taskForm,HttpMethod.POST,
				new URI("http://localhost:8081/api/v1/tasks"));
		ResponseEntity<ApiResponseDto<Void>> responseEntity = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<ApiResponseDto<Void>>() {
				});
		assertEquals(400, responseEntity.getStatusCode().value());
	}
	
	@Test
	@Order(4)
	void addTaskApiBadRequetForDescriptionIsNullTest() throws RestClientException, URISyntaxException {
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Title Test");
		RequestEntity<TaskForm> requestEntity = new RequestEntity<>(taskForm,HttpMethod.POST,
				new URI("http://localhost:8081/api/v1/tasks"));
		ResponseEntity<ApiResponseDto<Void>> responseEntity = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<ApiResponseDto<Void>>() {
				});
		assertEquals(400, responseEntity.getStatusCode().value());
	}
	
	@Test
	@Order(5)
	void addTaskApiBadRequetForDescriptionIsEmptyTest() throws RestClientException, URISyntaxException {
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Title Test");
		taskForm.setDescription("");
		RequestEntity<TaskForm> requestEntity = new RequestEntity<>(taskForm,HttpMethod.POST,
				new URI("http://localhost:8081/api/v1/tasks"));
		ResponseEntity<ApiResponseDto<Void>> responseEntity = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<ApiResponseDto<Void>>() {
				});
		assertEquals(400, responseEntity.getStatusCode().value());
	}
	
	@Test
	@Order(6)
	void addTaskApiBadRequetForDescriptionIsBlankTest() throws RestClientException, URISyntaxException {
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Title Test");
		taskForm.setDescription(" ");
		RequestEntity<TaskForm> requestEntity = new RequestEntity<>(taskForm,HttpMethod.POST,
				new URI("http://localhost:8081/api/v1/tasks"));
		ResponseEntity<ApiResponseDto<Void>> responseEntity = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<ApiResponseDto<Void>>() {
				});
		assertEquals(400, responseEntity.getStatusCode().value());
	}
	
	@Test
	@Order(7)
	void addTaskApiSuccessTest() throws RestClientException, URISyntaxException {
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Title Test");
		taskForm.setDescription("Description Test");
		RequestEntity<TaskForm> requestEntity = new RequestEntity<>(taskForm,HttpMethod.POST,
				new URI("http://localhost:8081/api/v1/tasks"));
		ResponseEntity<ApiResponseDto<Void>> responseEntity = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<ApiResponseDto<Void>>() {
				});
		assertEquals(201, responseEntity.getStatusCode().value());
	}
	
	@Test
	@Order(8)
	void updateTaskApiNotFoundFailureTest() throws RestClientException, URISyntaxException {
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Title Test");
		taskForm.setDescription("Description Test");
		RequestEntity<TaskForm> requestEntity = new RequestEntity<>(taskForm,HttpMethod.PUT,
				new URI("http://localhost:8081/api/v1/tasks/100"));
		ResponseEntity<ApiResponseDto<Void>> responseEntity = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<ApiResponseDto<Void>>() {
				});
		assertEquals(404, responseEntity.getStatusCode().value());
	}
	
	@Test
	@Order(9)
	void updateTaskApiSuccessTest() throws RestClientException, URISyntaxException {
		TaskForm taskForm = new TaskForm();
		taskForm.setTitle("Title Test");
		taskForm.setDescription("Description Test");
		Long id = getTaskList().getBody().getData().get(0).getId();
		RequestEntity<TaskForm> requestEntity = new RequestEntity<>(taskForm,HttpMethod.PUT,
				new URI("http://localhost:8081/api/v1/tasks/"+id));
		ResponseEntity<ApiResponseDto<Void>> responseEntity = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<ApiResponseDto<Void>>() {
				});
		assertEquals(200, responseEntity.getStatusCode().value());
	}
}
