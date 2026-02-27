CREATE TABLE job_locks (
    lock_name VARCHAR(64) PRIMARY KEY,
    locked_until TIMESTAMPTZ NOT NULL
);
