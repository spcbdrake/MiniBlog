package com.theironyard;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        ArrayList<Post> posts = new ArrayList();
        Spark.staticFileLocation("/public");
        Spark.init();
        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String username = request.queryParams("username");
                    Session session = request.session();
                    session.attribute("username", username);
                    response.redirect("/");
                    return "";
                })
        );
        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    if (username == null) {
                        return new ModelAndView(new HashMap(), "index.html");
                    }
                        HashMap m = new HashMap();
                        m.put("post", posts);
                        m.put("name", username);
                        return new ModelAndView(m, "post.html");
                    }),
                            new MustacheTemplateEngine()
                    );
        Spark.post(
            "/create-post",
            ((request, response) -> {
                Post post = new Post();
                post.id = posts.size() +1;
                post.post = request.queryParams("post");
                posts.add(post);
                response.redirect("/");
                return "";
            })
        );
        Spark.post(
                "/delete-post",
                ((request, response) -> {
                    String id = request.queryParams("postid");
                    try {
                        int idNum = Integer.valueOf(id);
                        posts.remove(idNum-1);
                        for (int i = 0; i < posts.size(); i ++) {
                            posts.get(i).id = i +1;
                        }
                    } catch (Exception e) {

                    }
                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/edit-post",
                ((request, response) -> {
                    String id = request.queryParams("postid");
                    String editPost= request.queryParams("editpost");
                    try {
                        int idNum = Integer.valueOf(id);
                        posts.get(idNum-1).post = editPost;
                    }
                    catch (Exception e) {
                    }
                    response.redirect("/");
                    return "";
                })

        );
    }
}
