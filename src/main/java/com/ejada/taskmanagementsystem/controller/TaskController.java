package com.ejada.taskmanagementsystem.controller;

import com.ejada.taskmanagementsystem.model.Task;
import com.ejada.taskmanagementsystem.model.User;
import com.ejada.taskmanagementsystem.security.UserPrincipal;
import com.ejada.taskmanagementsystem.service.TaskService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> add(@RequestBody Task task, @AuthenticationPrincipal UserPrincipal principal) {
        User user = principal.getUser();
        if (user.getRole() != User.Role.ADMIN)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied - insufficient permissions");
        Task created = taskService.addTask(task, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping()
    public ResponseEntity<List<Task>> getTasks( @RequestParam(required = false) Task.Status status,
                                                @RequestParam(required = false) Task.Priority priority,
                                                @RequestParam(required = false) LocalDate dueDate,
                                                @RequestParam(required = false, defaultValue = "dueDate") TaskSortBy sortBy,
                                                @RequestParam(required = false, defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(taskService.listAll(status, priority, dueDate, sortBy, sortDir));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody Task task,
                                          @AuthenticationPrincipal UserPrincipal principal) {
        User user = principal.getUser();
        if (user.getRole() != User.Role.ADMIN)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied - insufficient permissions");
        return ResponseEntity.ok(taskService.updateTask(id, task, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {
        User user = principal.getUser();
        if (user.getRole() != User.Role.ADMIN)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied - insufficient permissions");
        taskService.deleteTask(id, user);
        return ResponseEntity.noContent().build();
    }

    @Getter
    public enum TaskSortBy {
        title("title"),
        dueDate("dueDate"),
        priority("priority"),
        status("status");

        private final String sortBy;

        TaskSortBy(String sortBy) {
            this.sortBy = sortBy;
        }

    }

}
