package com.tsherpa.pro05.ctrl;


import com.tsherpa.pro05.biz.MainPhotoService;
import com.tsherpa.pro05.biz.LikesService;
import com.tsherpa.pro05.biz.MarketService;
import com.tsherpa.pro05.biz.PhotosService;
import com.tsherpa.pro05.entity.*;
import com.tsherpa.pro05.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("market")
public class MarketCtrl {
    @Autowired
    MarketService marketService;
    @Autowired
    PhotosService photosService;
    @Autowired
    MainPhotoService mainphotoService;
    @Autowired
    LikesService likesService;

    @Value("${spring.servlet.multipart.location}")
    String uploadFolder;

    private final ResourceLoader resourceLoader;

    public MarketCtrl(ResourceLoader resourceLoader){
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/marketList")
    public String list(HttpServletRequest request, Model model)throws Exception{

        int curPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;

        Page page = new Page();
        page.setType(request.getParameter("type"));
        page.setKeyword(request.getParameter("keyword"));

        int total = marketService.getMarCount(page);
        page.setPostCount(8);
        page.makeBlock(curPage, total);
        page.makeLastPageNum(total);
        page.makePostStart(curPage, total);

        model.addAttribute("page", page);
        model.addAttribute("curPage", curPage);

        List<MainVO> mainList = marketService.mainVOList(page);
        model.addAttribute("mainList",mainList);
        return "market/marketList";
    }

    @GetMapping("/marketInsert")
    public String insertMarket(Model model,String msg, Principal principal) throws Exception {
        String sid = principal != null ? principal.getName() : "";
        model.addAttribute("sid",sid);
        model.addAttribute("msg", msg);

        return "market/marketInsert";
    }


    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public String write(Market market, @RequestParam("upfile") MultipartFile[] repImage,@RequestParam("detailFile") MultipartFile[] detailImages ,HttpServletRequest req, Model model, RedirectAttributes rttr, Principal principal) throws Exception {


        if (!isValidFileExtension(repImage) || !isValidFileExtension(detailImages)) {
            // 확장자가 허용되지 않는 파일이 포함되어 있으면 에러 메시지 전달
            String msg = "파일 형식을 확인해주세요";
            model.addAttribute("msg",msg);
            return "market/marketInsert";
        }

        String sid = principal != null ? principal.getName() : "";
        String realPath = req.getSession().getServletContext().getRealPath("/resources/static/upload/");

        String today = new SimpleDateFormat("yyMMdd").format(new Date());
        String repImageSaveFolder = "rep_images/" + today;
        String detailImageSaveFolder = "detail_images/" + today;

        File repImageFolder = new File(realPath, repImageSaveFolder);
        File detailImageFolder = new File(realPath, detailImageSaveFolder);


        try {
            if (!repImageFolder.exists()) {
                repImageFolder.mkdirs();
            }

            if (!detailImageFolder.exists()) {
                detailImageFolder.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace(); // 로그 출력 또는 적절한 예외 처리
        }
        List<Mainphoto> mainphotoList = new ArrayList<>();
        for (MultipartFile mainphoto : repImage) {
            Mainphoto main = new Mainphoto();
            String originalmainName = mainphoto.getOriginalFilename();
            if (!originalmainName.isEmpty()) {
                String savemainName = UUID.randomUUID().toString() + originalmainName.substring(originalmainName.lastIndexOf("."));
                main.setSaveFile(today);
                main.setSaveFile(savemainName);
                main.setOriginFile(originalmainName);
                main.setSaveFolder(repImageSaveFolder);
                mainphoto.transferTo(new File(repImageFolder, savemainName));
            }
            mainphotoList.add(main);
        }
        market.setMainphotoList(mainphotoList);


        List<Photos> fileInfoList = new ArrayList<>();        // 첨부파일 정보를 리스트로 생성
        for(MultipartFile file : detailImages) {
            Photos fileInfo = new Photos();
            String originalFileName = file.getOriginalFilename(); // 첨부파일의 실제 파일명
            if(!originalFileName.isEmpty()) {
                String saveFileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));     // 파일 이름을 랜덤으로 설정
                fileInfo.setSaveFile(today);
                fileInfo.setOriginFile(originalFileName);
                fileInfo.setSaveFile(saveFileName);
                fileInfo.setSaveFolder(detailImageSaveFolder);
                file.transferTo(new File(detailImageFolder, saveFileName));    // 파일을 업로드 폴더에 저장
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



    private boolean isValidFileExtension(MultipartFile[] files) {
        // 허용할 파일 확장자 목록
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");

        for (MultipartFile file : files) {
            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();

            if (!allowedExtensions.contains(extension)) {
                return false; // 허용되지 않는 확장자가 포함되어 있으면 false 반환
            }
        }

        return true; // 모든 파일이 허용된 확장자일 경우 true 반환
    }

    @GetMapping("/detail")
    public String marketDetail(@RequestParam("marketNo") int marketNo, Principal principal,Model model)throws Exception{
        MainVO market = marketService.mainlistForDetailVOList(marketNo);
        List<Photos> photosList = photosService.photosList(marketNo);

        String sid = principal != null ? principal.getName() : "";

        Likes likes = new Likes();
        likes.setLoginId(sid);
        likes.setMarketNo(marketNo);
        int chkLiked = likesService.checkLikedMar(likes);
        model.addAttribute("chkLiked",chkLiked);



        if(marketService.marketDetail(marketNo).getReadable() == 0){
            model.addAttribute("photosList",photosList);
            model.addAttribute("market",market);
            model.addAttribute("sid",sid);
            return "market/marketDetail";
        }else {
            model.addAttribute("msg", "열람 불가능한 글입니다.");
            model.addAttribute("url", "/market/marketList");
            return "layout/alert";
        }

    }

    @PostMapping("/marketLike")
    @ResponseBody
    public String marketList(HttpServletRequest request, Principal principal, Model model) {

        String sid = principal != null ? principal.getName() : "";
        int marketNo = Integer.parseInt(request.getParameter("marketNo"));

        String result = "unliked";

        Likes likes = new Likes();
        likes.setLoginId(sid);
        likes.setMarketNo(marketNo);
        int chkLiked = likesService.checkLikedMar(likes);
        model.addAttribute("chkLiked",chkLiked);

        if(chkLiked==0) {
            likesService.addLikeMar(likes);
            result = "liked";
        } else if ( chkLiked == 1) {
            likesService.removeLikeMar(likes);
            result = "unliked";
        }

        return result;
    }

    @GetMapping("/mainImage")
    public ResponseEntity<Resource> download1(@ModelAttribute MainVO dto, HttpServletRequest req) throws IOException {
        String realPath = req.getSession().getServletContext().getRealPath("/resources/static/upload/");
        Path path = Paths.get(realPath + dto.getSaveFolder()+"/"+dto.getSaveFile());
        String contentType = Files.probeContentType(path);
        // header를 통해서 다운로드 되는 파일의 정보를 설정한다.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(dto.getOriginFile(), StandardCharsets.UTF_8)
                .build());
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        Resource resource = new InputStreamResource(Files.newInputStream(path));
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping("/totalImage")
    public ResponseEntity<Resource> download3(@ModelAttribute TotalVO dto,HttpServletRequest req) throws IOException {
        String realPath = req.getSession().getServletContext().getRealPath("/resources/static/upload/");
        Path path = Paths.get(realPath + dto.getMainSaveFolder()+"/"+dto.getMainSaveFile());
        String contentType = Files.probeContentType(path);
        // header를 통해서 다운로드 되는 파일의 정보를 설정한다.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(dto.getMainOriginFile(), StandardCharsets.UTF_8)
                .build());
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        Resource resource = new InputStreamResource(Files.newInputStream(path));
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping("/detailImage")
    public ResponseEntity<Resource> download2(@ModelAttribute DetailVO dto,HttpServletRequest req) throws IOException {
        String realPath = req.getSession().getServletContext().getRealPath("/resources/static/upload/");
        Path path = Paths.get(realPath + dto.getSaveFolder()+"/"+dto.getSaveFile());
        String contentType = Files.probeContentType(path);
        // header를 통해서 다운로드 되는 파일의 정보를 설정한다.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(dto.getOriginFile(), StandardCharsets.UTF_8)
                .build());
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);

        Resource resource = new InputStreamResource(Files.newInputStream(path));
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping("/delete")
    public String marketDelete(@RequestParam("marketNo") int marketNo, Principal principal)throws Exception{

        String sid = principal != null ? principal.getName() : "";

        marketService.marketDelete(marketNo);

        if(sid.equals("admin")){
            return "redirect:/admin/reportList";
        } else {
            return "redirect:/market/marketList";
        }

    }

    @GetMapping("/edit")
    public String marketEdit(@RequestParam int marketNo,MainVO mainVO, Model model,Principal principal) throws Exception{
        MainVO market = marketService.mainlistForDetailVOList(marketNo);
        List<Photos> photosList = photosService.photosList(marketNo);
        List<Mainphoto> mainphotoList = mainphotoService.mainphotoList(marketNo);

        //로그인한 사용자 아이디와 작성자 아이디 비교
        String sid = principal != null ? principal.getName() : "";
        if (!sid.equals(market.getLoginId())){
            return "redirect:/";
        }

        model.addAttribute("mainList",mainphotoList);
        model.addAttribute("photosList",photosList);
        model.addAttribute("market",market);
        return "market/marketEdit";
    }

    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public String edit(Market market, @RequestParam("upfile") MultipartFile[] repImage,@RequestParam("detailFile") MultipartFile[] detailImages ,HttpServletRequest req, Model model, RedirectAttributes rttr, Principal principal) throws Exception {

        String sid = principal != null ? principal.getName() : "";

        String realPath = req.getSession().getServletContext().getRealPath("/resources/static/upload/");

        String today = new SimpleDateFormat("yyMMdd").format(new Date());
        String repImageSaveFolder = "rep_images/" + today;
        String detailImageSaveFolder = "detail_images/" + today;

        File repImageFolder = new File(realPath, repImageSaveFolder);
        File detailImageFolder = new File(realPath, detailImageSaveFolder);

        if (!repImageFolder.exists()) {
            repImageFolder.mkdirs();
        }

        if (!detailImageFolder.exists()) {
            detailImageFolder.mkdirs();
        }
        if(repImage[0].getSize() != 0){
            List<Mainphoto> mainphotoList = mainphotoService.mainphotoList(market.getMarketNo());
            ServletContext application = req.getSession().getServletContext();
            for (Mainphoto mainphoto : mainphotoList) {
                File oldFile = new File(realPath+mainphoto.getSaveFolder()+"/"+mainphoto.getSaveFile());
                if(oldFile.exists()){
                    oldFile.delete();
                }
            }
        }

        if(detailImages[0].getSize() != 0){
            List<Photos> photosList = photosService.photosList(market.getMarketNo());
            ServletContext application = req.getSession().getServletContext();
            for (Photos photos : photosList) {
                File oldFile = new File(realPath+photos .getSaveFolder()+"/"+photos .getSaveFile());
                if(oldFile.exists()){
                    oldFile.delete();
                }
            }
        }

        List<Mainphoto> mainphotoList = new ArrayList<>();
        for (MultipartFile mainphoto : repImage) {
            Mainphoto main = new Mainphoto();
            String originalmainName = mainphoto.getOriginalFilename();
            if (!originalmainName.isEmpty()) {
                String savemainName = UUID.randomUUID().toString() + originalmainName.substring(originalmainName.lastIndexOf("."));
                main.setSaveFile(today);
                main.setSaveFile(savemainName);
                main.setOriginFile(originalmainName);
                main.setSaveFolder(repImageSaveFolder);
                mainphoto.transferTo(new File(repImageFolder, savemainName));
            }
            mainphotoList.add(main);
        }
        market.setMainphotoList(mainphotoList);


        List<Photos> fileInfoList = new ArrayList<>();        // 첨부파일 정보를 리스트로 생성
        for(MultipartFile file : detailImages) {
            Photos fileInfo = new Photos();
            String originalFileName = file.getOriginalFilename(); // 첨부파일의 실제 파일명

            if(!originalFileName.isEmpty()) {
                String saveFileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));     // 파일 이름을 랜덤으로 설정
                fileInfo.setSaveFile(today);
                fileInfo.setOriginFile(originalFileName);
                fileInfo.setSaveFile(saveFileName);
                fileInfo.setSaveFolder(detailImageSaveFolder);
                file.transferTo(new File(detailImageFolder, saveFileName));    // 파일을 업로드 폴더에 저장
            }
            fileInfoList.add(fileInfo);
        }
        market.setFileInfoList(fileInfoList);
        market.setLoginId(sid);

        try {
            marketService.marketEdit(market);
            rttr.addFlashAttribute("msg", "자료실에 글을 등록하였습니다");
        } catch(Exception e) {
            e.printStackTrace();
            rttr.addFlashAttribute("msg", "글 작성 중 문제가 발생했습니다");
        }

        return "redirect:/market/marketList";
    }
}
