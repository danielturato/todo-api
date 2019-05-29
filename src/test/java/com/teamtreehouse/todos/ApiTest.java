package com.teamtreehouse.todos;

import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.App;
import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.models.Todo;
import com.teamtreehouse.testing.ApiClient;
import com.teamtreehouse.testing.ApiResponse;
import org.junit.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ApiTest {

    public static final String PORT = "4568";
    public static final String TEST_DATASOURCE = "jdbc:h2:mem:testing";
    private Connection conn;
    private ApiClient client;
    private Gson gson;
    private Sql2oTodoDao todoDao;

    @BeforeClass
    public static void startServer() {
        String[] args = {PORT, TEST_DATASOURCE};
        App.main(args);
    }

    @AfterClass
    public static void stopServer() {
        Spark.stop();
    }

    @Before
    public void setUp() throws Exception {
        Sql2o sql2o = new Sql2o(TEST_DATASOURCE + ";INIT=RUNSCRIPT from 'classpath:db/init.sql'", "", "");
        todoDao = new Sql2oTodoDao(sql2o);
        conn = sql2o.open();
        client = new ApiClient("http://localhost:" + PORT);
        gson = new Gson();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void getAllTodos() throws Exception {
        Todo todo1 = new Todo("Wash dishes", false);
        Todo todo2 = new Todo("Have a shower", false);
        todoDao.add(todo1);
        todoDao.add(todo2);

        ApiResponse res = client.request("GET","/api/v1/todos");
        Todo[] retrieved = gson.fromJson(res.getBody(), Todo[].class);

        assertEquals(2, retrieved.length);
    }

    @Test
    public void createNewTodoAddsToDatabase() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "Wash dishes");
        values.put("completed", false);
        ApiResponse res = client.request("POST", "/api/v1/todos", gson.toJson(values));
        Todo todo = gson.fromJson(res.getBody(), Todo.class);

        assertEquals(1, todo.getId());
    }

    @Test
    public void creatingNewTodoGivesCorrectStatusCode() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "Wash dishes");
        values.put("completed", false);
        ApiResponse res = client.request("POST", "/api/v1/todos", gson.toJson(values));

        assertEquals(201, res.getStatus());
    }

    @Test
    public void todoIsUpdated() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "Wash dishes");
        values.put("completed", false);
        ApiResponse res = client.request("POST", "/api/v1/todos", gson.toJson(values));
        Todo original = gson.fromJson(res.getBody(), Todo.class);
        values.remove("name");
        values.put("name", "Have a shower");
        ApiResponse res2 = client.request("PUT", "/api/v1/todos/1", gson.toJson(values));
        Todo updated = gson.fromJson(res2.getBody(), Todo.class);

        assertNotEquals(original.getName(), updated.getName());
    }

    @Test
    public void todoIsDeleted() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "Wash dishes");
        values.put("completed", false);
        client.request("POST", "/api/v1/todos", gson.toJson(values));
        client.request("DELETE", "/api/v1/todos/1");
        Todo todo = todoDao.findById(1);

        assertNull(todo);
    }

    @Test
    public void todoDeletionGivesCorrectStatusCode() throws Exception {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "Wash dishes");
        values.put("completed", false);
        ApiResponse res = client.request("POST", "/api/v1/todos", gson.toJson(values));
        ApiResponse res2 = client.request("DELETE", "/api/v1/todos/1");

        assertEquals(204, res2.getStatus());
    }
}
