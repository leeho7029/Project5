package com.tsherpa.pro05.ctrl;

import com.tsherpa.pro05.biz.QnaService;
import com.tsherpa.pro05.biz.UserService;
import com.tsherpa.pro05.entity.Qna;
import com.tsherpa.pro05.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
public class QnaCtrl {

    @Autowired
    private QnaService qnaService;

    @Autowired
    private UserService userService;

    @GetMapping("/qna/list")
    public String getList(HttpServletRequest request, Model model){
        int curPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;

        Page page = new Page();

        int total = qnaService.getCount(page);
        page.makeBlock(curPage, total);
        page.makeLastPageNum(total);
        page.makePostStart(curPage, total);

        List<Qna> list = qnaService.getList(page);
        model.addAttribute("list", list);
        model.addAttribute("curPage", curPage);
        model.addAttribute("page", page);

        return "qna/qnaList";
    }

    @GetMapping("/qna/detail")
    public String getQna(@RequestParam("qno")int qno,Principal principal, Model model){
            String sid = principal != null ? principal.getName() : "";
            model.addAttribute("sid", sid);

            Qna detail = qnaService.qnaDetail(qno);
            model.addAttribute("detail", detail);

            return "qna/qnaDetail";


    }


    @GetMapping("/qna/questionInsert")
    public String getQuestionInsert() throws Exception {
        return "qna/qnaInsert";
    }


    @PostMapping("/qna/questionInsert")
    public String getQuestionInsertPro(HttpServletRequest request, Principal principal) throws Exception {

        String sid = principal != null ? principal.getName() : "";

        Qna dto = new Qna();
        dto.setTitle(request.getParameter("title"));
        dto.setContent(request.getParameter("content"));
        dto.setAuthor(sid);
        qnaService.questionInsert(dto);
        return "redirect:/qna/list";
    }

    @GetMapping("/qna/edit")
    public String getQnaEdit(HttpServletRequest request, Model model) throws Exception {
        int qno = Integer.parseInt(request.getParameter("qno"));
        Qna detail = qnaService.qnaDetail(qno);
        model.addAttribute("detail", detail);
        return "qna/qnaEdit";
    }

    @PostMapping("/qna/edit")
    public String getQnaEditPro(HttpServletRequest request, Model model) throws Exception {
        int qno = Integer.parseInt(request.getParameter("qno"));
        Qna dto = new Qna();
        dto.setQno(qno);
        dto.setTitle(request.getParameter("title"));
        dto.setContent(request.getParameter("content"));
        qnaService.qnaEdit(dto);
        return "redirect:/qna/list";
    }

    @GetMapping("/qna/delete")
    public String getQnaDelete(HttpServletRequest request, Model model) throws Exception {
        int qno = Integer.parseInt(request.getParameter("qno"));
        qnaService.qnaDelete(qno);
        return "redirect:/qna/list";
    }

}
