package com.neoniou.bot.handler;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.neoniou.bot.consts.MessageType;
import com.neoniou.bot.core.annotation.handler.BotHandler;
import com.neoniou.bot.core.entity.BotMessage;
import com.neoniou.bot.utils.MessageUtil;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Neo.Zzj
 * @date 2021/7/25
 */
@BotHandler(name = "B站短链接")
public class BiliUrlHandler {

    private static final Pattern BV_PATTERN = Pattern.compile("(BV)([A-Za-z0-9]{10})");
    private static final String REPLACE_REVERSE = "\\\\";

    private static final String BILI_VIDEO_API = "http://api.bilibili.com/x/web-interface/view?bvid=";

    private static final String BILI_PLAY_URL = "https://www.bilibili.com/video/";

    private static final String CODE = "code";
    private static final String DATA = "data";
    private static final String PIC = "pic";
    private static final String TITLE = "title";
    private static final String AID = "aid";
    private static final String OWNER = "owner";
    private static final String NAME = "name";
    private static final int OK = 0;

    private static final TimedCache<String, String> BV_CACHE = CacheUtil.newTimedCache(5 * 1000L);

    @BotHandler(
            name = "bvHandler",
            matchStr = "(BV)([A-Za-z0-9]{10})",
            matchRule = 1,
            async = true,
            type = {MessageType.FRIEND, MessageType.GROUP}
    )
    public void BvHandler(BotMessage<MessageEvent> message) {
        bvSendMessage(message.getMessage(), message.getEvent());
    }

    @BotHandler(
            name = "b23Handler",
            matchStr = "(https://b23.tv/)([A-Za-z0-9]{6})",
            matchRule = 1,
            async = true,
            type = {MessageType.FRIEND, MessageType.GROUP}
    )
    public void B23Handler(BotMessage<MessageEvent> message) {
        String shortUrl = substringMiniUrl(message.getMatch());
        String originalUrl = getOriginalUrl(shortUrl);
        bvSendMessage(originalUrl, message.getEvent());
    }

    @BotHandler(
            name = "b23MiniHandler",
            matchStr = "(\"qqdocurl\":\"https).*?(b23.tv).*?\\?",
            matchRule = 1,
            async = true,
            type = {MessageType.FRIEND, MessageType.GROUP}
    )
    public void B23MiniHandler(BotMessage<MessageEvent> message) {
        String originalUrl = getOriginalUrl(message.getMatch());
        bvSendMessage(originalUrl, message.getEvent());
    }

    private String getOriginalUrl(String shortUrl) {
        HttpResponse response = HttpRequest.get(shortUrl)
                .execute();
        return response.header(Header.LOCATION);
    }

    private String substringMiniUrl(String miniUrl) {
        miniUrl = miniUrl.substring(12, miniUrl.length() - 1);
        return miniUrl.replaceAll(REPLACE_REVERSE, "");
    }

    private void bvSendMessage(String str, MessageEvent event) {
        Matcher matcher = BV_PATTERN.matcher(str);
        if (matcher.find()) {
            String bvStr = matcher.group();
            //缓存bv，5s内不重新发送
            if (BV_CACHE.containsKey(bvStr)) {
                BV_CACHE.put(bvStr, "");
                return;
            } else {
                BV_CACHE.put(bvStr, "");
            }

            MessageChain message = generateBvMessage(bvStr, event);
            if (message != null) {
                event.getSubject().sendMessage(message);
            }
        }
    }

    private MessageChain generateBvMessage(String bvStr, MessageEvent event) {
        JSONObject data = getData(bvStr);
        if (data == null) {
            return null;
        }
        MessageChainBuilder mcb = new MessageChainBuilder();
        Image cover = MessageUtil.uploadImage(data.getStr(PIC), event);
        if (cover != null) {
            mcb.append(cover);
        }
        mcb.append("av").append(data.getStr(AID)).append("\n");
        mcb.append(data.getStr(TITLE)).append("\n");
        mcb.append("up主：").append(data.getJSONObject(OWNER).getStr(NAME)).append("\n");
        mcb.append(BILI_PLAY_URL).append(bvStr);
        return mcb.build();
    }

    private JSONObject getData(String bvStr) {
        String body = HttpRequest.get(BILI_VIDEO_API + bvStr)
                .execute()
                .body();
        JSONObject response = JSONUtil.parseObj(body);
        if (response.getInt(CODE) == OK) {
            return response.getJSONObject(DATA);
        } else {
            return null;
        }
    }
}
