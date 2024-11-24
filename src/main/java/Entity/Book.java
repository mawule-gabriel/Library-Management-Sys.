package Entity;

import Entity.Enums.BookStatus;

public class Book {

    private int bookId;
    private String title;
    private String author;
    private int publicationYear;
    private String genre;
    private BookStatus status;
    private String isbn;

    public Book(int bookId, String title, String author, int publicationYear, String genre, BookStatus status, String isbn){
        this.bookId = bookId;
        this.title = title;
        this.author =author;
        this.publicationYear =publicationYear;
        this.genre = genre;
        this.status = BookStatus.valueOf(String.valueOf(status));
        this.isbn = isbn;
    }

    //Getters and Setters

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId){
        this.bookId = bookId;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getAuthor(){
        return author;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public int getPublicationYear(){
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear){
        this.publicationYear = publicationYear;
    }

    public String getGenre(){
        return genre;
    }

    public void setGenre(String genre){
        this.genre = genre;
    }

    public BookStatus getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = BookStatus.valueOf(status);
    }

    public String getIsbn(){
        return isbn;
    }

    public void setIsbn(String isbn){
        this.isbn =isbn;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publicationYear=" + publicationYear +
                ", genre='" + genre + '\'' +
                ", status='" + status + '\'' +
                ", isbn='" + isbn + '\'' +
                '}';

    }

}

