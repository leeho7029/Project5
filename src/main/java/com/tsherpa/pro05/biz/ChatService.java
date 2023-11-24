package com.tsherpa.pro05.biz;

import com.tsherpa.pro05.entity.ChatList;
import com.tsherpa.pro05.entity.ChatListVO;
import com.tsherpa.pro05.entity.ChatRoom;
import com.tsherpa.pro05.entity.ChatRoomVO;
import com.tsherpa.pro05.per.ChatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    @Autowired
    private ChatMapper chatMapper;

    public ChatRoomVO chatRoomAllList(int productId, String productTable, String buyerId) throws Exception {
        return chatMapper.chatRoomAllList(productId, productTable, buyerId);
    }

    public List<ChatRoomVO> chatRoomAllListByProduct(int productId, String productTable) throws Exception {
        return chatMapper.chatRoomAllListByProduct(productId, productTable);
    }

    public List<ChatRoomVO> chatRoomAllListByBuyerId(String buyerId) throws Exception {
        return chatMapper.chatRoomAllListByBuyerId(buyerId);
    }

    public ChatRoomVO getRoom(Long roomId) throws Exception {
        return chatMapper.getRoom(roomId);
    }
    public ChatRoomVO getChatRoomLast() throws Exception {
        return chatMapper.getChatRoomLast();
    }

    public ChatRoomVO createChatRoom(ChatRoom chatRoom) throws Exception {
        chatMapper.createChatRoom(chatRoom);
        return chatMapper.getChatRoomLast();
    }

    public List<ChatListVO> getChat(Long roomId) throws Exception {
        return chatMapper.getChat(roomId);
    }
    public ChatListVO getChatLast(Long roomId) throws Exception {
        return chatMapper.getChatLast(roomId);
    }

    public int insertChatList(ChatList chatList) throws Exception {
        return chatMapper.insertChatList(chatList);
    }

    public int updateChatRead(Long chatId) throws Exception {
        return chatMapper.updateChatRead(chatId);
    }

}