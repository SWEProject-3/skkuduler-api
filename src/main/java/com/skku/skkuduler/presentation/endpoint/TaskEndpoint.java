package com.skku.skkuduler.presentation.endpoint;

import com.skku.skkuduler.application.TaskService;
import com.skku.skkuduler.dto.response.TaskDetail;
import com.skku.skkuduler.dto.request.TaskPostRequest;
import com.skku.skkuduler.dto.response.TaskPostResponse;
import com.skku.skkuduler.presentation.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskEndpoint {

    private final TaskService taskService;

//    @PostMapping
//    public ApiResponse<TaskPostResponse> createTask(@RequestBody List<TaskPostRequest> taskPostRequest) {
//        int insertedTasks = taskService.addAll(taskPostRequest);
//        TaskPostResponse resp = new TaskPostResponse();
//        resp.setInsertedRecords(insertedTasks);
//
//        return new ApiResponse<>(resp);
//    }
//
//    @GetMapping
//    public ApiResponse<List<TaskDetail>> tasks() {
//        return new ApiResponse<>(taskService.fetchAll());
//    }
//
//    @GetMapping("/{user}")
//    public ApiResponse<List<TaskDetail>> tasks(@PathVariable String user) {
//        return new ApiResponse<>(taskService.fetchByCreatedBy(user));
//    }
//
//    @GetMapping("/subset")
//    public ApiResponse<List<TaskDetail>> subset(@RequestParam(defaultValue="title") String orderBy,
//                       @RequestParam(defaultValue = "asc") String direction,
//                       @RequestParam(defaultValue = "0") int page) {
//        return new ApiResponse<>(taskService.subset(orderBy, direction, page));
//    }
}
