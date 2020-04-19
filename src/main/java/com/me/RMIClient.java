package com.me;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;

public class RMIClient {

    public static void main(String[] args) throws Exception {

        /**
         * Java的RMI远程调用是指，一个JVM中的代码可以通过网络实现远程调用另一个JVM的某个方法。
         * RMI是Remote Method Invocation的缩写。
         *
         * 提供服务的一方我们称之为服务器，而实现远程调用的一方我们称之为客户端。
         *
         * 要实现RMI，服务器和客户端必须共享同一个接口。
         * Java的RMI规定此接口必须派生自java.rmi.Remote，并在每个方法声明抛出RemoteException。
         *
         * 客户端只有接口，并没有实现类，因此，客户端获得的接口方法返回值实际上是通过网络从服务器端获取的
         * 对客户端来说，客户端持有的WorldClock接口实际上对应了一个“实现类”，它是由Registry内部动态生成的，并负责把方法调用通过网络传递到服务器端。
         * 而服务器端接收网络调用的服务并不是我们自己编写的WorldClockService，而是Registry自动生成的代码
         *
         * 我们把客户端的“实现类”称为stub，而服务器端的网络服务类称为skeleton，
         * 它会真正调用服务器端的WorldClockService，获取结果，然后把结果通过网络传递给客户端。
         *
         * Java的RMI严重依赖序列化和反序列化，而这种情况下可能会造成严重的安全漏洞，
         * 使用RMI时，双方必须是内网互相信任的机器，不要把1099端口暴露在公网上作为对外服务。
         *
         * Java的RMI调用机制决定了双方必须是Java程序，其他语言很难调用Java的RMI。
         * 如果要使用不同语言进行RPC调用，可以选择更通用的协议，例如gRPC。
         */
        // 连接到服务器localhost，端口1099:
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        // 查找名称为"WorldClock"的服务并强制转型为WorldClock接口:
        WorldClock worldClock = (WorldClock) registry.lookup("WorldClock");
        // 正常调用接口方法:
        LocalDateTime now = worldClock.getLocalDateTime("Asia/Shanghai");
        // 打印调用结果:
        System.out.println(now);



    }

}
