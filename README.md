# GraphQL Sandbox

The intent of this project is to learn how we can cleanly call GraphQL libraries from Java.

## Building
It's a Maven project.  Simply run `mvn clean install`


## Configuration
GitHub does not allow anonymous access to the V4 API.  You can add this in the `test.properties` file


## Source Generation
The `apollo-client-maven-plugin` is used to build Java objects based on GraphQL schema & query files.  For example the 
`source.graphql` file contains:  
```
query Source($repoName:String!, $repoOwner:String!, $blob:String!){
     repository(owner: $repoOwner, name: $repoName) {
       content:object(expression: $blob) {
         ... on Blob {
           text
         }
       }
     }
   }
```
This is a GraphQL query that will fetch the file contents based on the repository, branch, and path.  When the Maven
plugin runs it will generate objects to build this query in a type safe way.  For example:
```
BlameQuery query = BlameQuery.builder()
  .repoOwner(repoOwner)
  .repoName(repoName)
  .ref(ref)
  .path(path)
  .blob(ref + ":" + path)
  .build();
```

Note: If you want to play around with GraphQL and learn how to create GraphQL queries you can check out the 
[GitHub Explorer](https://developer.github.com/v4/explorer/)


## Creating the Client
These query objects are used with the Apollo Client, which uses the OkHTTP Client.
```
OkHttpClient okHttpClient = new OkHttpClient.Builder()
  .addInterceptor(chain -> chain.proceed(chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build()))
  .build();
   
ApolloClient client = ApolloClient.builder()
  .serverUrl(url)
  .okHttpClient(okHttpClient)
  .build();
```


## Executing Query
This client can then be called with the following:
```
client.query(query).enqueue(new ApolloCall.Callback<Optional<BlameQuery.Data>>() {
    @Override
    public void onResponse(@NotNull Response<Optional<BlameQuery.Data>> response) {
      . . .
    }
    @Override
    public void onFailure(@NotNull ApolloException e) {
      . . .
    }
  }
);
```
