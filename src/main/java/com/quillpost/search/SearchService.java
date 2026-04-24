package com.quillpost.search;

import com.quillpost.content.domain.Post;
import com.quillpost.content.domain.PostStatus;
import com.quillpost.content.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class SearchService {

    private final PostRepository posts;

    public SearchService(PostRepository posts) {
        this.posts = posts;
    }

    public List<Post> search(UUID workspaceId, String query) {
        return searchResults(workspaceId, query).stream().map(SearchResultDto::post).toList();
    }

    public List<SearchResultDto> searchResults(UUID workspaceId, String query) {
        String q = query.toLowerCase(Locale.ROOT);
        return posts.findByWorkspaceIdAndStatusOrderByUpdatedAtDesc(workspaceId, PostStatus.PUBLISHED).stream()
            .filter(p -> p.getTitle().toLowerCase(Locale.ROOT).contains(q)
                || (p.getBodyMarkdown() != null && p.getBodyMarkdown().toLowerCase(Locale.ROOT).contains(q)))
            .sorted(Comparator.comparingInt(p -> titleBoost(p, q)))
            .map(p -> SearchResultDto.of(p, query))
            .toList();
    }

    private int titleBoost(Post post, String query) {
        return post.getTitle().toLowerCase(Locale.ROOT).contains(query) ? 0 : 1;
    }
}
