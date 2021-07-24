# NginJ
Java Non blocking IO Http Web Framework

### Receive Get Request
```java
import jp.toufu3.nginj.http.HttpServer;
import jp.toufu3.nginj.http.ParseJSON;

HttpServer.get("/",(ctx)->{
    ctx.res.setBody("Hello, world!");
});
//80 TCP 80 port receive
HttpServer.get().start(80);
```

### Receive Post Request
```java
import jp.toufu3.nginj.http.HttpServer;

HttpServer.post("/",(ctx)->{
    ctx.res.setBody("Hello, world!");
});
//80 TCP 80 port receive
HttpServer.get().start(80);
```

### Get Path Params
```java
import jp.toufu3.nginj.http.HttpServer;

//Path param name is 'id'
HttpServer.get("/route/:id",(ctx)->{
    String pathParam = ctx.req.getPathParams().getOrDefault("id","empty");
    ctx.res.setBody(pathParam);
});
//TCP 80 port receive
HttpServer.get().start(80);
```

### Get FormParams
```java
import jp.toufu3.nginj.http.HttpServer;

HttpServer.get("/aaa",(ctx)->{
    Map<String,String> formParams = ctx.req.getFormParams();
});
//TCP 80 port receive
HttpServer.get().start(80);
```

### Convert Java instance variable convert to JSON
```java
import jp.toufu3.nginj.http.HttpServer;

HttpServer.post("/",(ctx)->{
    ctx.res.setJSON(Map.of("A",List.of(1,2,3)));
});
//TCP 80 port receive
HttpServer.get().start(80);
```

### Other
import jp.toufu3.nginj.http.HttpServer;
import jp.toufu3.nginj.http.StatusCode;

import java.util.Map;
import java.util.UUID;

HttpServer.get("/aaa",(ctx)->{
    //Client has cookie.
    Map<String, String> cookie = ctx.req.getCookie();
            
    //Server set new Cookie
    ctx.res.setCookie("Session", UUID.randomUUID().toString());

    //Set Status code -> 404
    ctx.res.setStatusCode(StatusCode.NotFound);
});
HttpServer.get().start(80);
```
