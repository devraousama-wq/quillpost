package com.quillpost.feeds;

import com.quillpost.content.domain.Post;
import com.quillpost.content.domain.PostStatus;
import com.quillpost.content.repository.PostRepository;
import com.quillpost.content.repository.WorkspaceRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class FeedService {

    private final WorkspaceRepository workspaces;
    private final PostRepository posts;

    public FeedService(WorkspaceRepository workspaces, PostRepository posts) {
        this.workspaces = workspaces;
        this.posts = posts;
    }

    public String rss(String workspaceSlug) {
        UUID workspaceId = workspaces.findBySlug(workspaceSlug).orElseThrow().getId();
        List<Post> published = posts.findByWorkspaceIdAndStatusOrderByUpdatedAtDesc(workspaceId, PostStatus.PUBLISHED);
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<rss version=\"2.0\"><channel>");
        xml.append("<title>").append(escape(workspaceSlug)).append("</title>");
        for (Post post : published) {
            xml.append("<item>");
            xml.append("<title>").append(escape(post.getTitle())).append("</title>");
            xml.append("<link>/w/").append(workspaceSlug).append("/p/").append(post.getSlug()).append("</link>");
            if (post.getPublishAt() != null) {
                xml.append("<pubDate>").append(DateTimeFormatter.RFC_1123_DATE_TIME.format(post.getPublishAt())).append("</pubDate>");
            }
            xml.append("<description>").append(escape(post.getExcerpt())).append("</description>");
            xml.append("</item>");
        }
        xml.append("</channel></rss>");
        return xml.toString();
    }

    public String sitemap(String workspaceSlug) {
        UUID workspaceId = workspaces.findBySlug(workspaceSlug).orElseThrow().getId();
        List<Post> published = posts.findByWorkspaceIdAndStatusOrderByUpdatedAtDesc(workspaceId, PostStatus.PUBLISHED);
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        for (Post post : published) {
            xml.append("<url><loc>/w/").append(workspaceSlug).append("/p/").append(post.getSlug()).append("</loc></url>");
        }
        xml.append("</urlset>");
        return xml.toString();
    }

    public MediaType rssMediaType() {
        return MediaType.APPLICATION_RSS_XML;
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
