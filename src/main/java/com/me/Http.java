package com.me;

public class Http {

    public static void main(String[] args) {

        //HTTP是HyperText Transfer Protocol的缩写，翻译为超文本传输协议，它是基于TCP协议之上的一种请求-响应协议。
        //当浏览器希望访问某个网站时，浏览器和网站服务器之间首先建立TCP连接，且服务器总是使用80端口和加密端口443，
        // 然后，浏览器向服务器发送一个HTTP请求，服务器收到后，返回一个HTTP响应，并且在响应中包含了HTML的网页内容，
        // 这样，浏览器解析HTML后就可以给用户显示网页了

        /**
         * HTTP请求的格式是固定的，它由HTTP Header和HTTP Body两部分构成。
         * 第一行总是请求方法 路径 HTTP版本，例如，GET / HTTP/1.1表示使用GET请求，路径是/，版本是HTTP/1.1。
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


    }
}
