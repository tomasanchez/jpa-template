# T Sanchez's Spark Seed

## Spark

Please, visit Spark [website](https://sparkjava.com/) for documentation about it.
See also its public [repository](https://github.com/perwendel/spark)

Spark - A micro framework for creating web applications in Kotlin and Java 8 with minimal effort

## About this seed

This seed is the result of improving an assigment for __UTN FRBA__, Systems Design Course where it consist of developing a simple web application using an MVC pattern with `Spark Java` and `JPA`. Having [`Bootstrap 5`](https://getbootstrap.com/docs/5.0/getting-started/introduction/) and `HandleBars` for front-end developing.
<br></br>
The seed provides a minimal set up to arrage your project so you can create a web application with minimal effort.
<br></br>
If you are developing the System Design assignment I highly recommend taking a look at this seed, it will provide a good inspiration and stating point.

__DISCLAIMER__: using this seed __DOES NOT__ guarantee passing the assignment, nor developers account for responsibility in case of wrong usage of interfaces provided.

<br></br>

## Getting Started

### There are a lot of folders! Where do I start?v

#### Core 

The core folder represents the fundamental components of this seed. There you will find a `mvc` _package_ where the `Model View Controller` is implemented.


`database` _package_ contains abstract convenience super classes which will help you with the model design.

`services` _package_ includes implementations used during run-time.

![CORE](./assets/seed-core.png)

<br></br>

There is no need for you to worry about the `core` package, _it just works_. Modifyng it may require some understanding of the MVC pattern and the seed itself.

##### `MVC` package

The **`Controller`** class is a generic implementation. Your controllers must inherit this class. By default all controllers are set to the path `/controllername` with the method [`GET`](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods), and if a [`TemplateEngine`]((https://sparkjava.com/documentation#views-and-templates)) is set, it executes a _method_ that responses with a [`ModelAndView`](https://sparkjava.com/documentation#views-and-templates), this `View` is loaded from `/resources/template/` from the file `controllername.html.hbs`. For example your `HomeController` would send an html made from the `HomeController.html.hbs` template.

`Controller` has multiple initialization methods, which will initialize different [`routes`](https://sparkjava.com/documentation#routes), by default only `GET`. you can `@Override` this by _overriding_ the method [`getInitialization()`]() like, for example in [`LoginController`](https://github.com/tomasanchez/jpa-template/blob/356169760683433c137132d0bbe7ebaecf7cad40/src/main/java/com/jpa/controller/LogInController.java#L19-L22), the `ControllerInitialization` availables are:

- `FULL_CRUD`, which inits all the methods `GET`, `POST`, `PUT/PATCH` and `DELETE`. And also provides a `GET /path/new` for a View destinated to create a resource.
- `CRUD_NOT_ENGINE`, same as above but whithout returning a `ModelAndView`.
- `GET_POST`, only `GET` and `POST`
- `FULL_GET_POST`, also includes `GET /path/:id` and `POST /path/:id`
- `GET_POST_DELETE` also includes DELETE.
- `ONLY_GET`

<br></br>

**IMPORTANT** when overriding this, you should also override the corresponding methods:

- Methods that returns `ModelAndView`.
    - `onPost(request, response)`
- Methods that return any other `Object` (No `TemplateEngine`)
    - `onGetResponse(request, response)`
    - `onPostResponse(request, response)` 
    - `onDeleteResponse(request, response)`
    - `onPutResponse(request, response)`

<br></br>

> **RECOMENDATION**: If you need to delete/update from a form the easiest way is to make a `POST` with a flag which indicates wheter it is a post, a delete or an update. This wil allow you to return a `ModelAndView`. Or if using `JavaScript`,  `fetch()` an when returning reload the window, this will reflect the changes in the `ViewModel`. 

<br></br>

**LifeCycle** methods.

You should override this methods, implementating the lifecycle of the `ViewModel`. However, you can leave them empty, and will work just as well, without any logic.

![Life Cycle Methods](./assets/controller-snippet.svg)

<br></br>

####  How does it work?

The `Controller` has a `View` which contains a `Model`, this last one will be refered as `ViewModel`. You should manipulate this `ViewModel` to reflect changes on your view, when a request is being responded. Your controller will be automatically instantiate by the `ControllerLoaderService` as long as it inherits from `Controller`  class and it is located inside the `controller` package (not the `core.mvc.controller` package), so you don't need to worry about initialization, nor trying to use a _singleton_ approach.


<br></br>

## License

All material is provided under an MIT License unless otherwise specified.

MIT License: <https://mit-license.org/> or see the [`LICENSE`](https://github.com/tomasanchez/jpa-template/blob/main/LICENSE) file.
