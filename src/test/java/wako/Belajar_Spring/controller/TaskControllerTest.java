package wako.Belajar_Spring.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.x.protobuf.MysqlxCrud;

import wako.Belajar_Spring.entity.Task;
import wako.Belajar_Spring.entity.User;
import wako.Belajar_Spring.model.CreateTaskRequest;
import wako.Belajar_Spring.model.TaskResponse;
import wako.Belajar_Spring.model.UpdateTaskRequest;
import wako.Belajar_Spring.model.WebResponse;
import wako.Belajar_Spring.repository.TaskRepository;
import wako.Belajar_Spring.repository.UserRepository;
import wako.Belajar_Spring.security.BCrypt;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("username");
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setName("testing");
        user.setToken("token");
        user.setTokenExpiredAt(System.currentTimeMillis() + 3600 * 1000);
        userRepository.save(user);
    }

    @Test
    void createTaskWrongToken() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setName("testing");

        mockMvc.perform(
            post("/api/tasks")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("X-API-TOKEN", "invalid-token")
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {

                });
                assertNotNull(response.getErrors());
        });
    }

    @Test
    void createTaskSuccess() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setName("testing");

        mockMvc.perform(
            post("/api/tasks")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "token")
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<TaskResponse> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {

                });
                assertNull(response.getErrors());
                assertEquals("testing", response.getData().getName());
                assertEquals(false, response.getData().getIsCompleted());

                assertTrue(taskRepository.existsById(response.getData().getId()));
        });
    }

    @Test
    void listAllTaskWrongToken() throws Exception {
        for (int i = 0; i < 5; i++) {
            Task task = new Task();
            task.setId("test-" + i);
            task.setName("testing-" + i);
            task.setIsCompleted(false);
            task.setUser(userRepository.findFirstByToken("token").get());
            taskRepository.save(task);
        }

        mockMvc.perform(
            get("/api/tasks")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "invalid-token")
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {

                });
                assertNotNull(response.getErrors());
        });
    }

    @Test
    void listAllTaskSuccess() throws Exception {
        for (int i = 0; i < 5; i++) {
            Task task = new Task();
            task.setId("test-" + i);
            task.setName("testing-" + i);
            task.setIsCompleted(false);
            task.setUser(userRepository.findFirstByToken("token").get());
            taskRepository.save(task);
        }

        mockMvc.perform(
            get("/api/tasks")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "token")
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<List<TaskResponse>> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {

                });
                assertNull(response.getErrors());
                assertEquals(5, response.getData().size());
        });
    }

    @Test
    void updateTaskWrongToken() throws Exception {
        Task task = new Task();
        task.setId("test");
        task.setName("testing");
        task.setIsCompleted(false);
        task.setUser(userRepository.findFirstByToken("token").get());
        taskRepository.save(task);

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setIsCompleted(true);
        
        mockMvc.perform(
            put("/api/tasks/test")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "invalid-token")
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {

                });
                assertNotNull(response.getErrors());
        });
    }

    @Test
    void UpdateTaskSuccess() throws Exception {
        Task task = new Task();
        task.setId("test");
        task.setName("testing");
        task.setIsCompleted(false);
        task.setUser(userRepository.findFirstByToken("token").get());
        taskRepository.save(task);

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setName("testing");
        request.setIsCompleted(true);
        
        mockMvc.perform(
            put("/api/tasks/test")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "token")
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<TaskResponse> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {

                });
                assertNull(response.getErrors());
                assertEquals("test", response.getData().getId());
                assertEquals("testing", response.getData().getName());
                assertEquals(true, response.getData().getIsCompleted());
        });
    }

    @Test
    void deleteTaskWrongToken() throws Exception {
        Task task = new Task();
        task.setId("test");
        task.setName("testing");
        task.setIsCompleted(false);
        task.setUser(userRepository.findFirstByToken("token").get());
        taskRepository.save(task);

        mockMvc.perform(
            delete("/api/tasks/test")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "invalid-token")
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {

                });
                assertNotNull(response.getErrors());
        });
    }

    @Test
    void deleteTaskSuccess() throws Exception {
        Task task = new Task();
        task.setId("test");
        task.setName("testing");
        task.setIsCompleted(false);
        task.setUser(userRepository.findFirstByToken("token").get());
        taskRepository.save(task);

        mockMvc.perform(
            delete("/api/tasks/test")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-TOKEN", "token")
        ).andExpectAll(
            status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {

                });
                assertNull(response.getErrors());
                assertTrue(taskRepository.findById("test").isEmpty());
        });
    }
}
