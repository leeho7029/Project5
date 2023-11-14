package com.tsherpa.team35.ctrl;

import com.tsherpa.team35.biz.MarketService;
import com.tsherpa.team35.entity.Market;
import com.tsherpa.team35.entity.Photos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("market")
public class MarketCtrl {
    @Autowired
    MarketService marketService;

    @GetMapping("/marketList")
    public String list(Model model)throws Exception{
        return "market/marketList";
    }

    @PostMapping("/reqInsert")
    public String reqInsert(Model model)throws Exception{

        return "redirect:marketList";
    }

    @GetMapping("/reqInsert")
    public String requestInsert(Model model)throws Exception{

        return "market/reqInsert";
    }

    @GetMapping("/marketInsert")
    public String insertMarket(Model model)throws Exception{

        return "market/marketInsert";
    }

    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public String write(Market market, @RequestParam("upfile") MultipartFile[] files, HttpServletRequest req, Model model, RedirectAttributes rttr, Principal principal) throws Exception {
        String sid = principal != null ? principal.getName() : "";
            String realPath = "";           // 업로드 경로 설정
            String today = new SimpleDateFormat("yyMMdd").format(new Date());
            String saveFolder = realPath + today;
        System.out.println("=============================");
        System.out.println("saveFolder");
            File folder = new File(saveFolder);
            if(!folder.exists()) {                                  // 폴더가 존재하지 않으면 폴더 생성
                folder.mkdirs();
            }
            List<Photos> fileInfoList = new ArrayList<>();        // 첨부파일 정보를 리스트로 생성
            for(MultipartFile file : files) {
                Photos fileInfo = new Photos();
                String originalFileName = file.getOriginalFilename(); // 첨부파일의 실제 파일명
                if(!originalFileName.isEmpty()) {
                    String saveFileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));     // 파일 이름을 랜덤으로 설정
                    fileInfo.setSaveFile(today);
                    fileInfo.setOriginFile(originalFileName);
                    fileInfo.setSaveFile(saveFileName);
                    fileInfo.setSaveFolder(saveFolder);
                    file.transferTo(new File(folder, saveFileName));    // 파일을 업로드 폴더에 저장
                }
                fileInfoList.add(fileInfo);
            }
            market.setFileInfoList(fileInfoList);
            market.setLoginId(sid);
            try {
           marketService.marketInsert(market);
                rttr.addFlashAttribute("msg", "자료실에 글을 등록하였습니다");
            } catch(Exception e) {
                e.printStackTrace();
                rttr.addFlashAttribute("msg", "글 작성 중 문제가 발생했습니다");
            }

        return "redirect:/market/marketList";
    }

    @GetMapping("/detail")
    public String marketDetail(){
        return "market/marketDetail";
    }

}
