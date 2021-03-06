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
package org.acme.mpopenapi;

import org.apache.commons.lang3.Validate;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Objects;

import javax.validation.constraints.NotNull;

/**
 * A DTO that represents a greeting
 */
@Schema
public class GreetingDTO {

  @NotNull
  @Schema(example = "Hey")
  private String greeting;

  public GreetingDTO() {
    super();
  }

  public GreetingDTO(final String greeting) {
    this();
    this.greeting = Validate.notNull(greeting, "greeting must not be null");
  }

  public String getGreeting() {
    return greeting;
  }

  public void setGreeting(final String greeting) {
    this.greeting = greeting;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GreetingDTO that = (GreetingDTO) o;
    return Objects.equals(greeting, that.greeting);
  }

  @Override
  public int hashCode() {
    return Objects.hash(greeting);
  }

  @Override
  public String toString() {
    return "GreetingDTO{" +
           "greeting='" + greeting + '\'' +
           '}';
  }
}
