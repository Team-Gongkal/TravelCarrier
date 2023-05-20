package tc.travelCarrier.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/TravelCarrier")
public class ReplyController {

    private final ReplyService replyService;
    private final AttachService attachService;
    private final MemberRepository memberRepository;

    @PostMapping("/reply/create")
    public int createReply(@RequestBody ReplyDTO dto) throws Exception {
        // origin=0이면 댓글, 1이상이면 답글
        //원댓글 찾기
        System.out.println(dto.toString());
        Reply originReply;
        if(dto.getOrigin() != 0) originReply = replyService.getReply(dto.getOrigin());
        else originReply = null;

        //로그인한 유저의 정보
        User activeUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = memberRepository.findUserById(activeUser.getId());

        int replyId = replyService.saveReply(new Reply(
                attachService.findAttachDaily(dto.getAttachNo()),
                dto.getText(),
                user,
                new CrudDate(dto.getCdate(), dto.getUdate()),
                originReply
        ));
        return replyId;
    }
    @GetMapping("/reply/{attachNo}")
    public List<ReplyDTO> getReplyList(@PathVariable("attachNo") int attachNo) throws Exception {
        AttachDaily ad = attachService.findAttachDaily(attachNo);
        List<Reply> replyList = ad.getReplyList(); //그대로 리턴하면 순환참조문제가 발생하므로 dto로 리턴해줄것임

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
                    r.getId()
            ));
        }
        return replyDTOList;
    }

    @PostMapping("/reply/modify")
    public void modifyReply(@RequestBody ReplyDTO dto) throws Exception {
        // replyId, text, udate가 들어온다.
        replyService.modifyReply(dto);
    }
    @PostMapping("/reply/delete")
    public void deleteReply(@RequestBody ReplyDTO dto) throws Exception {
        // replyId, text, udate가 들어온다.
        replyService.deleteReply(dto);
    }
}