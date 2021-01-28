Lego shop API demo

Notes:
* This is a spring boot application and has been coded contract first using OpenAPI and the openapi codegen maven plugin. The designed API is available in src/main/resources/api.yaml.


* In order to run the application execute the following:

    ```mvn clean install spring-boot:run```

    The server starts on port 8080. If you visit localhost:8080 you will be presented with the swagger-ui documentation of the API. All the endpoints can be called from within swagger ui.


* Items for sale in the shop are listed in the application.yaml file, and the list can be extended there. 


* The api implementation is the primary code, and is located in src/main/java/com/applicativeconsult/legoshop/api/controller/ShopController.java
The rest of the code is basically configuration.
  

* No persistence is used, i.e. its mock data that you are operating on.
