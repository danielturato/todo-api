package com.teamtreehouse.todos.dao;

import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.models.Todo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

import static org.junit.Assert.*;

public class Sql2oTodoDaoTest {

    private Sql2oTodoDao dao;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        dao = new Sql2oTodoDao(sql2o);
        conn = sql2o.open();
    }

    @Test
    public void addNewTodo() throws Exception {
        Todo todo = newTestTodo();
        int originalId = todo.getId();
        dao.add(todo);

        assertNotEquals(originalId, todo.getId());
    }

    @Test
    public void updateATodo() throws Exception {
        Todo todo = newTestTodo();
        String orginalName = todo.getName();
        dao.add(todo);
        todo.setName("changed");
        dao.update(todo);
        Todo updated = dao.findById(todo.getId());

        assertNotEquals(orginalName, updated.getName());
    }

    @Test
    public void todoGetsDeleted() {
        Todo todo = newTestTodo();
        dao.add(todo);
        dao.delete(todo.getId());

        Todo todo1 = dao.findById(todo.getId());

        assertNull(todo1);
    }

    @Test
    public void getAllTodos() throws Exception {
        Todo todo = newTestTodo();
        Todo todo2 = new Todo("Water the plants", false);
        dao.add(todo);
        dao.add(todo2);

        List<Todo> todos = dao.findAll();
        assertEquals(2, todos.size());
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    private Todo newTestTodo() {
        return new Todo("Feed the dog", false);
    }
}