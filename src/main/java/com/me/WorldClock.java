package com.me;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

//Java的RMI规定此接口必须派生自java.rmi.Remote，并在每个方法声明抛出RemoteException。
public interface WorldClock extends Remote {

    LocalDateTime getLocalDateTime(String zoneId) throws RemoteException;

}
