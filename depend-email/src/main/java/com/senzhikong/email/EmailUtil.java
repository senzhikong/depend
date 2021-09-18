package com.senzhikong.email;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.senzhikong.util.string.StringUtil;
import com.sun.mail.imap.IMAPMessage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;
import javax.mail.search.MessageIDTerm;
import javax.mail.search.SearchTerm;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmailUtil {
    public static String sendMail(EmailRequest emailRequest) {
        // 配置发送邮件的环境属性
        final Properties props = new Properties();
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp." + emailRequest.getEmailHost());
        props.put("mail.smtp.port", emailRequest.getSendPort());
        if (emailRequest.getIsSSL()) {
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.port", emailRequest.getSendSslPort());
            props.put("mail.smtp.port", emailRequest.getSendSslPort());
        }
        props.put("mail.user", emailRequest.getFromEmail());
        props.put("mail.password", emailRequest.getFromPassword());
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        MimeMessage message = new MimeMessage(mailSession);
        try {
            String nick = emailRequest.getFromName();
            nick = MimeUtility.encodeText(nick);
            InternetAddress from = new InternetAddress(nick + "<" + emailRequest.getFromEmail() + ">");
            message.setFrom(from);
            InternetAddress[] toUsers = new InternetAddress[emailRequest.getToList()
                    .size()];
            List<InternetAddress> tos = new ArrayList<>();
            for (String t : emailRequest.getToList()) {
                tos.add(new InternetAddress(t));
            }
            toUsers = tos.toArray(toUsers);
            message.setRecipients(MimeMessage.RecipientType.TO, toUsers);
            message.setSubject(emailRequest.getSubject());
            message.setContent(emailRequest.getHtmlMsg(), "text/html;charset=UTF-8");
            Transport.send(message);
        } catch (Exception e) {
            return "邮件发送失败：" + e.getMessage();
        }
        return null;
    }

    public static Store getImapStore(EmailRequest emailRequest) throws Exception {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imap");
        props.put("mail.imap.auth", "true");
        props.put("mail.imap.host", "imap." + emailRequest.getEmailHost());
        props.put("mail.imap.port", "143");
        props.put("mail.user", emailRequest.getFromEmail());
        props.put("mail.password", emailRequest.getFromPassword());
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(props, authenticator);
        // 创建IMAP协议的Store对象
        Store store = session.getStore("imap");
        // 连接邮件服务器
        store.connect(emailRequest.getFromEmail(), emailRequest.getFromPassword());
        return store;
    }

    public static List<ReceiveEmail> receiveEmail(EmailRequest emailRequest, SearchTerm term) {
        List<ReceiveEmail> emails = new ArrayList<>();
        try {
            // 创建IMAP协议的Store对象
            Store store = getImapStore(emailRequest);
            // 获得收件箱
            Folder folder = store.getFolder("INBOX");
            // 以读写模式打开收件箱
            folder.open(Folder.READ_WRITE);
            // 获得收件箱的邮件列表
            Message[] messages;
            if (term != null) {
                messages = folder.search(term);
            } else {
                messages = folder.getMessages();
            }

            // 解析邮件
            for (Message message : messages) {
                IMAPMessage msg = (IMAPMessage) message;
                ReceiveEmail email = parseEmail(msg);
                email.setReceiveEmail(emailRequest.getFromEmail());
                emails.add(email);
            }
            // 关闭资源
            folder.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emails;
    }

    public static List<ReceiveEmail> receiveUnread(EmailRequest emailRequest) {
        FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false); //false代表未读，true代表已读
        return receiveEmail(emailRequest, ft);
    }

    public static ReceiveEmail parseEmail(MimeMessage msg) throws Exception {
        ReceiveEmail email = new ReceiveEmail();
        String messageId = decodeText(msg.getMessageID());
        email.setMessageId(messageId);
        //邮件主推
        String subject = decodeText(msg.getSubject());
        email.setSubject(subject);
        //发件人
        Address[] froms = msg.getFrom();
        InternetAddress address = (InternetAddress) froms[0];
        String fromEmail = address.getAddress();
        String fromName = decodeText(address.getPersonal());
        email.setFromEmail(fromEmail);
        email.setFromName(fromName);
        //收件人
        List<String> toList = new ArrayList<String>();
        Address[] addresses = msg.getAllRecipients();
        for (Address add : addresses) {
            InternetAddress internetAddress = (InternetAddress) add;
            toList.add(internetAddress.toUnicodeString());
        }
        email.setToList(toList);
        //发送时间
        email.setSendTime(msg.getSentDate());
        //是否已读
        email.setRead(msg.getFlags()
                .contains(Flags.Flag.SEEN));
        //是否需要回执
        email.setReplySign(msg.getHeader("Disposition-Notification-To") != null);
        //优先级
        String priority = "Normal";
        String priorityDesc = "普通";
        String[] headers = msg.getHeader("X-Priority");
        if (headers != null) {
            String headerPriority = headers[0];
            if (headerPriority.indexOf("1") != -1 || headerPriority.indexOf("High") != -1) {
                priority = "High";
                priorityDesc = "普通";
            } else if (headerPriority.indexOf("5") != -1 || headerPriority.indexOf("Low") != -1) {
                priority = "Low";
                priorityDesc = "低";
            }
        }
        email.setPriority(priority);
        email.setPriorityDesc(priorityDesc);
        //文本内容
        StringBuffer contentText = new StringBuffer();
        StringBuffer contentHtml = new StringBuffer();
        getMailTextContent(msg, contentHtml, contentText);
        email.setHtmlMsg(contentHtml.toString());
        email.setTextMsg(contentText.toString());
        //邮件大小
        email.setSize(msg.getSize());
        JSONArray attachmentFiles = new JSONArray();
        listAttachment(msg, "root", attachmentFiles);
        email.setAttachmentFiles(attachmentFiles);
        for (int i = 0; i < attachmentFiles.size(); i++) {
            JSONObject file = attachmentFiles.getJSONObject(i);
            file.put("messageId", email.getMessageId());
        }
        return email;
    }


    /**
     * 获得邮件文本内容
     *
     * @param part
     * @param contentHtml
     * @param contentText
     * @throws Exception
     */
    public static void getMailTextContent(Part part, StringBuffer contentHtml, StringBuffer contentText)
            throws Exception {
        //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
        boolean isContainTextAttach = part.getContentType()
                .indexOf("name") > 0;
        if (part.isMimeType("text/plain") && !isContainTextAttach) {
            contentText.append(part.getContent()
                    .toString());
        } else if (part.isMimeType("text/html") && !isContainTextAttach) {
            contentHtml.append(part.getContent()
                    .toString());
        } else if (part.isMimeType("message/rfc822")) {
            getMailTextContent((Part) part.getContent(), contentHtml, contentText);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                getMailTextContent(bodyPart, contentHtml, contentText);
            }
        }
    }

    public static void listAttachment(Part part, String pos, JSONArray files) throws Exception {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();    //复杂体邮件
            //复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                //某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disp = bodyPart.getDisposition();
                String filePos = pos + "," + i;
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    files.add(getAttachment(bodyPart, filePos));
                } else if (bodyPart.isMimeType("multipart/*")) {
                    listAttachment(bodyPart, filePos, files);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
                        files.add(getAttachment(bodyPart, filePos));
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            listAttachment((Part) part.getContent(), pos + ",0", files);
        }
    }

    private static JSONObject getAttachment(BodyPart bodyPart, String filePos) throws Exception {
        JSONObject file = new JSONObject();
        String fileName = decodeText(bodyPart.getFileName());
        if (StringUtil.isEmpty(fileName))
            fileName = "unknow" + System.currentTimeMillis();
        file.put("pos", filePos);
        file.put("fileName", fileName);
        file.put("size", bodyPart.getSize());
        return file;
    }

    public static void downloadAttachment(EmailRequest emailRequest, JSONObject file, OutputStream out)
            throws Exception {
        downloadAttachment(emailRequest, file, out, null);
    }

    public static void downloadAttachment(EmailRequest emailRequest, JSONObject file, OutputStream out,
                                          HttpServletResponse response) throws Exception {
        try {
            // 创建IMAP协议的Store对象
            Store store = getImapStore(emailRequest);
            // 获得收件箱
            Folder folder = store.getFolder("INBOX");
            // 以读写模式打开收件箱
            folder.open(Folder.READ_WRITE);
            // 获得收件箱的邮件列表
            MessageIDTerm term = new MessageIDTerm(file.getString("messageId")); //false代表未读，true代表已读
            Message msg = folder.search(term)[0];
            String pos[] = file.getString("pos")
                    .replace("root,", "")
                    .split(",");

            Part part = msg;
            for (String p : pos) {
                if (part.isMimeType("multipart/*")) {
                    Multipart multipart = (Multipart) part.getContent();    //复杂体邮件
                    part = multipart.getBodyPart(Integer.parseInt(p));
                } else if (part.isMimeType("message/rfc822")) {
                    part = (Part) part.getContent();
                }
            }
            BufferedInputStream bis = new BufferedInputStream(part.getInputStream());
            if (response != null) {
                response.addHeader("Content-Length", "" + bis.available());
            }
            BufferedOutputStream bos = new BufferedOutputStream(out);
            byte[] bytes = new byte[2048];
            int len = 0;
            while ((len = bis.read(bytes)) > 0) {
                bos.write(bytes, 0, len);
            }
            bis.close();
            bos.flush();
            bos.close();
            // 关闭资源
            folder.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文本解码
     *
     * @param encodeText 解码MimeUtility.encodeText(String text)方法编码后的文本
     * @return 解码后的文本
     * @throws UnsupportedEncodingException
     */
    public static String decodeText(String encodeText) throws UnsupportedEncodingException {
        if (encodeText == null || "".equals(encodeText)) {
            return "";
        } else {
            return MimeUtility.decodeText(encodeText);
        }
    }

}
