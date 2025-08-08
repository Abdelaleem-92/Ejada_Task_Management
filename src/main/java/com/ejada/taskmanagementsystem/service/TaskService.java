package com.ejada.taskmanagementsystem.service;

import com.ejada.taskmanagementsystem.controller.TaskController;
import com.ejada.taskmanagementsystem.model.Task;
import com.ejada.taskmanagementsystem.model.User;
import com.ejada.taskmanagementsystem.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Task addTask(Task task, User createdUser) {
        task.setCreatedBy(createdUser);
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updated, User createdUser) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        if (!existing.getCreatedBy().getId().equals(createdUser.getId())) {  // same user who create task, can update it
            throw new RuntimeException("Not authorized");
        }

        if (updated.getTitle() != null) {
            existing.setTitle(updated.getTitle());
        }
        if (updated.getStatus() != null) {
            existing.setStatus(updated.getStatus());
        }
        if (updated.getPriority() != null) {
            existing.setPriority(updated.getPriority());
        }
        existing.setDescription(updated.getDescription());
        return taskRepository.save(existing);
    }

    public void deleteTask(Long id, User createdUser) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        if (!task.getCreatedBy().getId().equals(createdUser.getId())) {  // same user who create task, can update it
            throw new RuntimeException("Not authorized");
        }
        taskRepository.delete(task);
    }

    public List<Task> listAll(Task.Status status, Task.Priority priority, LocalDate dueDate,
                              TaskController.TaskSortBy sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy.getSortBy()).descending() : Sort.by(sortBy.getSortBy()).ascending();

        List<Task> tasks = taskRepository.findAll(sort);
        return tasks.stream()
                .filter(task -> status == null || task.getStatus() == status)
                .filter(task -> priority == null || task.getPriority() == priority)
                .filter(task -> dueDate == null || task.getDueDate().isEqual(dueDate))
                .toList();
    }

    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }
}
