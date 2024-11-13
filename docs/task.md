# Task API Spec

## Create Task

Endpoint : POST /api/tasks

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
  "name" : "name",
  "isCompleted" : "complete"
}
```

Response Body (Success) : 

```json
{
  "data": {
    "id" : "random-string",
    "name" : "name",
    "isCompleted" : "complete"
  }
}
```

Response Body (Failed) :

```json
{
  "errors" : "error"
}
```

## Update Task

Endpoint : PUT /api/tasks/{idTask}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Request Body :

```json
{
  "name" : "name",
  "isCompleted" : "complete"
}
```

Response Body (Success) :

```json
{
  "data": {
    "id" : "random-string",
    "name" : "name",
    "isCompleted" : "complete"
  }
}
```

Response Body (Failed) :

```json
{
  "errors" : "Invalid input data"
}
```

## Get Task

Endpoint : GET /api/tasks/{idTask}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "data": {
    "id" : "random-string",
    "name" : "name",
    "isCompleted" : "complete"
  }
}
```

Response Body (Failed, 404) :

```json
{
  "errors" : "Task is not found"
}
```

## Search Task

Endpoint : GET /api/tasks

Query Param :

- name : String, task name, using like query, optional
- isCompleted : Boolean, task completion status, optional
- page : Integer, start from 0, default 0
- size : Integer, default 10

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
    "data": [
        {
            "id": "random-string",
            "name": "task name",
            "isCompleted": "complete"
        }
    ],
    "paging" : {
        "currentPage" : 0,
        "totalPage" : 10,
        "size" : 10
    }
}
```

## Remove Task

Endpoint : DELETE /api/tasks/{idTask}

Request Header :

- X-API-TOKEN : Token (Mandatory)

Response Body (Success) :

```json
{
  "data" : "OK"
}
```

Response Body (Failed) :

```json
{
  "errors" : "Task is not found"
}
```