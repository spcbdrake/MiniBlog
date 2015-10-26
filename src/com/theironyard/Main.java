package com.theironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        User user = new User();
        ArrayList<Post> posts = new ArrayList();
        Spark.staticFileLocation("/public");
        Spark.init();
        Spark.post(
                "/create-user",
                ((request, response) -> {
                    user.name = request.queryParams("username");
                    response.redirect("/posts");
                    return "";
                })
        );
        Spark.get(
                "/posts",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    m.put("post", posts);
                    m.put("name", user.name);
                    return new ModelAndView(m, "post.html");
                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/create-post",
                ((request, response) -> {
                    Post post = new Post();
                    post.post = request.queryParams("post");
                    posts.add(post);
                    response.redirect("/posts");
                    return "";
                })
        );
    }
}
