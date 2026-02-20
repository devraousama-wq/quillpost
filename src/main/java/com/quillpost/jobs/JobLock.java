package com.quillpost.jobs;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "job_locks")
public class JobLock {

    @Id
    @Column(length = 64)
    private String lockName;

    @Column(nullable = false)
    private Instant lockedUntil;

    protected JobLock() {
    }

    public JobLock(String lockName, Instant lockedUntil) {
        this.lockName = lockName;
        this.lockedUntil = lockedUntil;
    }

    public String getLockName() {
        return lockName;
    }

    public Instant getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(Instant lockedUntil) {
        this.lockedUntil = lockedUntil;
    }
}
