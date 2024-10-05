package com.skku.skkuduler.application;

import com.skku.skkuduler.domain.Task;
import com.skku.skkuduler.dto.response.TaskDetail;
import com.skku.skkuduler.dto.request.TaskPostRequest;
import com.skku.skkuduler.infrastructure.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public int addAll(List<TaskPostRequest> taskPostRequest){
        List<Task> tasks = taskPostRequest.stream().map(taskRequest -> {
            Task en = new Task();
            en.setCreatedBy(taskRequest.getCreatedBy());
            en.setTitle(taskRequest.getTitle());
            en.setPriority(taskRequest.getPriority());
            en.setDescription(taskRequest.getDescription());
            return en;
        }).collect(Collectors.toList());

        return taskRepository.saveAll(tasks).size();
    }

    public List<TaskDetail> fetchAll() {
        List<Task> allTasks = taskRepository.findAll();

        return allTasks.stream()
                .map(entity->
                        new TaskDetail(
                                entity.getTitle(),
                                entity.getPriority(),
                                entity.getCreatedBy(),
                                entity.getDescription()))
                .collect(Collectors.toList());
    }

    public List<TaskDetail> fetchByCreatedBy(String user) {
        List<Task> tasksByUser = taskRepository.findAllByCreatedBy(user);

        return tasksByUser.stream()
                .map(entity->
                        new TaskDetail(
                                entity.getTitle(),
                                entity.getPriority(),
                                entity.getCreatedBy(),
                                entity.getDescription()))
                .collect(Collectors.toList());
    }

    public List<TaskDetail> subset(String orderBy, String direction, int page) {
        Pageable pageable = PageRequest.of(page, 3, Sort.by(Sort.Direction.valueOf(direction.toUpperCase()), orderBy));

        return taskRepository.findAll(pageable).stream()
                .map(entity->
                        new TaskDetail(
                                entity.getTitle(),
                                entity.getPriority(),
                                entity.getCreatedBy(),
                                entity.getDescription()))
                .collect(Collectors.toList());
    }
}