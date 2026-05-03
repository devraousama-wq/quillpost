package com.quillpost.jobs;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class JobExecutionLogService {

    private final JobExecutionLogRepository repository;

    public JobExecutionLogService(JobExecutionLogRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public JobExecutionLog start(String jobName, String detail) {
        return repository.save(new JobExecutionLog(jobName, "RUNNING", detail));
    }

    @Transactional
    public void finish(JobExecutionLog log, String status) {
        log.setFinishedAt(Instant.now());
        repository.save(new JobExecutionLog(log.getJobName(), status, log.getDetail()));
    }

    public List<JobExecutionLog> recent() {
        return repository.findTop50ByOrderByStartedAtDesc();
    }
}
