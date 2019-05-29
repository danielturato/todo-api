package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.models.Todo;

public interface TodoDao {
    void add(Todo todo);
    void update(Todo todo);
    void delete(Todo todo);
}
