package com.quillpost.jobs;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "job_execution_log")
public class JobExecutionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 64)
    private String jobName;

    @Column(nullable = false, length = 16)
    private String status;

    @Column(nullable = false)
    private Instant startedAt = Instant.now();

    @Column
    private Instant finishedAt;

    @Column(columnDefinition = "text")
    private String detail;

    protected JobExecutionLog() {
    }

    public JobExecutionLog(String jobName, String status, String detail) {
        this.jobName = jobName;
        this.status = status;
        this.detail = detail;
    }

    public UUID getId() {
        return id;
    }

    public String getJobName() {
        return jobName;
    }

    public String getStatus() {
        return status;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    public String getDetail() {
        return detail;
    }
}
