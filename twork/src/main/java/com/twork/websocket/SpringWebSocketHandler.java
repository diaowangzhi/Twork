package com.twork.websocket;

import com.alibaba.fastjson.JSONObject;
import com.twork.dao.*;
import com.twork.pojo.*;
import com.twork.websocket.message.*;
import com.twork.websocket.message.Message;
import com.twork.websocket.vo.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Component
public class SpringWebSocketHandler extends TextWebSocketHandler {

    private static final Map<Integer, WebSocketSession> userSocketSessionMap;
    private static final Map<Integer, HttpSession> userSessionMap;
    private static Logger logger = Logger.getLogger(SpringWebSocketHandler.class);

    static {
        userSocketSessionMap = new HashMap<>();
        userSessionMap = new HashMap<>();
    }

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private U2uChatRecordMapper u2uChatRecordMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private GroupChatRecordMapper groupChatRecordMapper;
    @Autowired
    private FriendMapper friendMapper;

    /**
     * 连接成功时候，会触发页面上onopen方法
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("connect to the websocket success......当前数量:" + userSocketSessionMap.size());
        User user = (User) session.getAttributes().get("user");
        HttpSession httpSession = (HttpSession) session.getAttributes().get("session");
        System.out.println(session);
        if (userSocketSessionMap.get(user.getUid()) != null) {
            System.out.println("不允许打开多个页面");
        }

        userSocketSessionMap.put(user.getUid(), session);
        userSessionMap.put(user.getUid(), httpSession);
        System.out.println(user.getUid());

        //更新状态
        this.online(user.getUid());

        //这块会实现自己业务，比如，当用户登录后，会把离线消息推送给用户
        //TextMessage returnMessage = new TextMessage("你将收到的离线");
        //session.sendMessage(returnMessage);
        Date lastonlinedatetime = user.getLastonlinedatetime();
        //发送好友离线消息
        this.friendOfflineMessage(lastonlinedatetime, user.getUid());
        //发送群组离线消息
        this.groupOfflineMessage(lastonlinedatetime, user.getUid());
    }

    /**
     * 关闭连接时触发
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        logger.debug("websocket connection closed......");

        // 移除Socket会话
        for (Entry<Integer, WebSocketSession> entry : userSocketSessionMap.entrySet()) {
            if (entry.getValue().getId().equals(session.getId())) {
                User outUser = userMapper.selectByPrimaryKey(entry.getKey());
                //更新登出时间
                outUser.setLastonlinedatetime(new Date());
                outUser.setStatus(false);
                userMapper.updateByPrimaryKey(outUser);
                userSessionMap.get(entry.getKey()).invalidate();
                userSessionMap.remove(entry.getKey());

                //更新状态
                this.offline(entry.getKey());
                userSocketSessionMap.remove(entry.getKey());
                session.close();
                System.out.println("用户" + entry.getKey() + "已退出！");
                System.out.println("Socket会话已经移除:用户ID" + entry.getKey());
                System.out.println("剩余在线用户" + userSocketSessionMap.size());
                break;
            }
        }
    }

    /**
     * js调用websocket.send时候，会调用该方法
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        System.out.println(message.getPayload());
        System.out.println(session);
        String data = message.getPayload();

        this.handleMessage(data);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        // 移除Socket会话
        for (Entry<Integer, WebSocketSession> entry : userSocketSessionMap.entrySet()) {
            if (entry.getValue().getId().equals(session.getId())) {
                userSocketSessionMap.remove(entry.getKey());
                System.out.println("Socket会话已经移除:用户ID" + entry.getKey());
                break;
            }
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private void handleMessage(String message) throws IOException {
        System.out.println(message);
        JSONObject json = JSONObject.parseObject(message);
        String emit = (String) json.get("emit");

        if (emit.equals("chatMessage")) {//聊天消息

            JSONObject data = json.getJSONObject("data");//获取数据

            String type = (String) (data.getJSONObject("to")).get("type");
            System.out.println(type);
            if (type.equals("friend")) {
                UserMessage messagefromClient = JSONObject.toJavaObject(data, UserMessage.class);
                Sender mine = messagefromClient.getMine();
                ToUser to = messagefromClient.getTo();
                int rid = to.getId();
                int sid = mine.getId();
                System.out.println(rid);
                String avatar = mine.getAvatar();
                String username = mine.getUsername();
                type = to.getType();
                int cid = 0;
                int fromid = mine.getId();
                Date timestamp = new Date();
                String content = mine.getContent();

                //存储好友消息
                U2uChatRecord chatRecord = new U2uChatRecord();
                chatRecord.setSid(sid);
                chatRecord.setRid(rid);
                chatRecord.setContent(content);
                u2uChatRecordMapper.insert(chatRecord);

                //服务器转发好友消息
                MessageToClient messageToClient = new MessageToClient();

                messageToClient.setAvatar(avatar);
                messageToClient.setCid(cid);
                messageToClient.setContent(content);
                messageToClient.setFromid(fromid);
                messageToClient.setMine(false);
                messageToClient.setTimestamp(timestamp);
                messageToClient.setUsername(username);
                messageToClient.setType(type);
                this.sendTypeMessage(type, messageToClient, sid, rid);
            } else {//群组消息
                GroupMessage messagefromClient = JSONObject.toJavaObject(data, GroupMessage.class);
                Sender mine = messagefromClient.getMine();
                ToGroup to = messagefromClient.getTo();
                int rid = to.getId();
                int sid = mine.getId();
                System.out.println(rid);
                String avatar = mine.getAvatar();
                String username = mine.getUsername();
                type = to.getType();
                int cid = 0;
                int fromid = mine.getId();
                Date timestamp = new Date();
                String content = mine.getContent();

                //存储群消息
                GroupChatRecord chatRecord = new GroupChatRecord();
                chatRecord.setGid(rid);
                chatRecord.setUid(sid);
                chatRecord.setContent(content);
                groupChatRecordMapper.insert(chatRecord);

                //服务器转发群消息
                MessageToClient messageToClient = new MessageToClient();

                messageToClient.setAvatar(avatar);
                messageToClient.setCid(cid);
                messageToClient.setContent(content);
                messageToClient.setFromid(fromid);
                messageToClient.setMine(false);
                messageToClient.setTimestamp(timestamp);
                messageToClient.setUsername(username);
                messageToClient.setType(type);
                this.sendTypeMessage(type, messageToClient, sid, rid);
            }
        }
    }

    private void friendOfflineMessage(Date lastonlinedatetime, int rid) {
        List<U2uChatRecord> u2uChatRecords = u2uChatRecordMapper.offLineRecord(rid, lastonlinedatetime);
        for (U2uChatRecord chatRecord : u2uChatRecords) {
            MessageToClient messageToClient = new MessageToClient();
            User friend = userMapper.selectByPrimaryKey(chatRecord.getSid());
            messageToClient.setCid(0);
            messageToClient.setContent(chatRecord.getContent());
            messageToClient.setFromid(chatRecord.getSid());
            messageToClient.setMine(false);
            messageToClient.setTimestamp(chatRecord.getDatetime());
            messageToClient.setUsername(friend.getName());
            messageToClient.setType("friend");
            try {
                this.sendTypeMessage("friend", messageToClient, chatRecord.getSid(), rid);
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    private void groupOfflineMessage(Date lastonlinedatetime, int rid) {
        List<Group> groups = groupMapper.listGroup(rid);
        for (Group group : groups) {
            List<GroupChatRecord> groupChatRecords = groupChatRecordMapper.offLineRecord(group.getGid(), lastonlinedatetime);
            for (GroupChatRecord chatRecord : groupChatRecords) {
                MessageToClient messageToClient = new MessageToClient();
                User sender = userMapper.selectByPrimaryKey(chatRecord.getUid());
                messageToClient.setCid(0);
                messageToClient.setContent(chatRecord.getContent());
                messageToClient.setFromid(chatRecord.getUid());
                messageToClient.setMine(false);
                messageToClient.setTimestamp(chatRecord.getDatetime());
                messageToClient.setUsername(sender.getName());
                messageToClient.setType("group");

                messageToClient.setId(group.getGid());
                Message message = new Message();
                message.setEmit("chatMessage");
                message.setData(messageToClient);
                String jsonObject = JSONObject.toJSONString(message);
                System.out.println(jsonObject);
                TextMessage sendMessage = new TextMessage(jsonObject);
                try {
                    this.sendMessageToUser(rid, sendMessage);
                } catch (IOException e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendTypeMessage(String type, MessageToClient messageToClient, int sid, int rid) throws IOException {
        if (type.equals("friend")) {//好友消息

            messageToClient.setId(sid);
            Message message = new Message();
            message.setEmit("chatMessage");
            message.setData(messageToClient);
            String jsonObject = JSONObject.toJSONString(message);
            System.out.println(jsonObject);
            final TextMessage sendMessage = new TextMessage(jsonObject);
            this.sendMessageToUser(rid, sendMessage);
        } else if (type.equals("group")) {//群消息
            messageToClient.setId(rid);
            Message message = new Message();
            message.setEmit("chatMessage");
            message.setData(messageToClient);
            String jsonObject = JSONObject.toJSONString(message);
            System.out.println(jsonObject);
            final TextMessage sendMessage = new TextMessage(jsonObject);
            // 群发
            for (Entry<Integer, WebSocketSession> entry : userSocketSessionMap.entrySet()) {
                if (entry.getKey() != sid && memberMapper.selectByPrimaryKey(rid, entry.getKey()) != null) {
                    if (entry.getValue().isOpen()) {
                        try {
                            entry.getValue().sendMessage(sendMessage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void addFriend(int uid, int id, String username, String sign, String avatar, int groupid) {//添加好友更新面板

        Message message = new Message();
        message.setEmit("addList");
        AddFriend addFriend = new AddFriend();
        addFriend.setType("friend");
        addFriend.setId(id);
        addFriend.setUsername(username);
        addFriend.setSign(sign);
        addFriend.setAvatar(avatar);
        addFriend.setGroupid(groupid);
        message.setData(addFriend);
        String jsonObject = JSONObject.toJSONString(message);
        System.out.println(jsonObject);
        final TextMessage sendMessage = new TextMessage(jsonObject);
        try {
            this.sendMessageToUser(uid, sendMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addGroup(int uid, int id, String avatar, String groupname) {//添加好友分组更新面板

        Message message = new Message();
        message.setEmit("addList");
        AddGroup addGroup = new AddGroup();
        addGroup.setType("group");
        addGroup.setId(id);
        addGroup.setGroupname(groupname);
        addGroup.setAvatar(avatar);
        message.setData(addGroup);
        String jsonObject = JSONObject.toJSONString(message);
        System.out.println(jsonObject);
        final TextMessage sendMessage = new TextMessage(jsonObject);
        try {
            this.sendMessageToUser(uid, sendMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void remove(int uid, int id, String type) {

        Message message = new Message();
        message.setEmit("removeList");
        Remove remove = new Remove();
        remove.setId(id);
        remove.setType(type);
        message.setData(remove);
        String jsonObject = JSONObject.toJSONString(message);
        System.out.println(jsonObject);
        final TextMessage sendMessage = new TextMessage(jsonObject);
        try {
            this.sendMessageToUser(uid, sendMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void online(int uid) {

        List<Friend> friends = friendMapper.selectByUid(uid);

        for (Friend friend : friends) {
            Message message = new Message();
            message.setEmit("updateStatus");
            UpdateStatus updateStatus = new UpdateStatus();
            updateStatus.setId(uid);
            updateStatus.setType("online");
            message.setData(updateStatus);
            String jsonObject = JSONObject.toJSONString(message);
            System.out.println(jsonObject);
            TextMessage sendMessage = new TextMessage(jsonObject);
            try {
                this.sendMessageToUser(friend.getFid(), sendMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void offline(int uid) {

        List<Friend> friends = friendMapper.selectByUid(uid);

        for (Friend friend : friends) {
            Message message = new Message();
            message.setEmit("updateStatus");
            UpdateStatus updateStatus = new UpdateStatus();
            updateStatus.setId(uid);
            updateStatus.setType("offline");
            message.setData(updateStatus);
            String jsonObject = JSONObject.toJSONString(message);
            System.out.println(jsonObject);
            TextMessage sendMessage = new TextMessage(jsonObject);
            try {
                this.sendMessageToUser(friend.getFid(), sendMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void newMessage(int uid) {//有新消息时通知客户端

        Message message = new Message();
        message.setEmit("messageBox");
        String jsonObject = JSONObject.toJSONString(message);
        System.out.println(jsonObject);
        final TextMessage sendMessage = new TextMessage(jsonObject);
        try {
            this.sendMessageToUser(uid, sendMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 给某个用户发送消息
     */
    private void sendMessageToUser(Integer uid, TextMessage message) throws IOException {
        WebSocketSession session = userSocketSessionMap.get(uid);
        if (session != null && session.isOpen()) {
            session.sendMessage(message);
        }
    }

    /*public void sendMessageToUsers(TextMessage message) {
        for (Entry<Integer, WebSocketSession> entry : userSocketSessionMap.entrySet()) {
            try {
                this.sendMessageToUser(entry.getKey(), message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}