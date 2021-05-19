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

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;

/**
 * JAX-RS Activator
 */
@ApplicationPath("api")
@SwaggerDefinition(info = @Info(title = "Greeting API", description = "Provides access to the API operations", version = "1.0.0",
    contact = @Contact(name = "Stephan Mueller", email = "stephan.mueller@acme.org", url = "https://github.com/stephan-mueller/api-documentation-showcase/swagger-showcase"),
    license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")))
public class JaxRsActivator extends Application {

}
