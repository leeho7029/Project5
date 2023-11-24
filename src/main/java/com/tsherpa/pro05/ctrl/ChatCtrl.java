package com.tsherpa.pro05.ctrl;

import com.tsherpa.pro05.biz.ChatService;
import com.tsherpa.pro05.biz.MarketService;
import com.tsherpa.pro05.biz.RequestService;
import com.tsherpa.pro05.biz.UserService;
import com.tsherpa.pro05.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatCtrl {

    @Autowired
    private final ChatService chatService;

    @Autowired
    private UserService userService;

    @Autowired
    private MarketService marketService;

    @Autowired
    private RequestService requestService;

    @GetMapping("/chat")
    public String chatHome(@RequestParam(value = "productNo") int productNo, @RequestParam("productTable") String productTable, Principal principal) throws Exception {
        String path = "redirect:/";

        if(principal != null) {

            path += "chat/";
            String sid = principal.getName();
            String id = "";

            // 상품에 판매자 정보 가져오기
            if(productTable.equals("market")) {
                MainVO market = marketService.chatVOList(productNo);
                id = market.getLoginId();
            } else if (productTable.equals("request")) {
                Request request = requestService.requestDetail(productNo);
                id = request.getLoginId();
            }

            if(id.equals(sid)){
                // 로그인한 아이디가 판매자 아이디가 같을 때
                path += "myProList?productId=" + productNo + "&productTable=" + productTable;
            } else {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>없음");
                // 로그인한 아이디가 판매자 아이디가 아닐 때
                ChatRoomVO chatRoomVO = chatService.chatRoomAllList(productNo, productTable, sid);
                Long roomId;
                if(chatRoomVO == null) {
                    ChatRoom chatRoom = new ChatRoom();
                    chatRoom.setProductId(productNo);
                    chatRoom.setProductTable(productTable);
                    chatRoom.setBuyerId(sid);
                    ChatRoomVO room = chatService.createChatRoom(chatRoom);
                    roomId = room.getRoomId();
                } else {
                    roomId = chatRoomVO.getRoomId();
                }
                path += "getChat/" + roomId;
            }
        } else {
            if(productTable.equals("market")) {
                path += "market/marketList";
            } else if (productTable.equals("request")) {
                path += "request/reqList";
            }
        }
        return path;
    }

    @GetMapping("/myProList")
    public String roomList(@RequestParam("productId") int productId, @RequestParam("productTable") String productTable, Model model, Principal principal) throws Exception {
        if(principal != null) {
            String sid = principal.getName();

            List<ChatRoomVO> roomList = chatService.chatRoomAllListByProduct(productId, productTable);
            model.addAttribute("roomList", roomList);

            String productNm = "";
            if(productTable.equals("market")) {
                Market market = marketService.marketDetail(productId);
                productNm = market.getTitle();
            }

            if(productTable.equals("request")) {
                Request request = requestService.requestDetail(productId);
                productNm = request.getTitle();
            }

            model.addAttribute("myChat", false);
            model.addAttribute("productNm", productNm);

            return "chat/list";
        } else {
            String path = "redirect:/";
            if (productTable.equals("market")) {
                path += "market/marketList";
            } else if (productTable.equals("request")) {
                path += "request/reqList";
            }
            return path;
        }
    }

    @GetMapping("/myChatList")
    public String myRoomList(Model model, Principal principal) throws Exception {
        if(principal != null) {
            String sid = principal.getName();

            List<ChatRoomVO> roomList = new ArrayList<>();

            //chatRoomAllListByProduct
            List<MainVO> marketList = marketService.userMainVOList(sid);
            if(marketList != null) {
                for(MainVO market : marketList) {
                    List<ChatRoomVO> marketChat = chatService.chatRoomAllListByProduct(market.getMarketNo(), "market");
                    if(marketChat != null) {
                        for(ChatRoomVO marketPro : marketChat) {
                            marketPro.setProductName(market.getTitle());
                        }
                        roomList.addAll(marketChat);
                    }
                }
            }

            List<Request> requestList = requestService.userRequestList(sid);
            if(requestList != null) {
                for(Request req : requestList) {
                    List<ChatRoomVO> requestChat = chatService.chatRoomAllListByProduct(req.getReqNo(), "request");
                    if(requestChat != null) {
                        for(ChatRoomVO requestPro : requestChat) {
                            requestPro.setProductName(req.getTitle());
                        }
                        roomList.addAll(requestChat);
                    }
                }
            }

            // 구매자...
            //chatRoomAllListByBuyerId
            List<ChatRoomVO> buyerList = chatService.chatRoomAllListByBuyerId(sid);
            if(buyerList != null) {
                for(ChatRoomVO chatRoomVO : buyerList) {
                    User user = new User();
                    if(chatRoomVO.getProductTable().equals("market")) {
                        Market market = marketService.marketDetail(chatRoomVO.getProductId());
                        user = userService.getUserByLoginId(market.getLoginId());
                        chatRoomVO.setSellerName(user.getUserName());
                        chatRoomVO.setBuyerActive(user.getActive());
                        chatRoomVO.setBuyerId(user.getLoginId());
                        chatRoomVO.setProductName(market.getTitle());
                    }

                    if(chatRoomVO.getProductTable().equals("request")) {
                        Request request = requestService.requestDetail(chatRoomVO.getProductId());
                        user = userService.getUserByLoginId(request.getLoginId());
                        chatRoomVO.setSellerName(user.getUserName());
                        chatRoomVO.setBuyerActive(user.getActive());
                        chatRoomVO.setBuyerId(user.getLoginId());
                        chatRoomVO.setProductName(request.getTitle());
                    }
                }
                roomList.addAll(buyerList);
            }

            model.addAttribute("myChat", true);
            model.addAttribute("roomList", roomList);

            return "chat/list";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/getChat/{roomId}")
    public String joinRoom(@PathVariable(required = false) Long roomId, Model model, Principal principal) throws Exception {

        String sid = principal != null ? principal.getName() : "";
        ChatRoomVO chatRoomVO = chatService.getRoom(roomId);

        if(principal != null) {
            String id = "";
            String productNm = "";
            String productTable = "";
            Integer productId = 0;

            if(chatRoomVO.getProductTable().equals("market")) {
                Market market = marketService.marketDetail(chatRoomVO.getProductId());
                id = market.getLoginId();
                productNm = market.getTitle();
                productTable = "market";
                productId = market.getMarketNo();

            } else if (chatRoomVO.getProductTable().equals("request")) {
                Request request = requestService.requestDetail(chatRoomVO.getProductId());
                id = request.getLoginId();
                productNm = request.getTitle();
                productTable = "request";
                productId = request.getReqNo();
            }

            if(chatRoomVO != null && (id.equals(sid) || chatRoomVO.getBuyerId().equals(sid))) {
                List<ChatListVO> chatList = chatService.getChat(roomId);
                model.addAttribute("roomId", roomId);
                model.addAttribute("chatList", chatList);
                model.addAttribute("loginId", sid);
                model.addAttribute("productNm", productNm);
                model.addAttribute("productTable", productTable);
                model.addAttribute("productId", productId);
                return "chat/get";
            }
        }

        String path = "redirect:/";
        if (chatRoomVO.getProductTable().equals("market")) {
            path += "market/marketList";
        } else if (chatRoomVO.getProductTable().equals("request")) {
            path += "request/reqList";
        }
        return path;

    }

    @MessageMapping("/{roomId}")
    @SendTo("/chat/getChat/{roomId}")
    public ChatListVO chatInsert(@DestinationVariable Long roomId, ChatListVO message, Principal principal) throws Exception {

        String sid = principal.getName();

        ChatList chatList = new ChatList();
        chatList.setRoomId(roomId);
        chatList.setMessage(message.getMessage());
        chatList.setSenderId(sid);
        int cnt = chatService.insertChatList(chatList);

        return chatService.getChatLast(roomId);
    }

    @GetMapping("/tradeInfo")
    public String trade(@RequestParam("marketNo") int marketNo, Model model, Principal principal) throws Exception {
        Market market = marketService.marketDetail(marketNo);
        model.addAttribute("market", market);
        return "chat/trade";
    }

    @PostMapping("/tradePro")
    @ResponseBody
    public boolean activeUpdatePro(@RequestParam("active") int active, @RequestParam("marketNo") int marketNo, Principal principal) throws Exception {
        boolean pass = false;
        String sid = principal != null ? principal.getName() : "";
        String loginId = principal.getName();
        Market market = marketService.marketDetail(marketNo);
        if(market.getLoginId().equals(loginId)) {
            marketService.updateActive(active, marketNo,sid);
            pass = true;
        }

        return pass;

    }

    @GetMapping("/tradeReqInfo")
    public String trade2(@RequestParam("reqNo") int reqNo, Model model, Principal principal) throws Exception {
        Request request = requestService.requestDetail(reqNo);
        model.addAttribute("request", request);
        return "chat/tradeReq";
    }

    @PostMapping("/tradeReqPro")
    @ResponseBody
    public boolean activeUpdatePro2(@RequestParam("active") int active, @RequestParam("reqNo") int reqNo, Principal principal) throws Exception {
        boolean pass = false;
        String sid = principal != null ? principal.getName() : "";
        String loginId = principal.getName();
        Request request = requestService.requestDetail(reqNo);
        if(request.getLoginId().equals(loginId)) {
            requestService.updateActive(active, reqNo, sid);
            pass = true;
        }

        return pass;

    }

}