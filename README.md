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

### Produtos

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

### Vendas

- POST /sales
![POST Mapping](https://github.com/magrininicolas/ecommerce-compass/blob/main/src/main/resources/static/sale/sale_create.png)
```
URL: http://localhost:8080/sales
Method: POST

Body:
{
  "items": [
    {
      "productId": "a8626629-10d7-4241-8051-35dd40967d1a",
      "quantity": 10
    }
  ]
}

Response Status: 201 Created
Response Body:
{
  "id": "24163754-58b1-492a-851c-f7ca46a2734a",
  "createdAt": "2024-07-10T19:32:31Z",
  "updatedAt": "1970-01-01T00:00:00Z",
  "total": 699,
  "items": [
    {
      "id": "c5ae177b-0864-47de-8b22-316c567867f3",
      "quantity": 10,
      "product": {
        "id": "a8626629-10d7-4241-8051-35dd40967d1a",
        "name": "Touca",
        "price": 69.9,
        "active": true,
        "stockQty": 240,
        "createdAt": "2024-07-10T19:03:11Z",
        "updatedAt": "2024-07-10T19:32:19Z"
      }
    }
  ]
}
```

- PUT /sales/{id}
![PUT Mapping](https://github.com/magrininicolas/ecommerce-compass/blob/main/src/main/resources/static/sale/sale_update.png)
```
URL: http://localhost:8080/sales/24163754-58b1-492a-851c-f7ca46a2734a
Method: PUT

Body:
{
  "items": [
    {
      "productId": "a8626629-10d7-4241-8051-35dd40967d1a",
      "quantity": 10
    }
  ]
}

Response Status: 200 OK
Response Body:
{
  "id": "24163754-58b1-492a-851c-f7ca46a2734a",
  "createdAt": "2024-07-10T19:32:31Z",
  "updatedAt": "2024-07-10T19:34:34Z",
  "total": 1398,
  "items": [
    {
      "id": "c5ae177b-0864-47de-8b22-316c567867f3",
      "quantity": 20,
      "product": {
        "id": "a8626629-10d7-4241-8051-35dd40967d1a",
        "name": "Touca",
        "price": 69.9,
        "active": true,
        "stockQty": 230,
        "createdAt": "2024-07-10T19:03:11Z",
        "updatedAt": "2024-07-10T19:32:31Z"
      }
    }
  ]
}
```
- DELETE /sales/{id}
![DELETE Mapping](https://github.com/magrininicolas/ecommerce-compass/blob/main/src/main/resources/static/sale/sale_delete.png)
```
URL: http://localhost:8080/sales/24163754-58b1-492a-851c-f7ca46a2734a
Method: DELETE

Response Status: 204 No Content
```

- GET /sales
![GET All](https://github.com/magrininicolas/ecommerce-compass/blob/main/src/main/resources/static/sale/sale_find_all.png)
```
URL: http://localhost:8080/sales
Method: GET

Response Status: 200 OK
Response Body:
[
  {
    "id": "2d00e88e-4d6a-4900-9770-f13dbbaa33c2",
    "createdAt": "2024-07-10T16:21:51Z",
    "updatedAt": "2024-07-10T16:22:15Z",
    "total": 396,
    "items": [
      {
        "id": "ed170301-c50a-4ea3-ae9e-7fb64a1f9c4d",
        "quantity": 20,
        "product": {
          "id": "e21a3e85-f5bc-4bd3-a670-9297777c6b72",
          "name": "Camiseta",
          "price": 9.9,
          "active": true,
          "stockQty": 70,
          "createdAt": "2024-07-10T15:31:09Z",
          "updatedAt": "2024-07-10T18:39:49Z"
        }
      },
      {
        "id": "f4b45ca8-3119-42c3-bd4a-58a0a19dc19a",
        "quantity": 20,
        "product": {
          "id": "f2fce9bf-0792-4f34-9ade-8441a74987b8",
          "name": "Casaco",
          "price": 69.9,
          "active": true,
          "stockQty": 201,
          "createdAt": "2024-07-10T15:13:29Z",
          "updatedAt": "2024-07-10T18:44:44Z"
        }
      }
    ]
  },
  {
    "id": "4a376b40-7ddc-408f-8a0d-7ae630d9207c",
    "createdAt": "2024-07-10T17:32:31Z",
    "updatedAt": "1970-01-01T00:00:00Z",
    "total": 99,
    "items": [
      {
        "id": "8f1c53d9-0605-4ac1-98df-0bf37019f47b",
        "quantity": 10,
        "product": {
          "id": "f2fce9bf-0792-4f34-9ade-8441a74987b8",
          "name": "Casaco",
          "price": 69.9,
          "active": true,
          "stockQty": 201,
          "createdAt": "2024-07-10T15:13:29Z",
          "updatedAt": "2024-07-10T18:44:44Z"
        }
      }
    ]
  }
]
```

- GET /sales/{id}
![GET by ID](https://github.com/magrininicolas/ecommerce-compass/blob/main/src/main/resources/static/sale/sale_find_id.png)
```
URL: http://localhost:8080/sales/4a376b40-7ddc-408f-8a0d-7ae630d9207c
Method: GET

Response Status: 200 OK
Response Body:
{
  "id": "4a376b40-7ddc-408f-8a0d-7ae630d9207c",
  "createdAt": "2024-07-10T17:32:31Z",
  "updatedAt": "1970-01-01T00:00:00Z",
  "total": 99,
  "items": [
    {
      "id": "8f1c53d9-0605-4ac1-98df-0bf37019f47b",
      "quantity": 10,
      "product": {
        "id": "f2fce9bf-0792-4f34-9ade-8441a74987b8",
        "name": "Casaco",
        "price": 69.9,
        "active": true,
        "stockQty": 201,
        "createdAt": "2024-07-10T15:13:29Z",
        "updatedAt": "2024-07-10T18:44:44Z"
      }
    }
  ]
}
```

- GET /sales/date?date=?
![GET by DATE](https://github.com/magrininicolas/ecommerce-compass/blob/main/src/main/resources/static/sale/sale_find_date.png)
```
URL: http://localhost:8080/sales/date?date=2024-07-10
Method: GET

Response Status: 200 OK
Response Body:
[
  {
    "id": "2d00e88e-4d6a-4900-9770-f13dbbaa33c2",
    "createdAt": "2024-07-10T16:21:51Z",
    "updatedAt": "2024-07-10T16:22:15Z",
    "total": 396,
    "items": [
      {
        "id": "ed170301-c50a-4ea3-ae9e-7fb64a1f9c4d",
        "quantity": 20,
        "product": {
          "id": "e21a3e85-f5bc-4bd3-a670-9297777c6b72",
          "name": "Camiseta",
          "price": 9.9,
          "active": true,
          "stockQty": 70,
          "createdAt": "2024-07-10T15:31:09Z",
          "updatedAt": "2024-07-10T18:39:49Z"
        }
      },
      {
        "id": "f4b45ca8-3119-42c3-bd4a-58a0a19dc19a",
        "quantity": 20,
        "product": {
          "id": "f2fce9bf-0792-4f34-9ade-8441a74987b8",
          "name": "Casaco",
          "price": 69.9,
          "active": true,
          "stockQty": 201,
          "createdAt": "2024-07-10T15:13:29Z",
          "updatedAt": "2024-07-10T18:44:44Z"
        }
      }
    ]
  },
  {
    "id": "4a376b40-7ddc-408f-8a0d-7ae630d9207c",
    "createdAt": "2024-07-10T17:32:31Z",
    "updatedAt": "1970-01-01T00:00:00Z",
    "total": 99,
    "items": [
      {
        "id": "8f1c53d9-0605-4ac1-98df-0bf37019f47b",
        "quantity": 10,
        "product": {
          "id": "f2fce9bf-0792-4f34-9ade-8441a74987b8",
          "name": "Casaco",
          "price": 69.9,
          "active": true,
          "stockQty": 201,
          "createdAt": "2024-07-10T15:13:29Z",
          "updatedAt": "2024-07-10T18:44:44Z"
        }
      }
    ]
  },
  {
    "id": "5648ef0f-4e97-476a-a969-4f2e3d05df80",
    "createdAt": "2024-07-10T17:32:33Z",
    "updatedAt": "1970-01-01T00:00:00Z",
    "total": 99,
    "items": [
      {
        "id": "1678084e-17b7-44d1-99d9-fee2fac8e70b",
        "quantity": 10,
        "product": {
          "id": "f2fce9bf-0792-4f34-9ade-8441a74987b8",
          "name": "Casaco",
          "price": 69.9,
          "active": true,
          "stockQty": 201,
          "createdAt": "2024-07-10T15:13:29Z",
          "updatedAt": "2024-07-10T18:44:44Z"
        }
      }
    ]
  }
]
```

- GET /sales/report/monthly?year=?&month=?
![GET by Month](https://github.com/magrininicolas/ecommerce-compass/blob/main/src/main/resources/static/sale/sale_report_month.png)
```
URL: http://localhost:8080/sales/report/monthly?year=2024&month=7
Method: GET

Response Status: 200 OK
Response Body:
[
  {
    "id": "2d00e88e-4d6a-4900-9770-f13dbbaa33c2",
    "createdAt": "2024-07-10T16:21:51Z",
    "updatedAt": "2024-07-10T16:22:15Z",
    "total": 396,
    "items": [
      {
        "id": "ed170301-c50a-4ea3-ae9e-7fb64a1f9c4d",
        "quantity": 20,
        "product": {
          "id": "e21a3e85-f5bc-4bd3-a670-9297777c6b72",
          "name": "Camiseta",
          "price": 9.9,
          "active": true,
          "stockQty": 70,
          "createdAt": "2024-07-10T15:31:09Z",
          "updatedAt": "2024-07-10T18:39:49Z"
        }
      },
      {
        "id": "f4b45ca8-3119-42c3-bd4a-58a0a19dc19a",
        "quantity": 20,
        "product": {
          "id": "f2fce9bf-0792-4f34-9ade-8441a74987b8",
          "name": "Casaco",
          "price": 69.9,
          "active": true,
          "stockQty": 201,
          "createdAt": "2024-07-10T15:13:29Z",
          "updatedAt": "2024-07-10T18:44:44Z"
        }
      }
    ]
  },
  {
    "id": "4a376b40-7ddc-408f-8a0d-7ae630d9207c",
    "createdAt": "2024-07-10T17:32:31Z",
    "updatedAt": "1970-01-01T00:00:00Z",
    "total": 99,
    "items": [
      {
        "id": "8f1c53d9-0605-4ac1-98df-0bf37019f47b",
        "quantity": 10,
        "product": {
          "id": "f2fce9bf-0792-4f34-9ade-8441a74987b8",
          "name": "Casaco",
          "price": 69.9,
          "active": true,
          "stockQty": 201,
          "createdAt": "2024-07-10T15:13:29Z",
          "updatedAt": "2024-07-10T18:44:44Z"
        }
      }
    ]
  },
  {
    "id": "5648ef0f-4e97-476a-a969-4f2e3d05df80",
    "createdAt": "2024-07-10T17:32:33Z",
    "updatedAt": "1970-01-01T00:00:00Z",
    "total": 99,
    "items": [
      {
        "id": "1678084e-17b7-44d1-99d9-fee2fac8e70b",
        "quantity": 10,
        "product": {
          "id": "f2fce9bf-0792-4f34-9ade-8441a74987b8",
          "name": "Casaco",
          "price": 69.9,
          "active": true,
          "stockQty": 201,
          "createdAt": "2024-07-10T15:13:29Z",
          "updatedAt": "2024-07-10T18:44:44Z"
        }
      }
    ]
  }
]
```

- GET /sales/report/weekly?date=?
![GET by Week](https://github.com/magrininicolas/ecommerce-compass/blob/main/src/main/resources/static/sale/sale_report_week.png)
```
URL: http://localhost:8080/sales/report/weekly?date=2024-07-08
Method: GET

Response Status: 200 OK
Response Body:
[
  {
    "id": "2d00e88e-4d6a-4900-9770-f13dbbaa33c2",
    "createdAt": "2024-07-10T16:21:51Z",
    "updatedAt": "2024-07-10T16:22:15Z",
    "total": 396,
    "items": [
      {
        "id": "ed170301-c50a-4ea3-ae9e-7fb64a1f9c4d",
        "quantity": 20,
        "product": {
          "id": "e21a3e85-f5bc-4bd3-a670-9297777c6b72",
          "name": "Camiseta",
          "price": 9.9,
          "active": true,
          "stockQty": 70,
          "createdAt": "2024-07-10T15:31:09Z",
          "updatedAt": "2024-07-10T18:39:49Z"
        }
      },
      {
        "id": "f4b45ca8-3119-42c3-bd4a-58a0a19dc19a",
        "quantity": 20,
        "product": {
          "id": "f2fce9bf-0792-4f34-9ade-8441a74987b8",
          "name": "Casaco",
          "price": 69.9,
          "active": true,
          "stockQty": 201,
          "createdAt": "2024-07-10T15:13:29Z",
          "updatedAt": "2024-07-10T18:44:44Z"
        }
      }
    ]
  },
  {
    "id": "4a376b40-7ddc-408f-8a0d-7ae630d9207c",
    "createdAt": "2024-07-10T17:32:31Z",
    "updatedAt": "1970-01-01T00:00:00Z",
    "total": 99,
    "items": [
      {
        "id": "8f1c53d9-0605-4ac1-98df-0bf37019f47b",
        "quantity": 10,
        "product": {
          "id": "f2fce9bf-0792-4f34-9ade-8441a74987b8",
          "name": "Casaco",
          "price": 69.9,
          "active": true,
          "stockQty": 201,
          "createdAt": "2024-07-10T15:13:29Z",
          "updatedAt": "2024-07-10T18:44:44Z"
        }
      }
    ]
  },
  {
    "id": "5648ef0f-4e97-476a-a969-4f2e3d05df80",
    "createdAt": "2024-07-10T17:32:33Z",
    "updatedAt": "1970-01-01T00:00:00Z",
    "total": 99,
    "items": [
      {
        "id": "1678084e-17b7-44d1-99d9-fee2fac8e70b",
        "quantity": 10,
        "product": {
          "id": "f2fce9bf-0792-4f34-9ade-8441a74987b8",
          "name": "Casaco",
          "price": 69.9,
          "active": true,
          "stockQty": 201,
          "createdAt": "2024-07-10T15:13:29Z",
          "updatedAt": "2024-07-10T18:44:44Z"
        }
      }
    ]
  }
]
```