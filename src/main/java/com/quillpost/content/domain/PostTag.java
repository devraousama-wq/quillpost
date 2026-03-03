package com.quillpost.content.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "post_tags")
@IdClass(PostTag.PostTagId.class)
public class PostTag {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    protected PostTag() {
    }

    public PostTag(Post post, Tag tag) {
        this.post = post;
        this.tag = tag;
    }

    public Post getPost() {
        return post;
    }

    public Tag getTag() {
        return tag;
    }

    public static class PostTagId implements Serializable {
        private UUID post;
        private UUID tag;

        public PostTagId() {
        }

        public PostTagId(UUID post, UUID tag) {
            this.post = post;
            this.tag = tag;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PostTagId that)) return false;
            return post.equals(that.post) && tag.equals(that.tag);
        }

        @Override
        public int hashCode() {
            return post.hashCode() * 31 + tag.hashCode();
        }
    }
}
