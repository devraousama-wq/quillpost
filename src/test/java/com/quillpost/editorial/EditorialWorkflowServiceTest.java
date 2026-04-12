package com.quillpost.editorial;

import com.quillpost.content.domain.Post;
import com.quillpost.content.domain.PostStatus;
import com.quillpost.content.domain.UserAccount;
import com.quillpost.content.domain.Workspace;
import com.quillpost.content.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EditorialWorkflowServiceTest {

    @Mock
    PostRepository posts;

    @InjectMocks
    EditorialWorkflowService workflowService;

    Workspace workspace;
    UserAccount author;
    Post post;

    @BeforeEach
    void setUp() {
        workspace = new Workspace("demo", "Demo");
        author = new UserAccount("a@b.com", "Author");
        post = new Post(workspace, author, "T", "t");
        post.setStatus(PostStatus.DRAFT);
    }

    @Test
    void submitMovesToReview() {
        when(posts.findById(any())).thenReturn(Optional.of(post));
        when(posts.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Post updated = workflowService.submitForReview(UUID.randomUUID(), UUID.randomUUID());
        assertEquals(PostStatus.IN_REVIEW, updated.getStatus());
    }
}
