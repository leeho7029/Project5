package com.tsherpa.pro05.ctrl;

import com.tsherpa.pro05.biz.*;
import com.tsherpa.pro05.entity.MainVO;
import com.tsherpa.pro05.entity.Report;
import com.tsherpa.pro05.entity.Request;
import com.tsherpa.pro05.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class UserCtrl {

    @Autowired
    private UserService userService;

    @Autowired
    private MarketService marketService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private LikesService likesService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(value = "exception", required = false) String exception){

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("exception", exception);
        modelAndView.setViewName("user/login");
        return modelAndView;
    }

    @GetMapping("/join")
    public ModelAndView join(){

        ModelAndView modelAndView = new ModelAndView();
        User user = new User();

        modelAndView.addObject("user", user);
        modelAndView.setViewName("user/join");
        return modelAndView;
    }

    @PostMapping("/join")
    public ModelAndView joinPro(User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.getUserByLoginId(user.getLoginId());
        if (userExists != null) {
            bindingResult
                    .rejectValue("loginId", "error.loginId","사용이 불가한 아이디입니다.");
        }

        if(!user.getPassword().equals(user.getPasswordConfirm())) {
            bindingResult
                    .rejectValue("password", "error.password","비밀번호와 비밀번호 확인이 다릅니다.");
        }

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("/user/join");
        } else {
            int cnt = userService.saveUser(user);
            if(cnt == 1) {
                modelAndView.setViewName("redirect:/");
            } else {
                modelAndView.setViewName("/user/join");
            }
        }

        return modelAndView;
    }

    //마이페이지
    @GetMapping("/mypage")
    public String mypage(Model model,Principal principal) {
        String sid = principal != null ? principal.getName() : "";
        User user = userService.getUserByLoginId(sid);
        model.addAttribute("user",user);

        //상점 개설일
        String regDateString = user.getRegDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime regDateTime = LocalDateTime.parse(regDateString, formatter);
        LocalDate regDate = regDateTime.toLocalDate();

        LocalDate now = LocalDate.now();
        Period period = Period.between(regDate, now);
        long days = period.getDays();

        model.addAttribute("marketOpen", days + "일 전");

        // 판매중인 상품 수
        int cntSell = marketService.cntSell(sid);
        model.addAttribute("cntSell",cntSell);

        //판매 목록
        List<MainVO> mainList = marketService.userMainVOList(sid);
        model.addAttribute("mainList",mainList);

        //삽니다 목록
        List<Request> requestList = requestService.userRequestList(sid);
        model.addAttribute("requestList",requestList);

        //신고 목록
        List<Report> reportList = reportService.userReportList(sid);
        model.addAttribute("reportList",reportList);

        //좋아요 목록
        //팝니다
        List<MainVO> likeMarket = marketService.likeMarketList(sid);
        model.addAttribute("likeMarket",likeMarket);
        //삽니다
        List<Request> likeRequestList = requestService.likeRequestList(sid);
        model.addAttribute("likeRequestList",likeRequestList);

        // 거래 누적 수
        int cntDeal = userService.cntDeal(sid);
        model.addAttribute("cntDeal",cntDeal);

        return "user/mypage";
    }



    //회원 정보 수정
    @GetMapping("/userEdit")
    public String userEdit(Model model, Principal principal) {
        String sid = principal != null ? principal.getName() : "";

        User user = userService.getUserByLoginId(sid);
        model.addAttribute("user",user);

        return "user/userEdit";
    }
    //회원 정보 수정 처리
    @PostMapping("/userEdit")
    public String userEditPro(User user,Model model){
        userService.userEdit(user);

        model.addAttribute("msg","회원 정보가 수정되었습니다.");
        return "user/userEdit";
    }

    //비밀번호 변경
    @GetMapping("/pwEdit")
    public String pwEdit(Model model, Principal principal) {
        String sid = principal != null ? principal.getName() : "";
        User user = new User();
        model.addAttribute("user",user);

        return "user/pwEdit";
    }

    //비밀번호 변경 처리
    @PostMapping("/pwEdit")
    public String pwEditPro(User user, BindingResult bindingResult,Principal principal,Model model) {
        String sid = principal != null ? principal.getName() : "";
        User userInfo = userService.getUserByLoginId(sid);
        user.setLoginId(sid);

        boolean chkEditPw = false;
        //변경할 비밀번호 일치 여부 확인
        if(!user.getPassword().equals(user.getPasswordConfirm())) {
            bindingResult
                    .rejectValue("password", "error.password","비밀번호와 비밀번호 확인이 다릅니다.");
        } else {
            chkEditPw = true;
        }

        //현재 비밀번호 일치여부 확인
        boolean chk = bCryptPasswordEncoder.matches(user.getNowPassword(),userInfo.getPassword());

        //변경할 비밀번호로 업데이트
        if (chk && chkEditPw){
            userService.pwEdit(user);
            model.addAttribute("msg","비밀번호가 변경되었습니다.");
        } else {
            model.addAttribute("msg","입력 값이 잘못되었습니다.");
        }

        return "user/pwEdit";
    }


    @GetMapping("/findId")
    public String findIdForm() {return "user/findId";}

    @PostMapping("/findId")
    public String findId(@RequestParam("email") String email, @RequestParam("tel") String tel, Model model) {

        User user = userService.findId(email, tel);
        if(user != null) {
                String firstId = user.getLoginId().substring(0, 3);

                String lastId = "";
                for (int i = 0; i < (user.getLoginId().length() - 3); i++) {
                    lastId += "*";
                }

                model.addAttribute("id", firstId + lastId);

                return "user/findIdSuc";

        } else  {
                model.addAttribute("msg", "등록된 정보가 없습니다. ");
                model.addAttribute("url", "/login");
                return "layout/alert";
        }
    }

    @GetMapping("/findPw")
    public String findPw() {
        return "user/findPw";
    }


    //비밀번호 찾기
    @PostMapping("/findPw")
    @ResponseBody
    public String findPassword(@RequestParam String name, @RequestParam String email, @RequestParam String id, Model model) {
        User user = userService.getUserByLoginId(id);
        String result = "fail";
        if(name.equals(user.getUserName()) && email.equals(user.getEmail()) && id.equals(user.getLoginId())){
            //임시비밀번호 암호화해서 DB에 저장
            String tempPassword = userService.getRamdomPassword(8);

            user.setPassword(tempPassword);
            userService.pwEdit(user);

            //이메일 전송
            userService.sendTempPasswordEmail(email, tempPassword);

            result="success";
            return result;
//            return "user/login";
        } else {
            model.addAttribute("msg","정보가 일치하지 않습니다.");
//            return "redirect:/findPw";
            return result;
        }


    }

}