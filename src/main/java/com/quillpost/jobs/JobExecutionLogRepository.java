package com.quillpost.jobs;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JobExecutionLogRepository extends JpaRepository<JobExecutionLog, UUID> {

    List<JobExecutionLog> findTop50ByOrderByStartedAtDesc();
}
