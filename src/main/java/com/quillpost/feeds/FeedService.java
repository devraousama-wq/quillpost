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
        return buildChannelXml(workspaceSlug, "rss", (xml, post) -> {
            xml.append("<item>");
            xml.append("<title>").append(escape(post.getTitle())).append("</title>");
            xml.append("<link>/w/").append(workspaceSlug).append("/p/").append(post.getSlug()).append("</link>");
            appendPubDate(xml, post);
            xml.append("<description>").append(escape(post.getExcerpt())).append("</description>");
            xml.append("</item>");
        });
    }

    public String atom(String workspaceSlug) {
        List<Post> published = publishedPosts(workspaceSlug);
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<feed xmlns=\"http://www.w3.org/2005/Atom\">");
        xml.append("<title>").append(escape(workspaceSlug)).append("</title>");
        xml.append("<id>urn:quillpost:").append(escape(workspaceSlug)).append("</id>");
        for (Post post : published) {
            xml.append("<entry>");
            xml.append("<title>").append(escape(post.getTitle())).append("</title>");
            xml.append("<id>urn:quillpost:").append(post.getId()).append("</id>");
            xml.append("<link href=\"/w/").append(workspaceSlug).append("/p/").append(post.getSlug()).append("\"/>");
            appendAtomUpdated(xml, post);
            xml.append("<summary>").append(escape(post.getExcerpt())).append("</summary>");
            xml.append("</entry>");
        }
        xml.append("</feed>");
        return xml.toString();
    }

    public String jsonFeed(String workspaceSlug) {
        List<Post> published = publishedPosts(workspaceSlug);
        StringBuilder json = new StringBuilder();
        json.append("{\"version\":\"https://jsonfeed.org/version/1.1\",\"title\":\"").append(escapeJson(workspaceSlug)).append("\",\"items\":[");
        for (int i = 0; i < published.size(); i++) {
            Post post = published.get(i);
            if (i > 0) json.append(",");
            json.append("{\"id\":\"").append(post.getId()).append("\",");
            json.append("\"title\":\"").append(escapeJson(post.getTitle())).append("\",");
            json.append("\"url\":\"/w/").append(workspaceSlug).append("/p/").append(post.getSlug()).append("\",");
            json.append("\"summary\":\"").append(escapeJson(post.getExcerpt())).append("\"}");
        }
        json.append("]}");
        return json.toString();
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

    public MediaType atomMediaType() {
        return MediaType.APPLICATION_ATOM_XML;
    }

    public MediaType jsonFeedMediaType() {
        return MediaType.parseMediaType("application/feed+json");
    }

    private String buildChannelXml(String workspaceSlug, String root, ItemAppender appender) {
        List<Post> published = publishedPosts(workspaceSlug);
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<").append(root).append(" version=\"2.0\"><channel>");
        xml.append("<title>").append(escape(workspaceSlug)).append("</title>");
        for (Post post : published) {
            appender.append(xml, post);
        }
        xml.append("</channel></").append(root).append(">");
        return xml.toString();
    }

    private List<Post> publishedPosts(String workspaceSlug) {
        UUID workspaceId = workspaces.findBySlug(workspaceSlug).orElseThrow().getId();
        return posts.findByWorkspaceIdAndStatusOrderByUpdatedAtDesc(workspaceId, PostStatus.PUBLISHED);
    }

    private void appendPubDate(StringBuilder xml, Post post) {
        if (post.getPublishAt() != null) {
            xml.append("<pubDate>").append(DateTimeFormatter.RFC_1123_DATE_TIME.format(post.getPublishAt())).append("</pubDate>");
        }
    }

    private void appendAtomUpdated(StringBuilder xml, Post post) {
        if (post.getUpdatedAt() != null) {
            xml.append("<updated>").append(post.getUpdatedAt()).append("</updated>");
        }
    }

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    @FunctionalInterface
    private interface ItemAppender {
        void append(StringBuilder xml, Post post);
    }
}
