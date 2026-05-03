package com.quillpost.api;

import com.quillpost.jobs.JobExecutionLog;
import com.quillpost.jobs.JobExecutionLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/jobs")
public class JobLogController {

    private final JobExecutionLogService jobExecutionLogService;

    public JobLogController(JobExecutionLogService jobExecutionLogService) {
        this.jobExecutionLogService = jobExecutionLogService;
    }

    @GetMapping("/log")
    public List<JobExecutionLog> recent() {
        return jobExecutionLogService.recent();
    }
}
