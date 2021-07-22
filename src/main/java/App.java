import jp.toufu3.nginj.http.HttpServer;
import jp.toufu3.nginj.http.ParseJSON;

import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        HttpServer.get("/route/:id",(ctx)->{
            ctx.res.setJSON(Map.of("pathParams",ctx.req.getPathParams().getOrDefault("id","empty")));
        });
        HttpServer.get().start(80);
    }
}
