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

The **`core`** package represents the fundamental components of this seed. There you will find a `mvc` _package_ where the `Model View Controller` is implemented.


`database` _package_ contains abstract convenience super classes which will help you with the model design.

`services` _package_ includes implementations used during run-time.

![CORE](./assets/seed-core.png)

<br></br>

There is no need for you to worry about the `core` package, _it just works_. Modifyng it may require some understanding of the MVC pattern and the seed itself.

#### `MVC` package

The **`Controller`** class is a generic implementation. Your controllers must inherit this class. By default all controllers are set to the path `/controllername` with the method [`GET`](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods), and if a [`TemplateEngine`]((https://sparkjava.com/documentation#views-and-templates)) is set, it executes a _method_ that responses with a [`ModelAndView`](https://sparkjava.com/documentation#views-and-templates).

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

The **`View`** class provides a layer of abstraction, representing the logic behind the matching and mapping of an `<HTML/>` file. It contains a `Model`, which will be refered as `ViewModel`, this will allow you to reflect the changes in the _actual_ view, the UI.

**Note**: Each `Controller` has its own `View` instance, this `View` is loaded from `/resources/template/` from the file `controllername.html.hbs`. For example your `HomeController` would send an html made from the `HomeController.html.hbs` template.

<br></br>

The `Model` is another abstraction, in this case of a `Map<String, Object>`, providing convenience methods such as instantiation via `JSON` or `Properies` _file_. Model also provides fluent setter for properties, and can be joined with other models (puts one map into another).

**Note**: Each `View` has a `Model` instance. In addition a `Model` can contain another `Model` within. As a `Controller` has a `View` which contains a `ViewModel`, controllers will have different `Model` instances, however there is a shared `Model` among them.


![View Example](./assets/view-snippet.svg)


`{{isValid}}` is a property of the `ViewModel` which can be set/unset by its corresponding controller, in this scenario it is used to modify a `Bootstrap 5` class for the `input form-control`, the `is-invalid` which alerts an error message when set.


<br></br>

**How does it all work?**

The `Controller` has a `View` which contains a `Model`, the `ViewModel`. You should manipulate this `ViewModel` to reflect changes on your view, when a request is being responded. Your controller will be automatically instantiate by the `ControllerLoaderService` as long as it inherits from `Controller`  class and it is located inside the `controller` package (not the `core.mvc.controller` package), so you don't need to worry about initialization, nor trying to use a _singleton_ approach. With this a `View` and a`ViewModel` will be automatically initializated, ready to be modified according to your needs.

<br></br>

**What do I have to do?** / **Quick Start**

- Create a new `class ExampleController extends Controller` inside the `controller` package (not the `core.mvc.controller` package)
- Create a new `file Example.html.hbs` with the same name as your controller. Ex. _`ToDoController`_, and _`ToDo.html.hbs`_ (case sensitive)
- Run `main` in `Router` class.
- Get your view at the endpoint. Ex. _`ToDoController`_, and _`ToDo.html.hbs`_ and `/todo`

<br></br>

## Going Further

<br></br>

### Routing

This seed contains 2 (two) views, `Home` and `LogIn` and a custom `Not Found` mapped view. Note that each view has its own controller, and all of these inherit from a `BaseController`. This controller has no associated view, as it is purely an implementation of shared methods and convenience shortcuts.

In this seed, used for developing assignments in `UTN-FRBA`, the `BaseController` implements `WithGlobalEntityManager, TransactionalOps`, interface provided by professors of the course [`System's Design`](https://dds-jv.github.io/), which includes convenience methods for transactional operations.

<br></br>

![Routing](./assets/app-routing.png)

<br></br>

### Database

The seed provides you with a `PersistentEntity` class which can be extended by your use case entities which will be persisted in a database. This is only a generic class with convenience attributes for an entity's `Id`. See how the `User` class extends `PersistentEntity`.

```java
@MappedSuperclass
public abstract class PersistentEntity implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
```


In addition, there is a `PersistentEntitySet<T>` generic class, which allows you to easily create `CRUD Repositories`.

The interface provided:

```java
@MappedSuperclass
public abstract class PersistentEntitySet<T> implements WithGlobalEntityManager {

    /**
     * Retrieves Table name (class name).
     * 
     * ? Example: PersistentEntitySet<User> => Table name is User
     * 
     * @return the table name
     */
    protected String getTableName();

    /**
     * Obtains all entities in the database.
     * 
     * @return an entity list.
     */
    public List<T> getEntitySet();

    /**
     * Persists an entity in database.
     * 
     * @param entity to be persisted
     * @return persisted entity
     */
    public T createEntity(T entity);
    

    /**
     * Obtains a single entity.
     * 
     * @param id the entity unique id
     * @return entity or null.
     */
    public T getEntity(long id);

    /**
     * Updates the database with the entity.
     * 
     * @param entity to be updated
     * @return the updated entity
     */
    public T updateEntity(T entity);

    /**
     * Updates the database with the entity.
     * 
     * @param id the entity's unique id
     * @return the updated entity
     */
    public T updateEntity(Long id);

    /**
     * Removes an entity from database.
     * 
     * @param entity the entity to be deleted
     */
    public void deleteEntity(T entity);

    /**
     * Removes an entity from database.
     * 
     * @param id the entity unique id
     */
    public void deleteEntity(Long id);
}
```

**NOTE**: You can easily add your own custom implementation in a child class.

See this case of `UserRepository`

```java
public class UserRepository extends PersistentEntitySet<User> {

    /**
     * Obtains an user from database that matches the given username and password.
     * 
     * @param username to match
     * @param password to validate
     * @return An authenticated user
     */
    public User getEntity(String username, String password);
}
```

<br></br>

### Internationalization

There is a custom implementation of a `ResourceBundle`, it requires a `.properties` file located in `/resources/locales/`, and also to be named as it follows: `i18n-{ISO 639-1}.properties`. See [`ISO 639-1`](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) for further information. 

These properties will be loaded automatically into a model, available for the controller, an so its view. 

Being your `i18n-en.properties` file:

```properties
inputLabel=An Input label
inputPh=Insert a text
```

Therefore, you can access these translations from a view this way:

```hbs
<label>{{i18n.inputLabel}}</label>
<input
 placeholder="{{i18n.inputPh}}"
/>
```

Which will result in:

```hbs
<label>An Input label</label>
<input
 placeholder="Insert a text"
/>
```

For each controller, before `onBeforeRendering`, the request's header [`Accept-Language`](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Language) will be evaluated, if it is the same as the current used, there will be no changes, otherwise, there will be a try to load the requested language from a `.properties` file, if it exists, else the default language will be used.

Following the previous example, if the `Accept-Language` header switchs to `es`, and there is a `i18n-es.properties`:


```properties
inputLabel=Una etiqueta
inputPh=Ingrese un texto
```

Will result in:

```hbs
<label>Una etiqueta</label>
<input
 placeholder="Ingrese un texto"
/>
```

<br></br>

## License

All material is provided under an MIT License unless otherwise specified.

MIT License: <https://mit-license.org/> or see the [`LICENSE`](https://github.com/tomasanchez/jpa-template/blob/main/LICENSE) file.
