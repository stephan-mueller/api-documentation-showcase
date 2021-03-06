/*
 * Copyright (C) Stephan Mueller
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package org.acme.swagger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;

/**
 * A resource that provides access to the world.
 */
@Path("greet")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api
public class GreetResource {

  private static final Logger LOG = LoggerFactory.getLogger(GreetResource.class);

  @Inject
  private GreetingApplicationService service;

  @GET
  @Path("{name}")
  @ApiOperation(nickname = "greetSomeone", value = "Greet someone")
  @ApiResponses(@ApiResponse(code = 200, message = "Ok", response = GreetDTO.class, examples = @Example(value = @ExampleProperty(mediaType = MediaType.APPLICATION_JSON, value = "{\"message\" : \"Hey Stephan!\"}"))))
  public Response greet(@ApiParam(value = "name", example = "Stephan") @PathParam("name") final String name) {
    LOG.info("Greet {}", name);

    GreetDTO message = new GreetDTO(service.getMessage(name));

    LOG.info("{}", message);

    return Response.status(Response.Status.OK)
        .entity(message)
        .build();
  }

  @GET
  @ApiOperation(nickname = "greetTheWorld", value = "Greet the world", extensions = {
      @Extension(properties = @ExtensionProperty(name = "default-response", value = "Hello World!"))
  })
  @ApiResponses(@ApiResponse(code = 200, message = "Ok", response = GreetDTO.class, examples = @Example(value = @ExampleProperty(mediaType = MediaType.APPLICATION_JSON, value = "{\"message\" : \"Hello World!\"}"))))
  public Response greetTheWorld() {
    return greet("World");
  }

  @Path("greeting")
  @GET
  @ApiOperation(nickname = "getGreeting", value = "Get greeting")
  @ApiResponses(@ApiResponse(code = 200, message = "Ok", response = GreetingDTO.class, examples = @Example(value = @ExampleProperty(mediaType = MediaType.APPLICATION_JSON, value = "{\"greeting\" : \"Hola\"}"))))
  public Response getGreeting() {
    LOG.info("Get greeting");

    GreetingDTO greeting = new GreetingDTO(service.getGreeting());

    LOG.info("{}", greeting);

    return Response.status(Response.Status.OK)
        .entity(greeting)
        .build();
  }

  @Path("greeting")
  @PUT
  @ApiOperation(nickname = "updateGreeting", value = "Update greeting")
  @ApiResponses({
      @ApiResponse(code = 204, message = "Greeting updated"),
      @ApiResponse(code = 400, message = "Invalid 'greeting' request")
  })
  public Response updateGreeting(@ApiParam(value = "greeting", examples = @Example(value = @ExampleProperty(mediaType = MediaType.APPLICATION_JSON, value = "{\"greeting\" : \"Hey\"}"))) @Valid final GreetingDTO greeting) {
    LOG.info("Set greeting to {}", greeting.getGreeting());

    service.updateGreeting(greeting.getGreeting());

    LOG.info("Greeting updated");

    return Response.status(Response.Status.NO_CONTENT).build();
  }
}
