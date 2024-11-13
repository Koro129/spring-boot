package wako.Belajar_Spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import wako.Belajar_Spring.entity.User;
import wako.Belajar_Spring.model.CreateTaskRequest;
import wako.Belajar_Spring.model.TaskResponse;
import wako.Belajar_Spring.model.UpdateTaskRequest;
import wako.Belajar_Spring.model.WebResponse;
import wako.Belajar_Spring.service.TaskService;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping(
        path = "/api/tasks",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TaskResponse> create(User user, @RequestBody CreateTaskRequest request) { 
        TaskResponse taskResponse = taskService.create(user, request);
        return WebResponse.<TaskResponse>builder().data(taskResponse).build();
    }

    @GetMapping(
        path = "/api/tasks",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<TaskResponse>> list(User user) {
        List<TaskResponse> taskResponses = taskService.listAll(user);
        return WebResponse.<List<TaskResponse>>builder().data(taskResponses).build();
    }

    @PutMapping(
        path = "/api/tasks/{taskId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TaskResponse> update(User user, 
                                            @RequestBody UpdateTaskRequest request, 
                                            @PathVariable("taskId") String taskId) {
        request.setId(taskId);

        TaskResponse taskResponse = taskService.update(user, request);
        return WebResponse.<TaskResponse>builder().data(taskResponse).build();
    }

    @DeleteMapping(
        path = "/api/tasks/{taskId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(User user, @PathVariable("taskId") String taskId) {
        taskService.delete(user, taskId);
        return WebResponse.<String>builder().data("OK").build();
    }
}
