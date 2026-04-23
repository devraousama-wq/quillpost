CREATE TABLE job_execution_log (
    id UUID PRIMARY KEY,
    job_name VARCHAR(64) NOT NULL,
    status VARCHAR(16) NOT NULL,
    started_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    finished_at TIMESTAMPTZ,
    detail TEXT
);

ALTER TABLE users ADD COLUMN digest_enabled BOOLEAN NOT NULL DEFAULT TRUE;
