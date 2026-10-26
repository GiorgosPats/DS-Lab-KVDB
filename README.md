Key_Value Database
===

-------

List all entries:

```shell
curl -X GET http://localhost:8080/kv ; echo
```

-------

Get entry:

```shell
curl -X GET http://localhost:8080/kv/name ; echo 
```

--------

Insert entry:

```shell
curl -X POST http://localhost:8080/kv/giorgos -d "it2022153@hua.gr" ; echo
```

---------

Update entry:

```shell
curl -X PATCH http://localhost:8080/kv/version -H "Content-Type: text/plain" -d "0.1.1" ; echo
```


---------

Upsert entry:

```shell
curl -X PUT http://localhost:8080/kv/version -H "Content-Type: text/plain" -d "0.1.1" ; echo
```

-------

Delete entry:

```shell
curl -X DELETE http://localhost:8080/kv/foo ; echo 
```
