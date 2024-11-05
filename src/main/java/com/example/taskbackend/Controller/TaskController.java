package com.example.taskbackend.Controller;

import com.example.taskbackend.Entity.Task;
import com.example.taskbackend.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class TaskController {
    @Autowired
    private TaskService taskService;

    // Fetch tasks for a specific user
    @GetMapping("/user/{userId}")
    public List<Task> getTasksByUser(@PathVariable String userId) {
        return taskService.getTasksByUser(userId);
    }

    // Add a new task
    @PostMapping
    public Task addTask(@RequestBody Task task) {
        return taskService.saveTask(task);
    }

    // Update task priority
    @PatchMapping("/{taskId}/priority")
    public Task updateTaskPriority(@PathVariable Long taskId, @RequestBody Task updatedTask) {
        return taskService.getTaskById(taskId).map(task -> {
          task.setPriority(updatedTask.getPriority());
            return taskService.updateTask(task);
        }).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    // Update task text
    @PatchMapping("/{taskId}/text")
    public Task updateTaskText(@PathVariable Long taskId, @RequestBody Task updatedTask) {
        return taskService.getTaskById(taskId).map(task -> {
            task.setText(updatedTask.getText());
            return taskService.updateTask(task);
        }).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    // Update task reminder
    @PatchMapping("/{taskId}/reminder")
    public Task updateTaskReminder(@PathVariable Long taskId, @RequestBody Task updatedTask) {
        return taskService.getTaskById(taskId).map(task -> {
            task.setReminderTime(updatedTask.getReminderTime());
            return taskService.updateTask(task);
        }).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    // Delete a task
    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }

    // Fetch notifications for tasks with reminders due
    @GetMapping("/notifications/{userId}")
    public List<Task> getNotifications(@PathVariable String userId) {
        List<Task> tasks = taskService.getTasksByUser(userId);
        return tasks.stream()
                .filter(task -> task.getReminderTime() != null && task.getReminderTime().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
    }
}

