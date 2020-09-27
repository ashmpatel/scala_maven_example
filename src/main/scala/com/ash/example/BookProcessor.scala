package com.ash.example

import java.util

import com.ash.example.model.Book

import collection.JavaConverters._
import scala.collection.mutable

object BooksProcessor {

  def filterByAuthor(author: String)(implicit books: util.List[Book]):mutable.Buffer[Book] = {
    books.asScala.filter(bk => bk.getAuthor == author)
  }

}
