package com.me;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class Http {

    //创建一个全局HttpClient实例，因为HttpClient内部使用线程池优化多个HTTP连接，可以复用
    static HttpClient httpClient = HttpClient.newBuilder().build();

    public static void main(String[] args) throws Exception {

        //HTTP是HyperText Transfer Protocol的缩写，翻译为超文本传输协议，它是基于TCP协议之上的一种请求-响应协议。
        //当浏览器希望访问某个网站时，浏览器和网站服务器之间首先建立TCP连接，且服务器总是使用80端口和加密端口443，
        // 然后，浏览器向服务器发送一个HTTP请求，服务器收到后，返回一个HTTP响应，并且在响应中包含了HTML的网页内容，
        // 这样，浏览器解析HTML后就可以给用户显示网页了

        /**
         * HTTP请求的格式是固定的，它由HTTP Header和HTTP Body两部分构成。
         * 第一行总是: 请求方法  路径  HTTP版本，例如，GET / HTTP/1.1表示使用GET请求，路径是/，版本是HTTP/1.1。
         * 后续的每一行都是固定的Header: Value格式，我们称为HTTP Header，服务器依靠某些特定的Header来识别客户端请求:
         */
         // Host：表示请求的域名，因为一台服务器上可能有多个网站，因此有必要依靠Host来识别用于请求；
         // User-Agent：表示客户端自身标识信息，不同的浏览器有不同的标识，服务器依靠User-Agent判断客户端类型；
         // Accept：表示客户端能处理的HTTP响应格式，*/*表示任意格式，text/*表示任意文本，image/png表示PNG格式的图片；
         // Accept-Language：表示客户端接收的语言，多种语言按优先级排序，服务器依靠该字段给用户返回特定语言的网页版本。

        /**
         * 如果是GET请求，那么该HTTP请求只有HTTP Header，没有HTTP Body。
         * 如果是POST请求，那么该HTTP请求带有Body，以一个空行分隔。
         *
         * POST请求通常要设置Content-Type表示Body的类型，Content-Length表示Body的长度，
         * 这样服务器就可以根据请求的Header和Body做出正确的响应。
         *
         * GET请求的参数必须附加在URL上，并以URLEncode方式编码
         * 因为URL的长度限制，GET请求的参数不能太多，而POST请求的参数就没有长度限制，因为POST请求的参数必须放到Body中。
         * 并且，POST请求的参数不一定是URL编码，可以按任意格式编码，只需要在Content-Type中正确设置即可。
         */

        /**
         * HTTP响应也是由Header和Body两部分组成
         * 响应的第一行总是: HTTP版本  响应代码  响应说明，例如，HTTP/1.1 200 OK,表示版本是HTTP/1.1，响应代码是200，响应说明是OK。
         * 客户端只依赖响应代码判断HTTP响应是否成功
         *
         * HTTP有固定的响应代码：
         *     1xx：表示一个提示性响应，例如101表示将切换协议，常见于WebSocket连接；
         *     2xx：表示一个成功的响应，例如200表示成功，206表示只发送了部分内容；
         *     3xx：表示一个重定向的响应，例如301表示永久重定向，303表示客户端应该按指定路径重新发送请求；
         *     4xx：表示一个因为客户端问题导致的错误响应，例如400表示因为Content-Type等各种原因导致的无效请求，404表示指定的路径不存在；
         *     5xx：表示一个因为服务器问题导致的错误响应，例如500表示服务器内部故障，503表示服务器暂时无法响应。
         */

        //Java使用HTTP客户端编程
        //
        //Java标准库提供了基于HTTP的包，但是要注意，早期的JDK版本是通过HttpURLConnection访问HTTP
        //代码编写比较繁琐，并且需要手动处理InputStream，所以用起来很麻烦
        URL url = new URL("https://www.baidu.com/");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setUseCaches(false);
        conn.setConnectTimeout(5000); // 请求超时5秒
        // 设置HTTP头:
        conn.setRequestProperty("Accept", "*/*");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 11; Windows NT 5.1)");
        // 连接并发送HTTP请求:
        conn.connect();
        // 判断HTTP响应是否200:
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("bad response");
        }
        // 获取所有响应Header:
        Map<String, List<String>> map = conn.getHeaderFields();
        for (String key : map.keySet()) {
            System.out.println(key + ": " + map.get(key));
        }
        // 获取响应内...
        InputStream input = conn.getInputStream();

        System.out.println("==============================");


        //从Java 11开始，引入了新的HttpClient，它使用链式调用的API，能大大简化HTTP的处理。
        //首先需要创建一个全局HttpClient实例，因为HttpClient内部使用线程池优化多个HTTP连接，可以复用
        //static HttpClient httpClient = HttpClient.newBuilder().build();
        String url1 = "https://www.sina.com.cn/";
        HttpRequest request = HttpRequest.newBuilder(new URI(url1))
                // 设置Header:
                .header("User-Agent", "Java HttpClient").header("Accept", "*/*")
                // 设置超时:
                .timeout(Duration.ofSeconds(5))
                // 设置版本:
                .version(HttpClient.Version.HTTP_2).build();
        //要获取图片这样的二进制内容，只需要把HttpResponse.BodyHandlers.ofString()换成HttpResponse.BodyHandlers.ofByteArray()，就可以获得一个HttpResponse<byte[]>对象。
        //如果响应的内容很大，不希望一次性全部加载到内存，可以使用HttpResponse.BodyHandlers.ofInputStream()获取一个InputStream流。
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        // HTTP允许重复的Header，因此一个Header可对应多个Value:
        Map<String, List<String>> headers = response.headers().map();
        for (String header : headers.keySet()) {
            System.out.println(header + ": " + headers.get(header).get(0));
        }
        System.out.println(response.body().substring(0, 1024) + "...");

        System.out.println("==============================");

        //要使用POST请求，我们要准备好发送的Body数据并正确设置Content-Type
        String url2 = "http://www.baidu.com/";
        String body = "username=bob&password=123456";
        HttpRequest requestp = HttpRequest.newBuilder(new URI(url2))
                // 设置Header:
                .header("Accept", "*/*")
                .header("Content-Type", "application/x-www-form-urlencoded")
                // 设置超时:
                .timeout(Duration.ofSeconds(5))
                // 设置版本:
                .version(HttpClient.Version.HTTP_2)
                // 使用POST并设置Body:
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> responsep = httpClient.send(requestp, HttpResponse.BodyHandlers.ofString());
        String s = responsep.body();
        System.out.println(s);

        System.out.println("==============================");



    }
}
