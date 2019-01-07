package com.alibaba.lucene.dao;

import com.alibaba.lucene.po.Book;

import java.util.List;

public interface BookDao {
    List<Book> findAll();
}
