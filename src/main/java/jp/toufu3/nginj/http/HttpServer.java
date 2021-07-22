package jp.toufu3.nginj.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import static jp.toufu3.nginj.http.HttpMethod.*;

/**
 * ノンブロッキングHTTP Server
 */

public class HttpServer {

    private final static HttpServer hs = new HttpServer();

    private AsynchronousServerSocketChannel server;

    private final int TIMEOUT = 10;

    public static final Map<HttpMethod,Map<String, Consumer<Context>>> handles =
            Map.of(GET,new LinkedHashMap<>(),
                    POST,new LinkedHashMap<>(),
                    PUT,new LinkedHashMap<>(),
                    UPDATE,new LinkedHashMap<>(),
                    DELETE,new LinkedHashMap<>());

    public static HttpServer get(){
        return hs;
    }

    private void handleRequest(AsynchronousSocketChannel channel) {
        try (AsynchronousSocketChannel acceptedChannel = channel) {

            ByteBuffer buff = ByteBuffer.allocateDirect(8192);

            acceptedChannel.read(buff).get(TIMEOUT, TimeUnit.SECONDS);

            buff.flip();
            byte[] bytes = new byte[buff.limit()];
            buff.get(bytes);
            buff.compact();

            new HttpRequestHandle(buff,new Scanner(new String(bytes)));

            buff.flip();

            acceptedChannel.write(buff).get(TIMEOUT, TimeUnit.SECONDS);

        } catch (InterruptedException | ExecutionException | TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void get(String baseURL,Consumer<Context> ctx){
        handles.get(GET).put(baseURL,ctx);
    }

    public static void post(String baseURL,Consumer<Context> ctx){
        handles.get(POST).put(baseURL, ctx);
    }

    public static void put(String baseURL,Consumer<Context> ctx){
        handles.get(PUT).put(baseURL, ctx);
    }

    public static void update(String baseURL,Consumer<Context> ctx){
        handles.get(UPDATE).put(baseURL, ctx);
    }

    public static void delete(String baseURL,Consumer<Context> ctx){
        handles.get(DELETE).put(baseURL, ctx);
    }

    public void start(int port){

        try {
            server = AsynchronousServerSocketChannel.open();
            server.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            server.bind(new InetSocketAddress(port));

            while(true) {
                Future<AsynchronousSocketChannel> acceptFuture = server.accept();
                try {
                    handleRequest(acceptFuture.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
