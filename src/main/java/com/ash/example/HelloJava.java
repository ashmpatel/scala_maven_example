package com.ash.example;

import com.ash.example.storage.BookStorage;
import com.ash.example.model.Book;

public class HelloJava {

    public static void main(String[] args) {
        BookStorage storage = new BookStorage();
        storage.getBooks().stream().forEach((Book b) -> System.out.println(b));
    }
}
