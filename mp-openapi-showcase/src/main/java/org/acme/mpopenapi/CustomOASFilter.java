package org.acme.mpopenapi;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Custom OAS Filter. Adds custom attributes to api operations.
 */
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
