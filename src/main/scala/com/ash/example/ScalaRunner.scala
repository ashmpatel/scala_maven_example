package com.ash.example

import com.ash.example.storage.BookStorage


object ScalaRunner extends App {
  implicit val books = (new BookStorage()).getBooks
  BooksProcessor.filterByAuthor("Jack London").foreach(b => println(b))
}