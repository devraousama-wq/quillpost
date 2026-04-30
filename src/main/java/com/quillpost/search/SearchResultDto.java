package com.quillpost.search;

import com.quillpost.content.domain.Post;

import java.util.Locale;

public record SearchResultDto(
    Post post,
    String snippet
) {
    public static SearchResultDto of(Post post, String query) {
        String body = post.getBodyMarkdown() == null ? "" : post.getBodyMarkdown();
        String lower = body.toLowerCase(Locale.ROOT);
        String q = query.toLowerCase(Locale.ROOT);
        int idx = lower.indexOf(q);
        String snippet;
        if (idx >= 0) {
            int start = Math.max(0, idx - 40);
            int end = Math.min(body.length(), idx + q.length() + 40);
            snippet = body.substring(start, end).replace(query, "<<" + query + ">>");
        } else {
            snippet = post.getExcerpt();
        }
        return new SearchResultDto(post, snippet);
    }
}
