package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.models.Todo;

import java.util.List;

public interface TodoDao {
    void add(Todo todo);
    void update(Todo todo);
    void delete(int id);
    Todo findById(int id);
    List<Todo> findAll();
}
