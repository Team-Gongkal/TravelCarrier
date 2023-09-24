package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.dto.MyPageDTO;
import tc.travelCarrier.dto.SearchDTO;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.repository.ReplyRepository;
import tc.travelCarrier.repository.WeeklySearchRepository;

import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static tc.travelCarrier.dto.MyPageDTO.generateFollowerDTO;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {
    private final WeeklySearchRepository weeklySearchRepository;
    private final MemberRepository memberRepository;

    //[마이페이지] 에서 user가 다이어리를 검색
    public List<MyPageDTO> findByTitleOrNationNameOrUserNameOrUserEmailContainingForCurrentUser (String keyword, User user, Pageable pageable){
        return transferWeeklyDTO(user, weeklySearchRepository.findByTitleOrNationNameOrUserNameOrUserEmailContainingForCurrentUser(keyword, user, pageable));
    }

    // [트래블러페이지] 에서 user가 다이어리를 검색
    public List<MyPageDTO> findByTitleOrNationNameOrUserNameOrUserEmailContainingForCurrentUser (String keyword, User user, User traveler, Pageable pageable){
        return transferCheckWeeklyDTO(traveler, user, weeklySearchRepository.findByTitleOrNationNameOrUserNameOrUserEmailContainingForCurrentUser(keyword, traveler, pageable));
    }

    //[마이페이지]에서 다이어리 데이터
    public List<MyPageDTO> findWeeklyPaging (User user, Pageable pageable){
        return transferWeeklyDTO(user,weeklySearchRepository.findByUserOrderByIdDesc(user, pageable));
    }
    //[트래블러페이지]에서 다이어리 데이터
    public List<MyPageDTO> findWeeklyPaging (User traveler, User user, Pageable pageable){
        return transferCheckWeeklyDTO(traveler,user,weeklySearchRepository.findByUserOrderByIdDesc(traveler, pageable));
    }

    //[마이페이지]에서 태그된 위클리 데이터
    public List<MyPageDTO> findTagWeeklyPaging (User user, Pageable pageable){
        return transferWeeklyDTO(user,weeklySearchRepository.findTaggedWeekliesByUser(user, pageable));
    }
    //[트래블러페이지]에서 태그된 위클리 데이터
    public List<MyPageDTO> findTagWeeklyPaging (User traveler, User user, Pageable pageable){
        return transferCheckTagWeeklyDTO(traveler,user,weeklySearchRepository.findTaggedWeekliesByUser(traveler, pageable));
    }

    //[마이페이지]에서 팔로워데이터
    public List<MyPageDTO> getFollowingPaging(User user, Pageable pageable) {
        return transferFollowerDTO("following", memberRepository.getFollowingPaging(user, pageable), user);
    }
    
    //[트래블러페이지]에서 팔로워데이터
    public List<MyPageDTO> getFollowingPaging(User traveler, User user, Pageable pageable) {
        return transferFollowerDTO("following", memberRepository.getFollowingPaging(traveler, pageable), user);
    }
    
    //[마이페이지]에서 팔로워 데이터
    public List<MyPageDTO> getFollowerPaging(User user, Pageable pageable) {
        return transferFollowerDTO("follower", memberRepository.getFollowerPaging(user,pageable), user);
    }
    
    //[트래블러페이지]에서 팔로워데이터
    public List<MyPageDTO> getFollowerPaging(User traveler, User user, Pageable pageable) {
        return transferFollowerDTO("follower", memberRepository.getFollowerPaging(traveler,pageable), user);
    }
    
    //[마이페이지]에서 태그된 위클리를 검색
    public List<MyPageDTO> findTaggedWeekliesByKeywordAndUser(String keyword, User user, Pageable pageable) {
        return transferWeeklyDTO(user, weeklySearchRepository.findTaggedWeekliesByKeywordAndUser(keyword, user, pageable));
    }

    //[트래블러페이지]에서 트래블러가 태그된 위클리를 검색
    public List<MyPageDTO> findTaggedWeekliesByKeywordAndUser(String keyword, User user,  User traveler, Pageable pageable) {
        return transferCheckTagWeeklyDTO(traveler, user, weeklySearchRepository.findTaggedWeekliesByKeywordAndUser(keyword, traveler, pageable));
    }


    //[마이페이지]에서 팔로잉/팔로워를 검색
    public List<MyPageDTO> findFollowerByNameAndEmail(String type, String keyword, User user, Pageable pageable) {
        if(type.equals("follower")) return transferFollowerDTO("follower",memberRepository.findFollowerByNameAndEmail(keyword, user, pageable),user);
        else if(type.equals("following")) return transferFollowerDTO("following",memberRepository.findFollowingByNameAndEmail(keyword, user, pageable),user);
        else return null;
    }

    //[트래블러페이지]에서 트래블러의 팔로잉/팔로워를 검색
    public List<MyPageDTO> findFollowerByNameAndEmail(String type, String keyword, User user, User traveler, Pageable pageable) {
        if(type.equals("follower")) return transferFollowerDTO("follower",memberRepository.findFollowerByNameAndEmail(keyword, traveler, pageable),user);
        else if(type.equals("following")) return transferFollowerDTO("following",memberRepository.findFollowingByNameAndEmail(keyword, traveler, pageable),user);
        else return null;
    }

    //[마이페이지]에서 다이어리 기간검색
    public List<MyPageDTO> findWeeklyPagingByDate(SearchDTO searchDTO, User user, Pageable pageable) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sdate = sdf.parse(searchDTO.getSdate());
        Date edate = sdf.parse(searchDTO.getEdate());
        return transferWeeklyDTO(user, weeklySearchRepository.findWeeklyPagingByDate(sdate, edate, user, pageable));
    }

    //[트래블러페이지]에서 다이어리 기간검색
    public List<MyPageDTO> findWeeklyPagingByDate(SearchDTO searchDTO, User user, User traveler, Pageable pageable) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sdate = sdf.parse(searchDTO.getSdate());
        Date edate = sdf.parse(searchDTO.getEdate());
        return transferCheckWeeklyDTO(traveler, user, weeklySearchRepository.findWeeklyPagingByDate(sdate, edate, traveler, pageable));
    }

    //[마이페이지]에서 태그된 위클리 기간검색
    public List<MyPageDTO> findTagWeeklyPagingByDate(SearchDTO searchDTO, User user, Pageable pageable) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sdate = sdf.parse(searchDTO.getSdate());
        Date edate = sdf.parse(searchDTO.getEdate());
        return transferWeeklyDTO(user, weeklySearchRepository.findTagWeeklyPagingByDate(sdate, edate, user, pageable));
    }

    //[트래블러페이지]에서 태그된 위클리 기간검색
    public List<MyPageDTO> findTagWeeklyPagingByDate(SearchDTO searchDTO, User user,  User traveler, Pageable pageable) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sdate = sdf.parse(searchDTO.getSdate());
        Date edate = sdf.parse(searchDTO.getEdate());
        return transferCheckWeeklyDTO(traveler, user, weeklySearchRepository.findTagWeeklyPagingByDate(sdate, edate,  traveler, pageable));
    }



    //다이어리, 태그된 다이어리 데이터를 DTO객체로 단순 변환하는 메소드 (마이페이지에서 사용 - 로그인객체와 상관없이 본인글은 전부 보여야함)
    private List<MyPageDTO> transferWeeklyDTO(User user, Page<Weekly> weeklyPage) {
        // dto : weeklyId, title, date, thumbPath, goWithList
        List<MyPageDTO> result = new ArrayList<>();
        for(Weekly w : weeklyPage){
            List<String> users = new ArrayList<>();
            boolean hide = false;
            for(Gowith g : w.getGowiths()) {
                users.add(g.getUser().getAttachUser().getThumbPath());
                if(g.getUser().getId() == user.getId()) hide = g.getHide();
            }

            MyPageDTO dto = MyPageDTO.weeklyBuilder()
                    .id(w.getId()).title(w.getTitle()).date(w.getTravelDate())
                    .thumbPath(w.getAttachWeekly().getThumbPath())
                    .goWithList(users)
                    .hide(hide)
                    .build();

            result.add(dto);
        }

        return result;
    }

    //팔로잉 데이터를 DTO로 변환하는 메소드 (각 로그인 유저에 맞춰 표시)
    private List<MyPageDTO> transferFollowerDTO(String type, Page<Follower> fPage, User loginUser) {
        // dto : weeklyId, title, date, thumbPath, goWithList
        List<MyPageDTO> result = new ArrayList<>();
        if(type.equals("following")) {
            for (Follower f : fPage) {
                // 팔로잉목록에 로그인유저의 친구가 있는지 확인..
                boolean fff = false;
                for (Follower fo : loginUser.getFollowers()){
                    System.out.println("!!"+fo.getFollower().getId()+", "+f.getFollower().getId());
                    if(fo.getFollower().getId() == f.getFollower().getId()) {
                        fff=true;
                        break;
                    }
                }
                //본인이 있는 경우의수도 고려해야함
                boolean fffSelf = false;
                System.out.println("??"+f.getFollower().getId() +" "+ loginUser.getId());
                if(f.getFollower().getId() == loginUser.getId()) fffSelf = true;

                result.add(generateFollowerDTO(f.getFollower().getName(), f.getFollower().getId(), f.getFollower().getAttachUser().getThumbPath(),
                        f.getFollower().getAttachUserBackground() == null ? null : f.getFollower().getAttachUserBackground().getThumbPath(),
                        f.getFDate(), f.getFollower().getEmail(),fff,fffSelf));
                //System.out.println(f.getFollower().getName()+", "+dto.toString());
            }
        } else if(type.equals("follower")) {
            for (Follower f : fPage) {
                //맞팔인지 확인해야함 즉 이사람이 내 follwing에도 있어야함..
                boolean fff = false;
                for (Follower fo : loginUser.getFollowers()){ //본인의 팔로워 돌면서 겹치는거 찾기 -> 같으면 맞팔인것!
                    //System.out.println("!!"+fo.getFollower().getName()+", "+f.getUser().getName());
                    if(fo.getFollower().getId() == f.getUser().getId()) {
                        fff=true;
                        break;
                    }
                }

                //본인이 있는 경우의수도 고려해야함
                boolean fffSelf = false;
                if(f.getUser().getId() == loginUser.getId()) fffSelf = true;

                result.add(generateFollowerDTO(f.getUser().getName(), f.getUser().getId(), f.getUser().getAttachUser().getThumbPath(),
                        f.getUser().getAttachUserBackground() == null ? null : f.getUser().getAttachUserBackground().getThumbPath(),
                        f.getFDate(), f.getUser().getEmail(),fff,fffSelf));
                //System.out.println(f.getFollower().getName()+", "+dto.toString());
            }
        }
        return result;
    }


    //다이어리 데이터중 로그인 유저가 접근할수있는 데이터만 남기기
    private List<MyPageDTO> transferCheckWeeklyDTO(User traveler, User user, Page<Weekly> weeklyPage) {
        // dto : weeklyId, title, date, thumbPath, goWithList
        List<MyPageDTO> result = new ArrayList<>();
        //내가 traveler의 팔로잉목록에 있는지 확인
        boolean isFollowUser = false;
        for(Follower f : traveler.getFollowers()){
            if(f.getFollower().getId() == user.getId()){
                isFollowUser = true;
                break;
            }
        }
        for(Weekly w : weeklyPage){
            if(w.getStatus() == OpenStatus.FOLLOW && !isFollowUser) continue;
            if(w.getStatus() == OpenStatus.ME){
                boolean flag = false;
                for(Gowith g : w.getGowiths()) {
                    if(g.getUser().getId() == user.getId()){
                        flag = true;
                    }
                }
                if(!flag) continue;
            }

            List<String> users = new ArrayList<>();
            boolean hide = false;
            for(Gowith g : w.getGowiths()) {
                users.add(g.getUser().getAttachUser().getThumbPath());
                if(g.getUser().getId() == user.getId()) hide = g.getHide();
            }

            MyPageDTO dto = MyPageDTO.weeklyBuilder()
                    .id(w.getId()).title(w.getTitle()).date(w.getTravelDate())
                    .thumbPath(w.getAttachWeekly().getThumbPath())
                    .goWithList(users)
                    .hide(hide)
                    .build();

            result.add(dto);
        }

        return result;
    }

    //태그된 다이어리중 로그인유저가 접근할 수 있는 데이터만 남기기
    //tag : ALL, FOLLOW(user가 글쓴이의 팔로잉목록에 있을경우만), ME(Gowith에 user가 있거나 글쓴이가 user일경우)
    private List<MyPageDTO> transferCheckTagWeeklyDTO(User traveler, User user, Page<Weekly> weeklyPage) {
        // dto : weeklyId, title, date, thumbPath, goWithList
        List<MyPageDTO> result = new ArrayList<>();
        for(Weekly w : weeklyPage){
            // FOLLOWER : 나도 글쓴이의 팔로워이거나 글쓴이가 본인이라면 띄우기
            //즉 글쓴이가 test2이고, 로그인한사람이 test2이면 자기자신은 스스로의 팔로워가아니므로 뜨지않는 오류를 해결
            if(w.getStatus() == OpenStatus.FOLLOW){
                boolean isFollowUser = false;
                for(Follower f : w.getUser().getFollowers()){
                    if(f.getFollower().getId() == user.getId()){
                        isFollowUser = true;
                        break;
                    } else if(w.getUser().getId() == user.getId()){
                        isFollowUser = true;
                        break;
                    }
                }
                if(!isFollowUser) continue;
            }
            if(w.getStatus() == OpenStatus.ME){
                if(w.getUser().getId() != user.getId()){
                    boolean flag = false;
                    for(Gowith g : w.getGowiths()) {
                        if(g.getUser().getId() == user.getId()){
                            flag = true;
                        }
                    }
                    if(!flag) continue;
                }
            }
            //hide된 내용은 보이면 안됨
            List<String> users = new ArrayList<>();
            boolean hide = false;
            for(Gowith g : w.getGowiths()) {
                users.add(g.getUser().getAttachUser().getThumbPath());
                if(g.getUser().getId() == traveler.getId()) hide = g.getHide();
            }
            if(hide) continue;

            MyPageDTO dto = MyPageDTO.weeklyBuilder()
                    .id(w.getId()).title(w.getTitle()).date(w.getTravelDate())
                    .thumbPath(w.getAttachWeekly().getThumbPath())
                    .goWithList(users)
                    .hide(hide)
                    .build();

            result.add(dto);
        }
        for(MyPageDTO d : result){
            System.out.println(d);
        }
        return result;
    }


}
