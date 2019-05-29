package com.teamtreehouse.techdegrees;


import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

public class App {

    public static void main(String[] args) {
        staticFileLocation("/public");

//        get("/api/v1/todos", "application/json", (req, res) -> {
//
//        });

    }

}
