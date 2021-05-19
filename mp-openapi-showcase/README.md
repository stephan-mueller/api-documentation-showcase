# MicroProfile OpenAPI Showcase
 
The showcase provides a hello world application based on the [Open Liberty](https://openliberty.io) microservice framework. It demonstrates
features of the [OpenAPI](https://spec.openapis.org/oas/v3.0.3) and the 
[MicroProfile OpenAPI](https://microprofile.io/project/eclipse/microprofile-open-api) specification.

**Notable features:**
* Usage of Extensions
* Customizing the OpenAPI definition
* Merging pregenerated OpenAPI documents with documentation from the code

## How to run

Before running the application it needs to be compiled and packaged using `Maven`. It creates the runnable JAR and Docker image and can be 
run via `docker`:

```shell script
$ mvn clean package
$ docker run --rm -p 9080:9080 mp-openapi-showcase
```

Wait for a message log similar to this:

> [5/19/21, 10:12:44:764 UTC] 0000002b id=         com.ibm.ws.kernel.feature.internal.FeatureManager            A CWWKF0011I: The defaultServer server is ready to run a smarter planet. The defaultServer server started in 3.616 seconds.


If everything worked you can access the api documentation via [http://localhost:9080/](http://localhost:9080/).

### Resolving issues

Sometimes it may happen that the containers did not stop as expected when trying to stop the pipeline early. This may
result in running containers although they should have been stopped and removed. To detect them you need to check
Docker:

```shell script
$ docker ps -a | grep mp-openapi-showcase
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

### OpenAPI

The application provides support for OpenAPI documentation.

OpenAPI in YAML / JSON Format
```shell script
curl -s -X GET http://localhost:9080/openapi

curl -H 'Accept: application/json' -X GET http://localhost:9080/openapi
```


#### Extensions

Extensions (also referred to as specification extensions or vendor extensions) are custom properties that start with x-, such as x-logo.
They can be used to describe extra functionality that is not covered by the standard OpenAPI Specification. Many API-related
products that support OpenAPI make use of extensions to document their own attributes, such as Amazon API Gateway, ReDoc,
APIMatic and others. Extensions are supported on the root level of the API spec and in other places (info section, path section, operations,
parameters, ...) as well. An extension value can be a primitive, an array, an object or null.

_GreetResource - Example for using the @Extension annotation with an @Operation annotation_
```java
@Path("greet")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GreetResource {

  ...

  @GET
  @Operation(operationId = "greetTheWorld", description = "Greet the world")
  @APIResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = GreetDTO.class), mediaType = MediaType.APPLICATION_JSON))
  @Extension(name = "x-default-response", value = "Hello World!")
  public Response greetTheWorld() {
    return greet("World");
  }

  ...
}
```

For further information about extensions please check the [OpenAPI Specification](https://spec.openapis.org/oas/v3.0.3#specification-extensions).


#### Customizing the OpenAPI Definition

There are many scenarios where application developers may wish to update or remove certain elements and fields of the OpenAPI document. The 
[MicroProfile OpenAPI](https://github.com/eclipse/microprofile-open-api/blob/master/spec/src/main/asciidoc/microprofile-openapi-spec.adoc#programming-model) 
provides a programming model to customise the generated OpenAPI definition beyond what is possible with the annotations.

The OASFilter provides a hook to customise automatically generated OpenAPI definitions. It allows application developers to receive 
callbacks for various key OpenAPI elements. The interface has a default implementation for every method, which allows application developers
to only override the methods they care about. To use it, simply create an implementation of this interface and register it using the 
`mp.openapi.filter configuration key, where the value is the fully qualified name of the filter class.

_META-INF/microprofile-config.properties_
```properties
mp.openapi.filter=org.acme.mpopenapi.CustomOASFilter
```


_CustomOASFilter - Example for adding a dynamic value provided by MicroProfile Config to the OpenAPI definition_
```java
public class CustomOASFilter implements OASFilter {

  private static final Logger LOG = LoggerFactory.getLogger(CustomOASFilter.class);

  private static final String X_APP_GREETING = "x-app-greeting";

  @Override
  public Operation filterOperation(final Operation operation) {
    if (Objects.equals(operation.getOperationId(), "updateGreeting")) {
      Config config = ConfigProvider.getConfig();
      String appGreeting = config.getValue("app.greeting", String.class);

      LOG.info("Add custom extension to api operation %s", operation.getOperationId());

      operation.addExtension(X_APP_GREETING, appGreeting);
    }

    return operation;
  }
}
```


#### Merging pregenerated OpenAPI documents with documentation from the code

As an alternative to generating the OpenAPI model tree from code, a valid pregenerated OpenAPI document can be provided to describe an API. 
The document has to be named openapi with a yml, yaml, or json extension and be placed under the META-INF directory. Depending on the 
scenario, the document might be fully or partially complete. If the document is fully complete, it is possible to disable annotation 
scanning entirely by setting the `mp.openapi.scan.disable` MicroProfile Config property to `true`. If the document is partially complete, 
it can be augmented with code.

_src/main/webapp/META-INF/openapi.yaml - Example for a partial document that is merged with the OpenAPI documentation in the code_
```yaml
---
openapi: 3.0.3
info:
  title: Greeting API
  contact:
    name: Stephan Mueller
    email: stephan.mueller@acme.org
    url: https://github.com/stephan-mueller/api-documentation-showcase/openapi-showcase
  version: 0.0.0
paths:
  /api/greet:
    get:
      operationId: greetTheWorld
      responses:
        "200":
          description: Ok
...
```

_openapi.yaml - Example for the final document after merging the partial document and the OpenAPI documentation in the code_
```yaml
---
openapi: 3.0.3
info:
  title: Greeting API
  description: Provides access to the API operations
  contact:
    name: Stephan Mueller
    email: stephan.mueller@acme.org
    url: https://github.com/stephan-mueller/api-documentation-showcase/openapi-showcase
  license:
    name: Apache 2.0
    url: http://www.apache.org/licenses/LICENSE-2.0.html
  version: 1.0.0
servers:
  - url: "http://{host}:{port}/{context-path}"
    variables:
      host:
        default: localhost
      port:
        default: "9080"
      context-path:
        default: mp-openapi-showcase
paths:
  /api/greet:
    get:
      description: Greet the world
      operationId: greetTheWorld
      responses:
        "200":
          description: Ok
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/GreetDTO'
          x-default-response: Hello World!
      x-default-response: Hello World!
...
```