# Swagger Showcase

The showcase provides a hello world application based on the [Open Liberty](https://openliberty.io) microservice framework. It demonstrates
features of the [Swagger RESTful API Documentation Specification](https://swagger.io/specification/v2/) and the 
[Swagger Core](https://github.com/swagger-api/swagger-core) framework.

**Notable features:**
* Usage of Extensions
* Customizing the Swagger definition

## How to run

Before running the application it needs to be compiled and packaged using `Maven`. It creates the runnable JAR and Docker image and can be
run via `docker`:

```shell script
$ mvn clean package
$ docker run --rm -p 9080:9080 swagger-showcase
```

Wait for a message log similar to this:

> [5/19/21, 10:12:44:764 UTC] 0000002b id=         com.ibm.ws.kernel.feature.internal.FeatureManager            A CWWKF0011I: The defaultServer server is ready to run a smarter planet. The defaultServer server started in 3.616 seconds.


If everything worked you can access the api documentation via [http://localhost:9080/](http://localhost:9080/).

### Resolving issues

Sometimes it may happen that the containers did not stop as expected when trying to stop the pipeline early. This may
result in running containers although they should have been stopped and removed. To detect them you need to check
Docker:

```shell script
$ docker ps -a | grep swagger-showcase
```

If there are containers remaining although the application has been stopped you can remove them:

```shell script
$ docker rm <ids of the containers>
```


## Features

### Application

The application is a very simple "Hello World" greeting service. It supports GET requests for generating a greeting message, and a PUT
request for changing the greeting itself. The response is encoded using JSON.

Try the application
```shell script
curl -X GET http://localhost:9080/api/greet
{"message":"Hello World!"}

curl -X GET http://localhost:9080/api/greet/Stephan
{"message":"Hello Stephan!"}

curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Hola"}' http://localhost:9080/api/greet/greeting

curl -X GET http://localhost:9080/api/greet/greeting
{"greeting":"Hola"}

curl -X GET http://localhost:9080/api/greet/Max
{"message":"Hola Max!"}
```

### Swagger

The application provides support for Swagger documentation.

OpenAPI in YAML / JSON Format
```shell script
curl -s -X GET http://localhost:9080/api/swagger.yaml

curl -s -X GET http://localhost:9080/api/swagger.json
```


#### Extensions

Extensions (also referred to as specification extensions or vendor extensions) are custom properties that start with x-, such as x-logo. 
They can be used to describe extra functionality that is not covered by the standard OpenAPI / Swagger Specification. Many API-related 
products that support OpenAPI / Swagger make use of extensions to document their own attributes, such as Amazon API Gateway, ReDoc, 
APIMatic and others. Extensions are supported on the root level of the API spec and in other places (info section, path section, operations,
parameters, ...) as well. An extension value can be a primitive, an array, an object or null.

_GreetResource - Example for using the @Extension annotation with an @ApiOperation annotation_
```java
@Path("greet")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api
public class GreetResource {

  ...

  @GET
  @ApiOperation(nickname = "greetTheWorld", value = "Greet the world", extensions = {
      @Extension(properties = @ExtensionProperty(name = "default-response", value = "Hello World!"))
  })
  @ApiResponses(@ApiResponse(code = 200, message = "Ok", response = GreetDTO.class, examples = @Example(value = @ExampleProperty(mediaType = MediaType.APPLICATION_JSON, value = "{\"message\" : \"Hello World!\"}"))))
  public Response greetTheWorld() {
    return greet("World");
  }

  ...
}
```

For further information about extensions please check the [Swagger Specification](https://swagger.io/docs/specification/2-0/swagger-extensions/).


#### Customizing the Swagger definition

Swagger provides two ways to customise the generated Swagger definition beyond what is possible with the annotations. The 
**[ReaderListener](https://github.com/swagger-api/swagger-core/wiki/Annotations-1.5.X#customising-the-swagger-definition)** 
and the **AbstractSwaggerExtension** provides hooks to customise automatically generated Swagger definitions in a JAX-RS environment. 

Any classes implementing the ReaderListener interface will be invoked before and after generating Swagger definitions, allowing code to add
additional data or change the generated definition. 

Classes extending the AbstractSwaggerExtension will be invoked after generating Swagger definitions and enables the decoration of 
operations with additional vendor based extensions. To use it, an implementation extending the superclass has to be registered in a 
file named `io.swagger.jaxrs.ext.SwaggerExtension` which has to be located in the `META-INF/services` directory. 

_META-INF/services/io.swagger.jaxrs.ext.SwaggerExtension_
```properties
org.acme.swagger.CustomSwaggerExtension
```


_CustomSwaggerExtension - Example for adding a dynamic value provided by MicroProfile Config to the Swagger definition_
```java
public class CustomSwaggerExtension extends AbstractSwaggerExtension {

  private static final Logger LOG = LoggerFactory.getLogger(CustomSwaggerExtension.class);

  private static final String X_APP_GREETING = "x-app-greeting";

  @Override
  public void decorateOperation(final Operation operation, final Method method, final Iterator<SwaggerExtension> chain) {
    if (operation.getOperationId().equals("updateGreeting")) {
      Config config = ConfigProvider.getConfig();
      String appGreeting = config.getValue("APP_GREETING", String.class);

      LOG.info("Add custom extension to api operation %s", operation.getOperationId());

      operation.getVendorExtensions().put(X_APP_GREETING, appGreeting);
    }

    super.decorateOperation(operation, method, chain);
  }
}
```