package com.quillpost.shared.seed;

import com.quillpost.content.domain.Post;
import com.quillpost.content.domain.PostStatus;
import com.quillpost.content.domain.RoleType;
import com.quillpost.content.domain.UserAccount;
import com.quillpost.content.domain.Workspace;
import com.quillpost.content.domain.WorkspaceMembership;
import com.quillpost.content.repository.PostRepository;
import com.quillpost.content.repository.UserAccountRepository;
import com.quillpost.content.repository.WorkspaceMembershipRepository;
import com.quillpost.content.repository.WorkspaceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("seed")
public class SeedDataConfig {

    @Bean
    CommandLineRunner seedDemoWorkspace(
        WorkspaceRepository workspaces,
        UserAccountRepository users,
        WorkspaceMembershipRepository memberships,
        PostRepository posts) {
        return args -> {
            if (workspaces.findBySlug("demo").isPresent()) {
                return;
            }
            UserAccount owner = users.findByEmailIgnoreCase("owner@demo.local")
                .orElseGet(() -> users.save(new UserAccount("owner@demo.local", "Demo Owner")));
            Workspace workspace = workspaces.save(new Workspace("demo", "Demo Publication"));
            memberships.save(new WorkspaceMembership(workspace, owner, RoleType.OWNER));
            Post welcome = new Post(workspace, owner, "Welcome to Quillpost", "welcome-to-quillpost");
            welcome.setBodyMarkdown("# Welcome\n\nThis is a seeded demo post.");
            welcome.setExcerpt("This is a seeded demo post.");
            welcome.setStatus(PostStatus.PUBLISHED);
            welcome.setReadingTimeMinutes(1);
            posts.save(welcome);
        };
    }
}
