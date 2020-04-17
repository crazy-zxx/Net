package com.me;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Mail {

    public static void main(String[] args) throws Exception {

        /**
         * MUA到MTA发送邮件的协议就是SMTP协议，
         * 它是Simple com.me.Mail Transport Protocol的缩写，使用标准端口25，也可以使用加密端口465或587
         * SMTP协议是一个建立在TCP之上的协议，任何程序发送邮件都必须遵守SMTP协议。
         * 使用Java程序发送邮件时，我们无需关心SMTP协议的底层原理，只需要使用JavaMail这个标准API就可以直接发送邮件。
         *
         * 常用邮件服务商的SMTP信息：
         * QQ邮箱：SMTP服务器是smtp.qq.com，端口是465/587；
         * 163邮箱：SMTP服务器是smtp.163.com，端口是465；
         * Gmail邮箱：SMTP服务器是smtp.gmail.com，端口是465/587。
         */

        //使用JavaMail发送邮件
        // 服务器地址:
        String smtp = "smtp.qq.com";
        // 登录用户名:
        String username = "1934109821@qq.com";
        // 登录口令:
        String password = "SSM404NOTFOUND";
        // 连接到SMTP服务器587端口:
        Properties props = new Properties();
        props.put("mail.smtp.host", smtp); // SMTP主机名
        props.put("mail.smtp.port", "587"); // 主机端口号
        props.put("mail.smtp.auth", "true"); // 是否需要用户认证
        props.put("mail.smtp.starttls.enable", "true"); // 启用TLS加密
        // 获取Session实例:
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        // 设置debug模式便于调试:
        session.setDebug(true);

        //发送邮件时，我们需要构造一个Message对象，然后调用Transport.send(Message)即可完成发送
        MimeMessage message = new MimeMessage(session);
        // 设置发送方地址:
        // 绝大多数邮件服务器要求发送方地址和登录用户名必须一致，否则发送将失败。
        message.setFrom(new InternetAddress("1934109821@qq.com"));
        // 设置接收方地址:
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("1934109821@qq.com"));
        // 设置邮件主题:
        message.setSubject("Hello", "UTF-8");
        // 设置邮件正文:
        message.setText("Hi Xiaoming...", "UTF-8");


        //发送HTML邮件和文本邮件是类似的，只需要改为：
        //message.setText(body, "UTF-8", "html");
        //传入的body是类似<h1>Hello</h1><p>Hi, xxx</p>这样的HTML字符串即可。
        //HTML邮件可以在邮件客户端直接显示为网页格式

        /**
         * 要在电子邮件中携带附件，要构造一个Multipart对象
         * 一个Multipart对象可以添加若干个BodyPart，其中第一个BodyPart是文本，即邮件正文，
         * 后面的BodyPart是附件。
         * BodyPart依靠setContent()决定添加的内容，如果添加文本，用setContent("...", "text/plain;charset=utf-8")添加纯文本，
         * 或者用setContent("...", "text/html;charset=utf-8")添加HTML文本。
         * 如果添加附件，需要设置文件名（不一定和真实文件名一致），并且添加一个DataHandler()，传入文件的MIME类型。
         * 二进制文件可以用application/octet-stream，Word文档则是application/msword。
         */
        Multipart multipart = new MimeMultipart();
        // 添加text:
        BodyPart textpart = new MimeBodyPart();
        textpart.setContent("this mail with a file", "text/html;charset=utf-8");
        multipart.addBodyPart(textpart);

        // 添加image附件:
        BodyPart imagepartfile = new MimeBodyPart();
        imagepartfile.setFileName("javamail-file.jpg");
        InputStream input=new FileInputStream(new File("javamail.jpg"));
        imagepartfile.setDataHandler(new DataHandler(new ByteArrayDataSource(input, "application/octet-stream")));
        multipart.addBodyPart(imagepartfile);

        //内嵌图片实际上也是一个附件，即邮件本身也是Multipart，但需要做一点额外的处理
        // 添加text:
        BodyPart textpartimage = new MimeBodyPart();
        textpartimage.setContent("<h1>Hello</h1><p><img src=\"cid:img01\"></p>", "text/html;charset=utf-8");
        multipart.addBodyPart(textpartimage);
        // 添加image:
        BodyPart imagepart = new MimeBodyPart();
        imagepart.setFileName("javamail-in.jpg");
        imagepart.setDataHandler(new DataHandler(new ByteArrayDataSource(input, "image/jpeg")));
        // 与HTML的<img src="cid:img01">关联
        //这个ID和HTML中引用的ID对应起来，邮件客户端就可以正常显示内嵌图片
        imagepart.setHeader("Content-ID", "<img01>");
        multipart.addBodyPart(imagepart);

        // 设置邮件内容为multipart:
        message.setContent(multipart);


        // 发送:
        Transport.send(message);
    }
}