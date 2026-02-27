package com.quillpost.jobs;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JobLockService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public boolean tryAcquire(String lockName, int ttlSeconds) {
        Instant now = Instant.now();
        JobLock existing = entityManager.find(JobLock.class, lockName);
        if (existing == null) {
            entityManager.persist(new JobLock(lockName, now.plus(ttlSeconds, ChronoUnit.SECONDS)));
            return true;
        }
        if (existing.getLockedUntil().isAfter(now)) {
            return false;
        }
        existing.setLockedUntil(now.plus(ttlSeconds, ChronoUnit.SECONDS));
        entityManager.merge(existing);
        return true;
    }
}
