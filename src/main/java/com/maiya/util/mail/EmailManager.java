package com.maiya.util.mail;

import com.lamfire.utils.PropertiesUtils;
import com.sun.mail.smtp.SMTPSendFailedException;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Created by DELL on 2016/12/15.
 */
@Service
public class EmailManager {
    private Properties props; //系统属性
    private Session session; //邮件会话对象
    private MimeMessage mimeMsg; //MIME邮件对象
    private Multipart mp; //Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象

    /**
     * Constructor
     *
     * @param
     */
    public EmailManager() {
        props = System.getProperties();
        props.put("mail.smtp.auth", "false");
        session = Session.getDefaultInstance(props, null);
        session.setDebug(true);
        mimeMsg = new MimeMessage(session);
        mp = new MimeMultipart();
    }

    /**
     * Constructor
     *
     * @param smtp 邮件发送服务器
     */
    public EmailManager(String smtp, String username, String password) {
        props = System.getProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", smtp);
        props.put("username", username);
        props.put("password", password);
        session = Session.getDefaultInstance(props, null);
        session.setDebug(true);
        mimeMsg = new MimeMessage(session);
        mp = new MimeMultipart();
    }

    /**
     * 批量分批分发邮件
     */
    public void sendMailList(String toToken, String subject, String content, String filename) throws InterruptedException, SMTPSendFailedException {
        String from = "frbaobigdata@163.com";
        String[] to = new String[1];
        String[] copyto = {};

        Properties maillist = PropertiesUtils.load("maillist.properties", this.getClass());
        String[] mails = maillist.getProperty(toToken).split("#");

        for (String mail : mails) {
            to[0] = mail;
            System.out.println("开始发送：".concat(to[0]));
            try {
                sendMail(from, to, copyto, subject, content, filename, false);
                //等待1分钟
                Thread.sleep(240000);
            } catch (SMTPSendFailedException e) {
                //发送失败则等待4分钟再发
                Thread.sleep(300000);
                System.out.println("发送失败等待4分钟再发:".concat(to[0]));
                sendMail(from, to, copyto, subject, content, filename, false);
            }
        }
    }

    /**
     * 发送邮件
     */
    public boolean sendMail(String from, String[] to, String[] copyto, String subject, String content, String filename, boolean fileF) throws SMTPSendFailedException {
        try {
            //设置发信人
            mimeMsg.setFrom(new InternetAddress(from));
            //设置接收人
            for (String aTo : to) {
                mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(aTo));
            }
            //设置抄送人
            for (String aCopyto : copyto) {
                mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(aCopyto));
            }
            //设置主题
            mimeMsg.setSubject(subject);
            //设置正文
            BodyPart bp = new MimeBodyPart();
            bp.setContent(content, "text/html;charset=utf-8");
            //发送前删除，否则会导致发送重复数据
            for (int i = 0; i < mp.getCount(); i++) {
                mp.removeBodyPart(i);
            }
            mp.addBodyPart(bp);
            //设置附件
            if (fileF) {
                bp = new MimeBodyPart();
                FileDataSource fileds = new FileDataSource(filename);
                bp.setDataHandler(new DataHandler(fileds));
                bp.setFileName(MimeUtility.encodeText(fileds.getName(), "UTF-8", "B"));
                //是否发送附件
                mp.addBodyPart(bp);
            }
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
            //发送邮件
            if (props.get("mail.smtp.auth").equals("true")) {
                Transport transport = session.getTransport("smtp");
                transport.connect((String) props.get("mail.smtp.host"), (String) props.get("username"), (String) props.get("password"));
                transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
                try {
                    transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.CC));
                } catch (NullPointerException e) {
                    System.out.println("未配置抄送人.");
                }
                transport.close();
            } else {
                Transport.send(mimeMsg);
            }
            System.out.println("邮件发送成功");
        } catch (SMTPSendFailedException e) {
            throw e;
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

}
