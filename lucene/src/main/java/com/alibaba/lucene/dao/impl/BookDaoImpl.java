package com.alibaba.lucene.dao.impl;

import com.alibaba.lucene.dao.BookDao;
import com.alibaba.lucene.po.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements BookDao {
    @Override
    public List<Book> findAll() {
        //存储书籍
        List<Book> bookList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement prep = null;
        ResultSet res = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/spring", "root", "root");
            prep = connection.prepareStatement("select * from book");
            res = prep.executeQuery();
            while (res.next()) {

            Book book = new Book();
                book.setId(res.getInt("id"));
                book.setBookName(res.getString("bookname"));
                book.setBookDesc(res.getString("bookdesc"));
                book.setPic(res.getString("pic"));
                book.setPrice(res.getFloat("price"));
                bookList.add(book);
            }
            return bookList;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
                if (prep != null) {
                    prep.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        return null;
    }
}
