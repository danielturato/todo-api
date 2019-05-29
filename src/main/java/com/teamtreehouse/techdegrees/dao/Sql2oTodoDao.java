package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.models.Todo;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oTodoDao implements TodoDao {

    private final Sql2o sql2o;

    public Sql2oTodoDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Todo todo) {
        String sql = "INSERT INTO todos (name, completed) VALUES (:name, :completed)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql)
                    .bind(todo)
                    .executeUpdate()
                    .getKey();

            todo.setId(id);
        } catch (Sql2oException ex) {
            System.exit(0);
        }
    }

    @Override
    public void update(Todo todo) {
        String sql = "UPDATE todos SET name = :name, completed = :completed WHERE id = :id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql).bind(todo).executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
        }
    }

    @Override
    public void delete(Todo todo) {
        String sql = "DELETE FROM todos WHERE id = :id";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql).bind(todo).executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex.getMessage());
            System.exit(0);
        }
    }

    @Override
    public Todo findById(int id) {
        String sql = "SELECT * FROM todos WHERE id = :id";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Todo.class);
        }
    }


    @Override
    public List<Todo> findAll() {
        String sql = "SELECT * FROM todos";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Todo.class);
        }
    }
}
