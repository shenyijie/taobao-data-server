# upload-data-service

Backend service for the Taobao auto upload client.

## Overview

This is a Java Spring Boot backend that works with the `taobao-auto-upload-client`.

It fetches Taobao product information from Taobao data service providers or Taobao product pages, normalizes the product fields, and returns structured JSON data to the desktop upload client.

## Core Capabilities

- Token-based authentication
- Account balance checking
- Fetch product source data by product ID
- Parse and normalize product information: ID, title, brand, shop name, main images, etc.
- Handle offline products, missing items, and query failures
- Return unified JSON responses for the upload client

## Tech Stack

- Java 17 + Spring Boot
- MyBatis
- MySQL
- JSON parsing
- Local file cache for raw product data
- REST API with unified response wrapper

## Workflow

1. The desktop client sends a request with `token` and `productId`
2. The backend verifies the token and account status
3. The backend fetches raw product data from Taobao data channels
4. The service parses, cleans, and assembles standardized product data
5. The structured result is returned to the client for upload automation

## Design Note

This service separates data acquisition, authentication, and permission logic from browser automation.

By centralizing data processing on the server side, the client can focus on UI control and browser operations. This architecture improves maintainability and makes it easier to adjust data rules without affecting the client workflow.

## Related Repository

Client: [taobao-auto-upload-client](https://github.com/yourname/taobao-auto-upload-client)

## Disclaimer

This project is intended for personal learning and private workflow automation only.
Please comply with Taobao platform rules and applicable laws.
