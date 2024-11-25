# Packaged
Simplify HTTP requests, the way they should be.

Java's verbosity often feels cumbersome, especially when it comes to tasks so essential, that you'd expect them to be perfect. One such case is **HTTP requests**. Most of the time, your task is as simple as fetching some data from the web. Don't you wish the process were simpler? **Packaged** aims to abstract out all the complexity of HTTP requests by providing convenient methods that not only feel modern but also help your workflow.

## Why Packaged?
Packaged was made specifically to reduce the boilerplate code you have to write and bring you straight to the point instead.
- **Async-To-Go**: Asynchronous by design - keep your code running while waiting for the server to respond.
- **0 Boilerplate**: Forget the tedious setup of `HTTPConnection` for basic tasks.
- **JS-Inspired Syntax**: Call a single method and instantly get the response.

> [!NOTE]
> Packaged is by no means a competitor to the `HTTPClient` class. Instead, it tries to simplify what already should be simple.

## Fetching

As previously mentioned, Packaged keeps all its functionality within a single, yet **overloaded** method. `Packaged.fetch()` that is. We wouldn't argue that the **GET** request is by far the most common one. Packaged counts on that and provides a convenient overload. Calling `fetch()` without a provided `Request` defaults to the `Request.GET` one.

```java
public static void main(String[] args) {
    var response = Packaged.fetch("https://example.com").join();
    if (response.ok) {
        System.out.println(response.text());
    }
}
``` 

> [!IMPORTANT]
> `fetch()` returns a `CompletableFuture` of type `Response`. You'd probably want to **await** the response by calling the `join()` method on the `CompletableFuture`.

Just like that we send a **GET** request to [a website](https://example.com), await the result, and if the response is `ok`, print it to the console. Simple, yet effective!

The `Response#text` method we used returns the body of the response as a literal string. As you can already tell, this is **not** something you'd want to do often. You'd rather want to deserialize the body into some sort of object that you can work with. Luckily, `Response` has us covered with its `parse` method. It takes in a `Function` of type `<String, T>` and returns whatever the function does (`T`). This is similar to the `json` method in JS, but a lot more generic due to the absence of a built-in JSON library in Java. You can however use a third-party library, such as [Gson](https://github.com/google/gson), to deserialize the string.

```java
public static void main(String[] args) {
    var response = Packaged.fetch("https://jsonplaceholder.typicode.com/todos/1").join();
    if (response.ok) {
        var mapped = response.parse(body -> new Gson().fromJson(body, Map.class));
        System.out.println(mapped);
    }
}
```

## Using Requests

While the `GET` method is undoubtedly the most essential one, Packaged allows you to use any other one. Let's take a look at how you might send a more advanced request, such as a `POST` one.

The main concept behind constructing requests is using the `Request.Builder` class. Remember how I quickly mentioned that although only one `fetch` method exists, it's overloaded (And **a lot** in fact). Well, it's time to take a look at its other two overloads - one takes in a `Request` object, the other one is more of a utility one - it takes in a `Request.Builder` and immediately delegates to the `Request` overload, by calling the `build` method on the builder. What it essentially means is that you don't need to call the `build` method yourself if you're constructing the request in-line.

> [!NOTE]
> Creating the `Builder` itself is also a bit different from how it's usually done. You can call any method of the `Builder` class on the `Request` class itself, which will then return a `Builder`. (e.g. calling `Request.method();` is the same as calling `new Request.Builder().method();`, though the second option wouldn't work since the constructor is private.)

```java
public static void main(String[] args) {
    var entry = """
            {
                "title": "Updated title",
                "completed": true
            }
            """;
    var response = Packaged.fetch("https://jsonplaceholder.typicode.com/posts/", Request.method("POST").json(entry)).join();
    if (response.ok) {
        var json = response.parse(body -> new Gson().fromJson(body, Map.class));
        System.out.println(json);
    }
}
```

You might have noticed that we're using the `json` method to set the body of the request. Well, the `json` method is actually an overload of the `body` one that not only sets the body but also adds the appropriate `Content-Type` header. There are other overloads for other content types as well, so feel free to reach out to the `xml`, `text`, or maybe even use the raw `body` method.

To add a header to your request, simply use the `header` method or any of its overloads.

## Working with the response

While you're already familiar with the `text` and `parse` methods of the `Response` class, it's worth taking a brief look at other methods and fields. The most important ones are the `status` and the `ok` fields. The status code directly represents the status code of the response (e.g. 200, 404). The `ok` field is a shorthand for checking whether the status code falls anywhere between 2XX and 3XX, hence whether the response was successful.

```java
public static void main(String[] args) {
    var response = Packaged.fetch("https://jsonplaceholder.typicode.com/todos/1", Request.method("GET").acceptJson()).join();
    if (response.ok) {
        var json = response.parse(body -> new Gson().fromJson(body, Map.class));
        System.out.println(json);
    } else {
        System.err.printf("%s: %s%n", response.status, response.statusText);
    }
}
```
