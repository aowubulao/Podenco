package com.neoniou.bot.message.handler;

import cn.hutool.http.HttpRequest;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Neo.Zzj
 * @date 2020/12/13
 */
public abstract class MessageHandler {

    private static final int ZERO = 0;
    public static final int REGEX = 1;
    public static final int EQUAL = 2;
    public static final int CONTAINS = 3;

    private static final String IMAGE_REGEX = "(\\{).*?(-).*?(}.jpg)";
    private static final String IMAGE_PNG_REGEX = "(\\{).*?(-).*?(}.png)";
    private static final Pattern IMAGE_PATTERN = Pattern.compile(IMAGE_REGEX);
    private static final Pattern IMAGE_PNG_PATTERN = Pattern.compile(IMAGE_PNG_REGEX);

    private static final String IMAGE_SYMBOL = "mirai:image:";

    private static long groupId = 0L;

    private long[] permitFriends = new long[0];

    private long[] permitGroups = new long[0];

    private String matchString = "";

    private int matchRule = 0;

    /**
     * 处理 Mirai MessageEvent，获取消息
     *
     * @param event MessageEvent
     * @return Message Body 原消息
     */
    public static String getMessageBody(MessageEvent event) {
        Object[] message = event.getMessage().toArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < message.length; i++) {
            sb.append(message[i]);
        }
        return sb.toString();
    }

    /**
     * 验证消息是否匹配
     *
     * @param srcMsg   发送的源消息
     * @param str      需要匹配的字符串
     * @param matching 匹配规则 1:正则 2:相等 3:包含
     * @return true or false
     */
    public static boolean isMatch(String srcMsg, String str, int matching) {
        if (matching == ZERO) {
            return true;
        }
        if (matching == REGEX) {
            return srcMsg.matches(str);
        }
        if (matching == EQUAL) {
            return srcMsg.equals(str);
        }
        if (matching == CONTAINS) {
            return srcMsg.contains(str);
        }
        return false;
    }

    public static boolean containsImage(MessageEvent event) {
        return getMessageBody(event).contains(IMAGE_SYMBOL);
    }

    /**
     * 根据 url 获取图片 inputStream
     *
     * @param url 图片链接
     * @return 输入流
     */
    public static InputStream getImageFile(String url) {
        return HttpRequest.get(url)
                .execute()
                .bodyStream();
    }

    /**
     * 根据 message body提取图片链接
     *
     * @param event MessageEvent
     * @return image url
     */
    public static String getImageUrl(MessageEvent event) {
        String messageBody = getMessageBody(event);
        Matcher jpgMatcher = IMAGE_PATTERN.matcher(messageBody);
        Matcher pngMatcher = IMAGE_PNG_PATTERN.matcher(messageBody);

        if (jpgMatcher.find()) {
            String imageId = jpgMatcher.group();
            return getImageUrl(imageId);
        } else if (pngMatcher.find()) {
            String imageId = pngMatcher.group();
            return getImageUrl(imageId);
        } else {
            return "";
        }
    }

    public static String getImageUrl(String imageId) {
        return Image.queryUrl(Image.fromId(imageId));
    }

    /**
     * 根据图片链接上传图片获取图片对象
     *
     * @param imageUrl 图片链接
     * @param event    MessageEvent
     * @return Image对象，上传失败则返回null
     */
    public static Image uploadImage(String imageUrl, MessageEvent event) {
        try {
            InputStream imageFile = getImageFile(imageUrl);
            ExternalResource externalResource = ExternalResource.create(imageFile);
            Image image = event.getSubject().uploadImage(externalResource);
            externalResource.close();
            imageFile.close();
            return image;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据图片链接上传图片获取图片对象
     *
     * @param imageUrl 图片链接
     * @param bot      Bot
     * @return Image对象
     */
    public static Image uploadImage(String imageUrl, Bot bot) {
        try {
            InputStream imageFile = getImageFile(imageUrl);
            ExternalResource externalResource = ExternalResource.create(imageFile);
            Group group = getUsableGroup(bot);
            Image image;
            if (group != null) {
                image = group.uploadImage(externalResource);
            } else {
                image = bot.getAsFriend().uploadImage(externalResource);
            }
            externalResource.close();
            imageFile.close();
            return image;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据图片链接上传图片获取图片对象
     *
     * @param inputStream 图片流
     * @param bot      Bot
     * @return Image对象
     */
    public static Image uploadImage(InputStream inputStream, Bot bot) {
        try(ExternalResource externalResource = ExternalResource.create(inputStream)) {
            Group group = getUsableGroup(bot);
            Image image;
            if (group != null) {
                image = group.uploadImage(externalResource);
            } else {
                image = bot.getAsFriend().uploadImage(externalResource);
            }
            inputStream.close();
            return image;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取一个可用的 Group用作上传图片的对象
     *
     * @param bot Bot
     * @return Group对象，如果没有则返回null
     */
    private static Group getUsableGroup(Bot bot) {
        if (groupId == 0L) {
            return initGroup(bot);
        }

        try {
            return bot.getGroupOrFail(groupId);
        } catch (Exception e) {
            return initGroup(bot);
        }
    }

    private static Group initGroup(Bot bot) {
        ContactList<Group> groups = bot.getGroups();
        if (groups.size() == 0) {
            return null;
        } else {
            for (Group group : groups) {
                groupId = group.getId();
                return group;
            }
        }
        return null;
    }

    /**
     * 处理消息总事件
     *
     * @param event 消息事件
     */
    public abstract void handleMessage(MessageEvent event);

    public long[] getPermitFriends() {
        return permitFriends;
    }

    public void setPermitFriends(long[] permitFriends) {
        this.permitFriends = permitFriends;
    }

    public long[] getPermitGroups() {
        return permitGroups;
    }

    public void setPermitGroups(long[] permitGroups) {
        this.permitGroups = permitGroups;
    }

    public String getMatchString() {
        return matchString;
    }

    public int getMatchRule() {
        return matchRule;
    }

    public void setMatch(String matchString, int matchRule) {
        this.matchString = matchString;
        this.matchRule = matchRule;
    }

}
