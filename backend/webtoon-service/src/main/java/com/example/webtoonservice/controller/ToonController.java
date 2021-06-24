package com.example.webtoonservice.controller;


import com.example.webtoonservice.exception.AppException;
import com.example.webtoonservice.exception.FileStorageException;
import com.example.webtoonservice.exception.ResourceNotFoundException;
import com.example.webtoonservice.model.*;
import com.example.webtoonservice.repository.*;
import com.example.webtoonservice.payload.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RequestMapping("/webtoon-service")
@RestController
public class ToonController {

    private static final Logger logger = LoggerFactory.getLogger(ToonController.class);

    @Autowired
    private ToonRepository toonRepository;
    
    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ToonThumbnailRepository toonThumbnailRepository;

    @Autowired
    private EpiThumbnailRepository epiThumbnailRepository;

    @Autowired
    private EpiToonRepository epiToonRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RateRepository rateRepository;

    @Autowired
    private FavRepository favRepository;


    
    // 새 웹툰 등록
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PostMapping(value = "/newAdd", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Toon newAdd(@RequestParam("title") String title, @RequestParam("artist") String artist,
            @RequestParam("day") String day, @RequestParam("genre") String genre,
            @RequestParam("file") MultipartFile file, @CurrentSecurityContext(expression="authentication.name") String username) {

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
            Toon toon = new Toon(title, artist, day, genre, user);
            ToonThumbnail toonThumbnail = this.uploadsSave(file);
            toon.setToonThumbnail(toonThumbnail);
            toonThumbnail.setToon(toon);
            Toon result = toonRepository.save(toon);
            return result;
    }

    // 웹툰썸네일 업로드
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PostMapping(value="/uploads")
    public ToonThumbnail uploadsSave(MultipartFile file) {
            // 파일 이름 -> UUID
            UUID uid = UUID.randomUUID();
            String filename = uid.toString() + file.getContentType().replace('/','.').toString();
            String fileName = StringUtils.cleanPath(filename);

            try {
                File targetFile = new File("src/main/resources/static/uploads/" + fileName);
                File targetFile_2 = new File("target/classes/static/uploads/" + fileName);
                // 파일명에 부적합 문자가 있는지 확인한다.
                if(fileName.contains("..")) {
                    throw new FileStorageException("파일명에 부적합 문자가 포함되어 있습니다. " + fileName);}
                    // 파일 복사
                    FileCopyUtils.copy(file.getBytes(), targetFile);
                    FileCopyUtils.copy(file.getBytes(), targetFile_2);

                String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/static/uploads/")
                        .toUriString() + filename;

                ToonThumbnail toonThumbnail = new ToonThumbnail(fileName, file.getContentType(), fileUri, file.getSize());
                return toonThumbnail;

            } catch (IOException ex) {
                throw new FileStorageException("Could not store file " +fileName + ". Please try again!", ex);
            }
    }


    // 에피썸네일 업로드
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PostMapping(value="/thumbnails")
    public EpiThumbnail thumbnailSave(MultipartFile file) {
        // 파일 이름 -> UUID
        UUID uid = UUID.randomUUID();
        String filename = uid.toString() + file.getContentType().replace('/','.').toString();
        String fileName = StringUtils.cleanPath(filename);

        try {
            File targetFile = new File("src/main/resources/static/thumbnails/" + fileName);
            File targetFile_2 = new File("target/classes/static/thumbnails/"  + fileName);
            // 파일명에 부적합 문자가 있는지 확인한다.
            if(fileName.contains("..")) {
                throw new FileStorageException("파일명에 부적합 문자가 포함되어 있습니다. " + fileName);}
            // 파일 복사
            FileCopyUtils.copy(file.getBytes(), targetFile);
            FileCopyUtils.copy(file.getBytes(), targetFile_2);

            String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/thumbnails/")
                    .toUriString() + filename;

            EpiThumbnail epiThumbnail = new EpiThumbnail(fileName, file.getContentType(), fileUri, file.getSize());
            return epiThumbnail;

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " +fileName + ". Please try again!", ex);
        }
    }


    // 에피소드툰 업로드
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PostMapping(value="/toons")
    public EpiToon ToonSave(MultipartFile file) {
        // 파일 이름 -> UUID
        UUID uid = UUID.randomUUID();
        String filename = uid.toString() + file.getContentType().replace('/','.').toString();
        String fileName = StringUtils.cleanPath(filename);

        try {
            File targetFile = new File("src/main/resources/static/toons/" + fileName);
            File targetFile_2 = new File("target/classes/static/toons/" + fileName);
            // 파일명에 부적합 문자가 있는지 확인한다.
            if(fileName.contains("..")) {
                throw new FileStorageException("파일명에 부적합 문자가 포함되어 있습니다. " + fileName);}
            // 파일 복사
            FileCopyUtils.copy(file.getBytes(), targetFile);
            FileCopyUtils.copy(file.getBytes(), targetFile_2);

            String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/toons/")
                    .toUriString() + filename;

            EpiToon epiToon = new EpiToon(fileName, file.getContentType(), fileUri, file.getSize());
            return epiToon;

        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " +fileName + ". Please try again!", ex);
        }
    }


    // 새 에피소드 등록
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PostMapping(value = "/newEpi", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Episode newEpi(@RequestParam("epiTitle") String epiTitle, @RequestParam("toonId") Toon toon,
            @RequestParam("eFile") MultipartFile eFile, @RequestParam("mFile") MultipartFile mFile) {

        
        Episode episode = new Episode(epiTitle, toon);
        EpiThumbnail epiThumbnail = this.thumbnailSave(eFile);
        EpiToon epiToon = this.ToonSave(mFile);

        episode.setEpiToon(epiToon);
        epiToon.setEpisode(episode);

        episode.setEpiThumbnail(epiThumbnail);
        epiThumbnail.setEpisode(episode);

        Episode result = episodeRepository.save(episode);
        
        return result;

    }



    // 새 코멘트 등록
    @PostMapping("/saveComment/{id}")
    public Episode createComment(@PathVariable int id, @RequestParam("user") String user, @RequestParam("comment") String comment) {
        Episode epi = episodeRepository.findById(id).get();
        Comment com = new Comment(user, comment);
        com.setEpisode(epi);
        
        epi.getComments().add(com);
        return episodeRepository.save(epi);
    }



    // 선호작품 등록
    @PostMapping("/saveFav/{id}")
    public Toon saveFav(@PathVariable int id, @RequestParam("user") String user, @RequestParam("title")String title, @RequestParam("webtoonId")Integer webtoonId){
        Toon toon = toonRepository.findById(id).get();
        Fav fav = new Fav(user, title, toon);
        toon.getFav().add(fav);
        return toonRepository.save(toon);
    }



    // 특정 webtoonHome에서 선호작품 삭제
    @DeleteMapping("/deleteFav/{id}/{user}")
    public void deleteFav(@PathVariable("id") int id, @PathVariable("user") String user){
        favRepository.deleteFav(id, user);
    }


    // 선호작품 삭제
    @DeleteMapping("/deleteFavById/{id}")
    public void defeFavById(@PathVariable("id") int id){
        favRepository.deleteById(id);
    }

    // 선호작품 가져오기
    @GetMapping("/getFav/{user}")
    public Collection<Fav> getFav(@PathVariable("user") String user){
        return favRepository.getFav(user);
    }

    // 유저의 선호 작품인지 조회
    @GetMapping("/getFavById/{tno}/{user}")
    public Collection<Fav> getFav(@PathVariable("tno") Integer tno, @PathVariable("user") String user){
        return favRepository.getFavById(tno, user);
    }

    //수정한 댓글 업로드
    @PutMapping("/uploadEditComment/{id}")
    public Comment uploadEditComment(@PathVariable int id, @RequestParam("comment") String comment){
        Comment com = commentRepository.findById(id).get();
        com.setComment(comment);
        return commentRepository.save(com);
    }

    // Rate 등록
    @PostMapping("/uploadRate/{id}")
    public Episode uploadRate(@PathVariable int id, @RequestParam("user") String user, @RequestParam("rate") Integer rate){
        Episode epi = episodeRepository.findById(id).get();
        Rate r = new Rate(user, rate);
        r.setEpisode(epi);
        epi.getRate().add(r);
        return episodeRepository.save(epi);
    }

    // 기존 Rate 가져오기
    @GetMapping(value={"/fetchRate/{id}/{user}"})
    public Optional<Rate> fetchRate(@PathVariable("id") int id, @PathVariable("user") String user){
        return rateRepository.getRateByEpiId(id, user);
    }

    // Rate 수정
    @PutMapping("/uploadEditRate/{id}")
    public Rate uploadEditRate(@PathVariable int id,@RequestParam("user") String user, @RequestParam("rate") Integer rate){
        Rate r = rateRepository.getRateByEpiId(id, user).get();
        r.setRate(rate);
        return rateRepository.save(r);
    }


    // 새 에피소드 등록을 위한 webtoonId 값 가져오기
    @GetMapping("/getToonIdAndName")
    public List<Map<String, Object>> getTIAN() {
        return toonRepository.getToonIdAndName();
    }


    // 작가의 웹툰정보(id와 이름) 가져오기
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @GetMapping("/getToonIdAndName/me")
    public List<Map<String, Object>> getTIANm(@CurrentSecurityContext(expression="authentication.name") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return toonRepository.getToonIdAndNameByUser(user.getId());
    }


    // 만화 전체 + 에피소드 전체 + 댓글 전체 + 별점 전체 + 선호도 전체
    @GetMapping("/getToon")
    public Collection<Toon> getToon() {
        return toonRepository.findAll();
    }


    //자신이 만든 웹툰만 find
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @GetMapping("/getToon/me")
    public Collection<Toon> getToonme(@CurrentSecurityContext(expression="authentication.name") String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return toonRepository.findByUser(user.getId());
    }

    // 웹툰id로 에피소드 전체 응답
    @GetMapping("/getEpi/{id}")
    public Collection<Episode> getEpi(@PathVariable int id) {
        return episodeRepository.getEpi(id);
    }

    // 웹툰id로 댓글 전체 응답
    @GetMapping("/getComment/{id}")
    public Collection<Comment> getComment(@PathVariable int id) {
        return commentRepository.getComment(id);
    }


    // 애피소드 id로 특정 애피소드 가져오기
    // 이때 대여권이 있는 사람만 열리도록 check!@@@@@@@@@
    @GetMapping("/getEpiById/{id}")
    public Optional<Episode> getEpiById(@PathVariable int id, @CurrentSecurityContext(expression="authentication.name") String username) {
        return episodeRepository.findById(id);
    }


    // 웹툰 id 로 특정 웹툰관련정보 가져오기
    @GetMapping("/getToonById/{id}")
    public Optional<Toon> getToonById(@PathVariable int id) {
        return toonRepository.findById(id);
    }

    //웹툰썸네일 id로 특정썸네일정보 가져오기
    @GetMapping("/getToonThumbnailById/{id}")
    public Optional<ToonThumbnail> getToonThumbnailById(@PathVariable int id) {
        return toonThumbnailRepository.getToonThumbnailByID(id);
    }



    // 썸네일 삭제
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @DeleteMapping("/deleteToonThumbnail/{id}")
    public void deleteToonThumbnail(@PathVariable Integer id) {
        toonThumbnailRepository.deleteToonThumbnail(id);
    }

    // 기존 웹툰 삭제
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @DeleteMapping("/deleteToon/{id}")
    public void deleteToon(@PathVariable Integer id) {
        toonRepository.deleteById(id);
    }

    // 특정 코멘트 삭제
    @DeleteMapping("/deleteComment/{id}")
    public void deleteCommen(@PathVariable Integer id){
        commentRepository.deleteById(id);
    }

    //기존 에피소드 삭제
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @DeleteMapping("/deleteEpi/{id}")
    public void deleteEpi(@PathVariable Integer id) {
        episodeRepository.deleteById(id);
    }

    //기존 에피소드 수정을 위해 한 에피소드 가져오기
    @GetMapping("/getEditEpi/{id}")
    public episodeEpiPayload getEditEpiById(@PathVariable int id){
        Episode epi = episodeRepository.findById(id).orElse(null);
        episodeEpiPayload epiinfo = new episodeEpiPayload();
        if(epi!=null){
            epiinfo.setEpiTitle(epi.getEpiTitle());
            epiinfo.setEno(epi.getEno());
            epiinfo.setWebtoonId(epi.getToon().getTno());
            epiinfo.setWebtoonTitle(epi.getToon().getTitle());
        }
        return epiinfo;
    }

    //에피소드 수정 용 웹툰 타이틀 가져오기
    @GetMapping("/getToonTitle/{id}")
    public Optional<Toon> getToonTitle(@PathVariable int id){
        return toonRepository.findById(id);
    }

    


    // 수정한 웹툰 업로드
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PutMapping(value = "/uploadEditToon/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Toon uploadEditToon(@PathVariable int id, @RequestParam("title") String title, @RequestParam("artist") String artist,
            @RequestParam("day") String day, @RequestParam("genre") String genre,
            @RequestParam("file") MultipartFile file, @CurrentSecurityContext(expression="authentication.name") String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Toon toon = toonRepository.findById(id).get();
        toon.setTitle(title);
        toon.setArtist(artist);
        toon.setDay(day);
        toon.setGenre(genre);
        toon.setUser(user);

        ToonThumbnail toonThumbnail = this.uploadsSave(file);
        
        toon.setToonThumbnail(toonThumbnail);

        toonThumbnail.setToon(toon);

        Toon result = toonRepository.save(toon);

        return result;

    }

    // 수정한 웹툰 업로드 (파일 바뀌지 않았을 때)
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PutMapping(value = "/uploadEditToonExceptFile/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Toon uploadEditToonExceptFile(@PathVariable int id, @RequestParam("title") String title, @RequestParam("artist") String artist,
            @RequestParam("day") String day, @RequestParam("genre") String genre,  @CurrentSecurityContext(expression="authentication.name") String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Toon toon = toonRepository.findById(id).get();
        toon.setTitle(title);
        toon.setArtist(artist);
        toon.setDay(day);
        toon.setGenre(genre);
        toon.setUser(user);

        Toon result = toonRepository.save(toon);

        return result;

    }



    // 수정한 에피소드 업로드
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PutMapping(value = "/uploadEditEpi/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Episode uploadEditEpi(@PathVariable int id, @RequestParam("epiTitle") String epiTitle,
            @RequestParam("eFile") MultipartFile eFile, @RequestParam("mFile") MultipartFile mFile) {
        

        Episode episode = episodeRepository.findById(id).get();
        episode.setEpiTitle(epiTitle);

        EpiThumbnail epiThumbnail = this.thumbnailSave(eFile);
        EpiToon epiToon = this.ToonSave(mFile);

        
        episode.setEpiToon(epiToon);
        epiToon.setEpisode(episode);

        episode.setEpiThumbnail(epiThumbnail);
        epiThumbnail.setEpisode(episode);

        Episode result = episodeRepository.save(episode);

        return result;

    }

    // 수정한 에피소드 업로드 (문서만 변경됐을 경우)
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PutMapping(value = "/uploadEditEpiExceptTaM/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Episode uploadEditEpiExceptTaM(@PathVariable int id, @RequestParam("epiTitle") String epiTitle) {
        

        Episode episode = episodeRepository.findById(id).get();
        episode.setEpiTitle(epiTitle);

        Episode result = episodeRepository.save(episode);

        return result;

    }

    // 수정한 에피소드 업로드(썸네일만 변경됐을 경우)
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PutMapping(value = "/uploadEditEpiExceptM/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Episode uploadEditEpiExceptM(@PathVariable int id, @RequestParam("epiTitle") String epiTitle,
            @RequestParam("eFile") MultipartFile eFile) {
        

        Episode episode = episodeRepository.findById(id).get();
        episode.setEpiTitle(epiTitle);

        EpiThumbnail epiThumbnail = this.thumbnailSave(eFile);
        

        episode.setEpiThumbnail(epiThumbnail);
        epiThumbnail.setEpisode(episode);

        Episode result = episodeRepository.save(episode);

        return result;

    }

    // 수정한 에피소드 업로드(본문만 변경됐을 경우)
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PutMapping(value = "/uploadEditEpiExceptT/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Episode uploadEditEpiExceptT(@PathVariable int id, @RequestParam("epiTitle") String epiTitle,
            @RequestParam("mFile") MultipartFile mFile) {
        

        Episode episode = episodeRepository.findById(id).get();
        episode.setEpiTitle(epiTitle);

        EpiToon epiToon = this.ToonSave(mFile);

        
        
        episode.setEpiToon(epiToon);
        epiToon.setEpisode(episode);


        Episode result = episodeRepository.save(episode);

        return result;

    }



    @GetMapping("/getEpiThumbnailById/{id}")
    public Optional<EpiThumbnail> getEpiThumbnailById(@PathVariable int id) {
        return epiThumbnailRepository.getEpiThumbnailById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @DeleteMapping("/deleteEpiThumbnail/{id}")
    public void deleteEpiThumbnail(@PathVariable Integer id) {
        epiThumbnailRepository.deleteEpiThumbnail(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @DeleteMapping("/deleteEpiToon/{id}")
    public void deleteEpiToon(@PathVariable Integer id) {
        epiToonRepository.deleteEpiToon(id);
    }

    @GetMapping("/getEpiToon/{id}")
    public Optional<EpiToon> getEpiToon(@PathVariable int id) {
        return epiToonRepository.getEpiToon(id);
    }

    // 평균별점가져오기
    @GetMapping("/getAvgRate/{id}")
    public Double getAvgRate(@PathVariable int id){
        return rateRepository.getAvgRate(id);
    }

    
}
