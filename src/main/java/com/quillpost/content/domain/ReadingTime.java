package com.quillpost.content.domain;

public record ReadingTime(int minutes) {

    public ReadingTime {
        if (minutes < 0) {
            throw new IllegalArgumentException("minutes cannot be negative");
        }
    }

    public static ReadingTime fromWordCount(int words) {
        int minutes = Math.max(1, (int) Math.ceil(words / 200.0));
        return new ReadingTime(minutes);
    }
}
