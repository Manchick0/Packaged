# Packaged
Simplify HTTP requests, the way they should be.

Java's verbosity often feels cumbersome, especially when it comes to tasks so essential, you'd expect them to be perfect. One such case is **HTTP requests**. Most of the time, your task is as simple as fetching some data from the web. Don't you wish the process were simpler? **Packaged** aims to abstract out all the complexity of HTTP requests by providing convenient methods that not only feel modern but also help your workflow.

## Why Packaged?
Packaged was made specifically to reduce the boilerplate code you have to write and bring you straight to the point instead.
- **Async-To-Go**: Asynchronious by design. Keep your code responsive while waiting for the server to respond.
- **0 Boilerplate**: Forget the tedious setup of `HTTPConnection` for basic tasks.
- **JS-Inspired Syntax**: Call a single method and instantly get the response.

> [!NOTE]
> Packaged is by no means a competitor the the `HTTPClient` class. It instead tries to simplify what already should be simple.

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
