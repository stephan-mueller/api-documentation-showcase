# API Documentation Showcase

[![GitHub Workflow](https://github.com/stephan-mueller/api-documentation-showcase/actions/workflows/maven.yml/badge.svg)](https://github.com/stephan-mueller/api-documentation-showcase/actions)
[![GitHub last commit](https://img.shields.io/github/last-commit/stephan-mueller/api-documentation-showcase)](https://github.com/stephan-mueller/api-documentation-showcase/commits)
[![GitHub](https://img.shields.io/github/license/stephan-mueller/api-documentation-showcase)](https://github.com/stephan-mueller/api-documentation-showcase/blob/master/LICENSE)

This is a showcase for api documentation. It contains hello world applications, which demonstrates features of the
[OpenAPI Specification](https://openapis.org) and others. Software requirements to run the samples are `maven`, `openjdk-8` (or any other 
JDK 8) and `docker`.

## OpenAPI Specification (OAS)

The [OpenAPI Specification](https://spec.openapis.org/oas/v3.0.3) (OAS) defines a standard, programming language-agnostic interface description for REST APIs, which allows both 
humans and computers to discover and understand the capabilities of a service without requiring access to source code, additional 
documentation, or inspection of network traffic. When properly defined via OpenAPI, a consumer can understand and interact with the remote
service with a minimal amount of implementation logic. Similar to what interface descriptions have done for lower-level programming, the 
OpenAPI Specification removes guesswork in calling a service.

### OAS v2 (fka Swagger RESTful API Documentation Specification)

[Swagger](https://swagger.io/specification/v2/) is an Interface Description Language for describing RESTful APIs expressed using JSON. Swagger is used together with a set of 
open-source software tools to design, build, document, and use RESTful web services. Swagger includes automated documentation, code 
generation (into many programming languages), and test-case generation.

### Showcases

* The [Eclipse MicroProfile OpenAPI Showcase](mp-openapi-showcase/README.md) shows features of the MP OpenAPI Specification.
* The [Swagger Showcase](swagger-showcase/README.md) shows features of the Swagger Specification and the Swagger Core framework.