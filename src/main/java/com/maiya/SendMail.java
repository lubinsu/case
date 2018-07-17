package com.maiya;

import com.maiya.util.mail.EmailManager;
import com.sun.mail.smtp.SMTPSendFailedException;

/**
 * Created by lubin 2017/3/20
 */
public class SendMail {
    public static void main(String[] args) throws SMTPSendFailedException {
        String smtp = "smtp.163.com";
        String username = "frbaobigdata@163.com";
        String password = "psaux1234";
        EmailManager email = new EmailManager(smtp, username, password);
        String from = "frbaobigdata@163.com";
        String[] to = new String[1];
        String[] copyto = {};

        to[0] = args[0];

        email.sendMail(from, to, copyto, "邮件日报", "邮件内容如下。。。。", "bigdata.properties", true);
    }
}
