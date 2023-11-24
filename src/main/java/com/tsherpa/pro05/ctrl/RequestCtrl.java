package com.tsherpa.pro05.ctrl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsherpa.pro05.biz.LikesService;
import com.tsherpa.pro05.biz.ReportService;
import com.tsherpa.pro05.biz.RequestService;
import com.tsherpa.pro05.entity.*;
import com.tsherpa.pro05.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("request")
public class RequestCtrl {
    @Autowired
    private RequestService requestService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private LikesService likesService;

    @GetMapping("/reqList")
    public String reqList(HttpServletRequest request, Model model)throws Exception{

        int curPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;

        Page page = new Page();
        page.setType(request.getParameter("type"));
        page.setKeyword(request.getParameter("keyword"));

        int total = requestService.getReqCount(page);
        page.setPostCount(8);
        page.makeBlock(curPage, total);
        page.makeLastPageNum(total);
        page.makePostStart(curPage, total);

        model.addAttribute("page", page);
        model.addAttribute("curPage", curPage);

        List<Request> requestList = requestService.requestList(page);
        model.addAttribute("requestList",requestList);

        return "request/requestList";
    }

    @GetMapping("/bookSearch")
    public String list(){

        return "request/bookSearch";
    }

    @PostMapping("/list")
    public String listing(@RequestParam("text") String text, Model model) {

        // 네이버 검색 API 요청
        String clientId = "t3prRpioTofJgssyOPNT";
        String clientSecret = "xpvqvASKIF";

//        String apiURL = "https://openapi.naver.com/v1/search/book?query=" + text;    // JSON 결과
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/book.json")
                .queryParam("query", text)
                .queryParam("display", 30)
                .queryParam("start", 1)
                .queryParam("sort", "sim")
                .encode()
                .build()
                .toUri();

        // Spring 요청 제공 클래스
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .build();
        // Spring 제공 restTemplate
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.exchange(req, String.class);

        // JSON 파싱 (Json 문자열을 객체로 만듦, 문서화)
        ObjectMapper om = new ObjectMapper();
        NaverResultVO resultVO = null;

        try {
            resultVO = om.readValue(resp.getBody(), NaverResultVO.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<BookVO> books =resultVO.getItems();	// books를 list.html에 출력 -> model 선언
        model.addAttribute("books", books);

        return "request/bookSearch";
    }

    @GetMapping("/insert")
    public String insertPage()throws Exception{
        return "bookSearch";
    }


    @PostMapping("/bookInsert")
    public String insert(HttpServletRequest request, Model model)throws Exception{
        BookVO book = new BookVO();
        book.setIsbn(request.getParameter("isbn"));
        book.setAuthor(request.getParameter("author"));
        book.setPubdate(request.getParameter("pubdate"));
        book.setImage(request.getParameter("image"));
        book.setTitle(request.getParameter("title"));
        book.setDiscount(request.getParameter("discount"));
        book.setPublisher(request.getParameter("publisher"));
        model.addAttribute("book",book);
        return "request/reqInsert";
    }

    @RequestMapping(value = "requestInsert", method = RequestMethod.POST)
    public String requestInsert(Request request, Book book,HttpServletRequest req, Model model, RedirectAttributes rttr, Principal principal) throws Exception {
        String sid = principal != null ? principal.getName() : "";
        request.setLoginId(sid);
        request.setAddr(req.getParameter("addr"));
        request.setPrice(req.getParameter("price"));
        request.setContent(req.getParameter("content"));
        request.setTitle(req.getParameter("title"));
        request.setIsbn(req.getParameter("isbn"));
        request.setBookAuthor(req.getParameter("bookAuthor"));
        request.setBookTitle(req.getParameter("bookTitle"));
        request.setPubdate(req.getParameter("pubdate"));
        request.setPublisher(req.getParameter("publisher"));
        request.setBookImage(req.getParameter("bookImage"));
        request.setDiscount(req.getParameter("discount"));
        requestService.reqInsert(request);
        return "redirect:/request/reqList";
    }

    @GetMapping("/reqDetail")
    public String reqDetail(Model model,@RequestParam int reqNo, Principal principal) throws Exception{

        Request request = requestService.requestDetail(reqNo);

        String sid = principal != null ? principal.getName() : "";

        Likes likes = new Likes();
        likes.setLoginId(sid);
        likes.setReqNo(reqNo);
        int chkLiked = likesService.checkLikedReq(likes);
        model.addAttribute("chkLiked",chkLiked);

        if(requestService.requestDetail(reqNo).getReadable() == 0){
            List<Request> requestList = requestService.allRequest();
            model.addAttribute("requestList", requestList);
            model.addAttribute("request", request);
            model.addAttribute("sid", sid);
            return "request/reqDetail";
        }else {
            model.addAttribute("msg", "열람 불가능한 글입니다.");
            model.addAttribute("url", "/request/reqList");
            return "layout/alert";
        }
    }

    @PostMapping("/requestLike")
    @ResponseBody
    public String requestLike(HttpServletRequest request,Principal principal, Model model){

        String sid = principal != null ? principal.getName() : "";
        int reqNo = Integer.parseInt(request.getParameter("reqNo"));

        String result = "unliked";

        Likes likes = new Likes();
        likes.setLoginId(sid);
        likes.setReqNo(reqNo);
        int chkLiked = likesService.checkLikedReq(likes);

        model.addAttribute("chkLiked",chkLiked);

        if(chkLiked==0) {
            likesService.addLikeReq(likes);
            result = "liked";
            model.addAttribute("liked", result);
        } else if (chkLiked==1){
            likesService.removeLikeReq(likes);
            result = "unliked";
        }

        return result;
    }


    @GetMapping("/edit")
    public String editForm(@RequestParam int reqNo,HttpServletRequest request, Model model,Principal principal) throws Exception {
        Request detail = requestService.requestDetail(reqNo);

        //로그인한 사용자 아이디와 작성자 아이디 비교
        String sid = principal != null ? principal.getName() : "";
        if (!sid.equals(detail.getLoginId())){
            return "redirect:/";
        }

        model.addAttribute("detail", detail);
        return "/request/reqEdit";
    }

    @PostMapping("/reqEdit")
    public String requestEdit( Request request, HttpServletRequest req, Model model) throws Exception {
        requestService.requestEdit(request);
        int reqNo = request.getReqNo();
        return "redirect:/request/reqList";
    }
    @PostMapping("/AllreqEdit")
    public String requestEditAll(Request request, HttpServletRequest req, Model model) throws Exception {
        requestService.requestEditAll(request);
        int reqNo = request.getReqNo();
        return "redirect:/request/reqList";
    }

    @GetMapping("/delete")
    public String reqDelete(@RequestParam int reqNo,HttpServletRequest request, Model model, Principal principal) throws Exception {

        String sid = principal != null ? principal.getName() : "";
        requestService.requestDelete(reqNo);

        if(sid.equals("admin")){
            return "redirect:/admin/reportList";
        }else {
            return "redirect:/request/reqList";
        }



    }

    @PostMapping("/editAll")
    public String editAllForm(@RequestParam int reqNo ,HttpServletRequest request, Model model) throws Exception {
        BookVO book = new BookVO();
        book.setIsbn(request.getParameter("isbn"));
        book.setAuthor(request.getParameter("author"));
        book.setPubdate(request.getParameter("pubdate"));
        book.setImage(request.getParameter("image"));
        book.setTitle(request.getParameter("title"));
        book.setDiscount(request.getParameter("discount"));
        book.setPublisher(request.getParameter("publisher"));
        model.addAttribute("book",book);
        model.addAttribute("reqNo",reqNo);
        return "/request/reqEditAll";
    }

    @GetMapping("/editSearch")
    public String searchForEdit(@RequestParam int reqNo, Model model){
        model.addAttribute("reqNo",reqNo);
        return "request/bookEditSearch";
    }

    @PostMapping("/list2")
    public String listing2(@RequestParam("text") String text,@RequestParam int reqNo, Model model) {

        // 네이버 검색 API 요청
        String clientId = "t3prRpioTofJgssyOPNT";
        String clientSecret = "xpvqvASKIF";

//        String apiURL = "https://openapi.naver.com/v1/search/book?query=" + text;    // JSON 결과
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/book.json")
                .queryParam("query", text)
                .queryParam("display", 30)
                .queryParam("start", 1)
                .queryParam("sort", "sim")
                .encode()
                .build()
                .toUri();

        // Spring 요청 제공 클래스
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .build();
        // Spring 제공 restTemplate
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.exchange(req, String.class);

        // JSON 파싱 (Json 문자열을 객체로 만듦, 문서화)
        ObjectMapper om = new ObjectMapper();
        NaverResultVO resultVO = null;

        try {
            resultVO = om.readValue(resp.getBody(), NaverResultVO.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<BookVO> books =resultVO.getItems();	// books를 list.html에 출력 -> model 선언
        model.addAttribute("books", books);
        model.addAttribute("reqNo", reqNo);

        return "request/bookEditSearch";
    }

}
