package data.entities;

import java.util.Objects;

public class Book  {
    private final String author;
    private final String title;
    private final int pages;

    public Book(BookBuilder builder) {
        this.author = builder.author;
        this.title = builder.title;
        this.pages = builder.pages;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public int getPages() {
        return pages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return pages == book.pages && Objects.equals(author, book.author) && Objects.equals(title, book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, title, pages);
    }

    @Override
    public String toString() {
        return "Книга{" +
                "Автор='" + author + '\'' +
                ", Название книги='" + title + '\'' +
                ", Количество страниц=" + pages +
                '}';
    }

    public static class BookBuilder {
        private String author;
        private String title;
        private int pages;

        public BookBuilder setAuthor(String author) {
            this.author = author;
            return this;
        }

        public BookBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public BookBuilder setPages(int pages) {
            this.pages = pages;
            return this;
        }

        public Book build() {
            return new Book(this);
        }
    }
}

