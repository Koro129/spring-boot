package wako.Belajar_Spring.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import wako.Belajar_Spring.entity.Task;
import wako.Belajar_Spring.entity.User;
import wako.Belajar_Spring.model.CreateTaskRequest;
import wako.Belajar_Spring.model.TaskResponse;
import wako.Belajar_Spring.model.UpdateTaskRequest;
import wako.Belajar_Spring.repository.TaskRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ValidationService validationService;

    private TaskResponse toTaskResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .isCompleted(task.getIsCompleted())
                .build();
    }

    public TaskResponse create(User user, CreateTaskRequest request) {
        validationService.validate(request);
        
        Task task = new Task();
        task.setId(UUID.randomUUID().toString());
        task.setName(request.getName());
        task.setIsCompleted(false);
        task.setUser(user);

        taskRepository.save(task);

        return toTaskResponse(task);

    }

    @Transactional(readOnly = true)
    public List<TaskResponse> listAll(User user) {
        List<Task> tasks = taskRepository.findAllByUser(user);
        return tasks.stream().map(this::toTaskResponse).toList();
    }

    @Transactional
    public TaskResponse update(User user, UpdateTaskRequest request) {
        validationService.validate(request);

        Task task = taskRepository.findByIdAndUser(request.getId(), user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        task.setName(request.getName());
        task.setIsCompleted(request.getIsCompleted());

        taskRepository.save(task);

        return toTaskResponse(task);
    }

    @Transactional
    public void delete(User user, String taskId) {
        Task task = taskRepository.findByIdAndUser(taskId, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        taskRepository.delete(task);
    }
}
