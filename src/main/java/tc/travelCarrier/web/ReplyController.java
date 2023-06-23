package tc.travelCarrier.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tc.travelCarrier.auth.PrincipalDetails;
import tc.travelCarrier.domain.AttachDaily;
import tc.travelCarrier.domain.CrudDate;
import tc.travelCarrier.domain.Reply;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.dto.KwordDTO;
import tc.travelCarrier.dto.ReplyDTO;
import tc.travelCarrier.repository.MemberRepository;
import tc.travelCarrier.repository.ReplyRepository;
import tc.travelCarrier.service.AttachService;
import tc.travelCarrier.service.ReplyService;
import tc.travelCarrier.service.WeeklyService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/TravelCarrier")
public class ReplyController {

    private final ReplyService replyService;
    private final AttachService attachService;
    private final MemberRepository memberRepository;

    // 댓글/답글 등록 로직
    @PostMapping("/reply/create")
    public ResponseEntity<Map<String, Object>> createReply(@RequestBody ReplyDTO dto,  @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        // origin=0이면 댓글, 1이상이면 답글
        //원댓글 찾기
        System.out.println(dto.toString());
        Reply originReply;
        if(dto.getOrigin() != 0) originReply = replyService.getReply(dto.getOrigin());
        else originReply = null;

        //로그인한 유저의 정보
        User user = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());

        int replyId = replyService.saveReply(new Reply(
                attachService.findAttachDaily(dto.getAttachNo()),
                dto.getText(),
                user,
                new CrudDate(dto.getCdate(), dto.getUdate()),
                originReply
        ), user);

        // 글 작성자
        User weeklyUser = attachService.findAttachDaily(dto.getAttachNo()).getDaily().getWeekly().getUser();

        //replyId, sender, receiver
        Map<String, Object> response = new HashMap<>();
        response.put("replyId", replyId);
        response.put("sender", user.getId()); //방금 댓글 작성한사람
        if(originReply == null) response.put("receiver", weeklyUser.getId()); //댓글일경우 알림받는사람 : 글쓴이
        else response.put("receiver", originReply.getUser().getId()); //답댓일경우 알림받는사람 : 원댓글 작성자

        return ResponseEntity.ok(response);
    }

    // 댓글목록 조회 로직
    @GetMapping("/reply/{attachNo}")
    public List<ReplyDTO> getReplyList(@PathVariable("attachNo") int attachNo,  @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception {
        AttachDaily ad = attachService.findAttachDaily(attachNo);
        List<Reply> replyList = ad.getReplyList(); //그대로 리턴하면 순환참조문제가 발생하므로 dto로 리턴해줄것임
        User activeUser = memberRepository.findUserByEmail( principalDetails.getUser().getEmail());
        List<ReplyDTO> replyDTOList = new ArrayList<>();
        for(Reply r : replyList) {
            replyDTOList.add(new ReplyDTO(
                    r.getAttachDaily().getId(),
                    r.getText(),
                    r.getCrudDate().getCdate(),
                    r.getCrudDate().getUdate(),
                    r.getCrudDate().getDdate(),
                    r.getOrigin(),
                    r.getUser(),
                    r.getId(),
                    activeUser
            ));
        }
        return replyDTOList;
    }

    // 댓글/답글 수정 로직
    @PostMapping("/reply/modify")
    public void modifyReply(@RequestBody ReplyDTO dto) throws Exception {
        // replyId, text, udate가 들어온다.
        replyService.modifyReply(dto);
    }

    // 댓글/답글 삭제 로직
    @PostMapping("/reply/delete")
    public void deleteReply(@RequestBody ReplyDTO dto) throws Exception {
        // replyId, text, udate가 들어온다.
        replyService.deleteReply(dto);
    }
}