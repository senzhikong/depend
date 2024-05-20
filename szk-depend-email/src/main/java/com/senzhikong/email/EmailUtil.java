package com.senzhikong.email;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.mail.imap.IMAPMessage;
import org.apache.commons.lang3.StringUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author shu
 */
public class EmailUtil {
    /**
     * 发送邮件
     *
     * @param emailRequest 邮件
     * @return 发送结果
     */
    public static String sendMail(EmailRequest emailRequest) {
        // 配置发送邮件的环境属性
        final Properties props = new Properties();
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp." + emailRequest.getEmailHost());
        props.put("mail.smtp.port", emailRequest.getSendPort());
        if (emailRequest.isSsl()) {
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.port", emailRequest.getSendSslPort());
            props.put("mail.smtp.port", emailRequest.getSendSslPort());
        }
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = getAuthSession(props, emailRequest);
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

    public static Session getAuthSession(Properties props, EmailRequest emailRequest) {
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
        return Session.getInstance(props, authenticator);
    }

    public static Store getImapStore(EmailRequest emailRequest) throws Exception {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imap");
        props.put("mail.imap.auth", "true");
        props.put("mail.imap.host", "imap." + emailRequest.getEmailHost());
        props.put("mail.imap.port", "143");
        Session session = getAuthSession(props, emailRequest);
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
        //false代表未读，true代表已读
        FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
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
        List<String> toList = new ArrayList<>();
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
            String headerPriority = headers[0].toLowerCase();
            if (headerPriority.contains(EmailPriority.HIGH.level()) || headerPriority.contains(
                    EmailPriority.HIGH.code().toLowerCase())) {
                priority = EmailPriority.HIGH.code();
                priorityDesc = EmailPriority.HIGH.desc();
            } else if (headerPriority.contains(EmailPriority.LOW.level()) || headerPriority.contains(
                    EmailPriority.LOW.code().toLowerCase())) {
                priority = EmailPriority.LOW.code();
                priorityDesc = EmailPriority.LOW.desc();
            }
        }
        email.setPriority(priority);
        email.setPriorityDesc(priorityDesc);
        //文本内容
        StringBuilder contentText = new StringBuilder();
        StringBuilder contentHtml = new StringBuilder();
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

    private static final String EMAIL_HTML = "text/html";
    private static final String EMAIL_TEXT = "text/plain";
    private static final String EMAIL_MESSAGE = "message/rfc822";
    private static final String EMAIL_MULTIPART = "multipart/*";

    /**
     * 获得邮件文本内容
     *
     * @param part        邮件模块
     * @param contentHtml 富文本
     * @param contentText 纯文本
     * @throws Exception 解析异常
     */
    public static void getMailTextContent(Part part, StringBuilder contentHtml, StringBuilder contentText)
            throws Exception {
        //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
        boolean isContainTextAttach = part.getContentType()
                .indexOf("name") > 0;
        if (part.isMimeType(EMAIL_TEXT) && !isContainTextAttach) {
            contentText.append(part.getContent()
                    .toString());
        } else if (part.isMimeType(EMAIL_HTML) && !isContainTextAttach) {
            contentHtml.append(part.getContent()
                    .toString());
        } else if (part.isMimeType(EMAIL_MESSAGE)) {
            getMailTextContent((Part) part.getContent(), contentHtml, contentText);
        } else if (part.isMimeType(EMAIL_MULTIPART)) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                getMailTextContent(bodyPart, contentHtml, contentText);
            }
        }
    }

    public static void listAttachment(Part part, String pos, JSONArray files) throws Exception {
        if (part.isMimeType(EMAIL_MULTIPART)) {
            //复杂体邮件
            Multipart multipart = (Multipart) part.getContent();
            //复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                //某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disposition = bodyPart.getDisposition();
                String filePos = pos + "," + i;
                if (disposition.equalsIgnoreCase(Part.ATTACHMENT) || disposition.equalsIgnoreCase(Part.INLINE)) {
                    files.add(getAttachment(bodyPart, filePos));
                } else if (bodyPart.isMimeType(EMAIL_MULTIPART)) {
                    listAttachment(bodyPart, filePos, files);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.contains("name") || contentType.contains("application")) {
                        files.add(getAttachment(bodyPart, filePos));
                    }
                }
            }
        } else if (part.isMimeType(EMAIL_MULTIPART)) {
            listAttachment((Part) part.getContent(), pos + ",0", files);
        }
    }

    /**
     * 获取邮箱附件
     *
     * @param bodyPart 主体
     * @param filePos  文件位置
     * @return 文件主体
     * @throws Exception 解析异常
     */
    private static JSONObject getAttachment(BodyPart bodyPart, String filePos) throws Exception {
        JSONObject file = new JSONObject();
        String fileName = decodeText(bodyPart.getFileName());
        if (StringUtils.isBlank(fileName)) {
            fileName = "unknown" + System.currentTimeMillis();
        }
        file.put("pos", filePos);
        file.put("fileName", fileName);
        file.put("size", bodyPart.getSize());
        return file;
    }

    /**
     * 文本解码
     *
     * @param encodeText 解码MimeUtility.encodeText(String text)方法编码后的文本
     * @return 解码后的文本
     * @throws UnsupportedEncodingException 不支持的编码
     */
    public static String decodeText(String encodeText) throws UnsupportedEncodingException {
        if (encodeText == null || "".equals(encodeText)) {
            return "";
        } else {
            return MimeUtility.decodeText(encodeText);
        }
    }

}
