CREATE TABLE workspace_settings (
    workspace_id UUID PRIMARY KEY REFERENCES workspaces(id),
    theme_name VARCHAR(120) NOT NULL DEFAULT 'default',
    primary_color VARCHAR(32) NOT NULL DEFAULT '#2d6a4f',
    background_color VARCHAR(32) NOT NULL DEFAULT '#faf9f7',
    comments_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    digest_enabled BOOLEAN NOT NULL DEFAULT TRUE
);
