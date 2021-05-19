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

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Iterator;

import io.swagger.jaxrs.ext.AbstractSwaggerExtension;
import io.swagger.jaxrs.ext.SwaggerExtension;
import io.swagger.models.Operation;

/**
 * Custom swagger extension. Adds custom attributes to api operations.
 */
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
