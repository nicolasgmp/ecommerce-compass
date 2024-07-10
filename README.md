<h1 align="center"> Desafio Compass - E-Commerce</h1>

<p align="center">
  <a href="https://github.com/magrininicolas/placesAPIMVC/blob/main/LICENSE">
    <img src="https://img.shields.io/npm/l/react" alt="NPM License" />
 </a>
  <a href="https://www.linkedin.com/in/nicolasgmpereira">
    <img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" alt="LinkedIn" />
  </a>
</p>

API para o gerenciamento de um Ecommerce feito de acordo com o desafio proposto durante o estágio que realizo na CompassUOL.

## Tecnologias Utilizadas
- [Java](https://docs.oracle.com/en/java/)
- [Spring Boot](https://spring.io/projects/spring-boot/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [PostgreSQL](https://www.postgresql.org)

## Endpoints da API

As requisições HTTP abaixo foram feitas utilizando o [Bruno](https://www.usebruno.com/).

- Produtos

- POST /products
![POST Mapping](https://github.com/magrininicolas/ecommerce-compass/blob/main/src/main/resources/static/product/product_create.png)

```
URL: http://localhost:8080/products
Method: POST

Body(JSON):
{
  "name": "Touca",
  "stockQty": 201,
  "price": 69.90
}

Response Status: 201 Created
Response Body:
{
  "id": "a8626629-10d7-4241-8051-35dd40967d1a",
  "name": "Touca",
  "price": 69.9,
  "active": true,
  "stockQty": 201,
  "createdAt": "2024-07-10T19:03:11Z",
  "updatedAt": "1970-01-01T00:00:00Z"
}
```

- PUT /product/{id}
![PUT Mapping](https://github.com/magrininicolas/ecommerce-compass/blob/main/src/main/resources/static/product/product_update.png)
```
URL: http://localhost:8080/products/a8626629-10d7-4241-8051-35dd40967d1a
Method: PUT

Body:
{
  "stockQty": 250,
  "price": 69.90
}

Response Status: 200 OK
Response Body:
{
  "id": "a8626629-10d7-4241-8051-35dd40967d1a",
  "name": "Touca",
  "price": 69.9,
  "active": true,
  "stockQty": 250,
  "createdAt": "2024-07-10T19:03:11Z",
  "updatedAt": "2024-07-10T19:07:29Z"
}
```

- PUT /products/changestate/{id}
![PUT change state](https://github.com/magrininicolas/ecommerce-compass/blob/main/src/main/resources/static/product/product_change_state.png)
```
URL: http://localhost:8080/chagestate/a8626629-10d7-4241-8051-35dd40967d1a
Method: PUT

Response Status: 204 No Content
```

- GET /products/{id}
![GET by id](https://github.com/magrininicolas/ecommerce-compass/blob/main/src/main/resources/static/product/product_find_id.png)
```
URL: http://localhost:8080/products/a8626629-10d7-4241-8051-35dd40967d1a
Method: GET

Response Status: 200 OK
Response Body:
{
  "id": "a8626629-10d7-4241-8051-35dd40967d1a",
  "name": "Touca",
  "price": 69.9,
  "active": true,
  "stockQty": 250,
  "createdAt": "2024-07-10T19:03:11Z",
  "updatedAt": "2024-07-10T19:07:30Z"
}
```

- GET /products
![GET all](https://github.com/magrininicolas/ecommerce-compass/blob/main/src/main/resources/static/product/product_find_all.png)
```
URL: http://localhost:8080/products
Method: GET

Response Status: 200 OK
Response Body:
[
  {
    "id": "493192b6-1a81-4eae-b968-935917a78a13",
    "name": "Chuteira",
    "price": 69.9,
    "active": true,
    "stockQty": 100,
    "createdAt": "2024-07-10T17:27:43Z",
    "updatedAt": "2024-07-10T18:42:57Z"
  },
  {
    "id": "f2fce9bf-0792-4f34-9ade-8441a74987b8",
    "name": "Casaco",
    "price": 69.9,
    "active": true,
    "stockQty": 201,
    "createdAt": "2024-07-10T15:13:29Z",
    "updatedAt": "2024-07-10T18:44:44Z"
  },
  {
    "id": "a8626629-10d7-4241-8051-35dd40967d1a",
    "name": "Touca",
    "price": 69.9,
    "active": true,
    "stockQty": 250,
    "createdAt": "2024-07-10T19:03:11Z",
    "updatedAt": "2024-07-10T19:07:30Z"
  }
]
```

- DELETE /products/{id}
![DELETE mapping](https://github.com/magrininicolas/ecommerce-compass/blob/main/src/main/resources/static/product/product_delete.png)
```
URL: http://localhost:8080/products/a8626629-10d7-4241-8051-35dd40967d1a
Method: DELETE

Response Status: 204 No Content
```