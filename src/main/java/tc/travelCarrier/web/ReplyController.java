package tc.travelCarrier.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tc.travelCarrier.domain.AttachDaily;
import tc.travelCarrier.domain.CrudDate;
import tc.travelCarrier.domain.Reply;
import tc.travelCarrier.dto.KwordDTO;
import tc.travelCarrier.dto.ReplyDTO;
import tc.travelCarrier.repository.MemberRepository;
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
    public void createReply(@RequestBody ReplyDTO dto) throws Exception {
        replyService.saveReply(new Reply(
                attachService.findAttachDaily(dto.getAttachNo()),
                dto.getText(),
                memberRepository.getUser(1),
                new CrudDate(dto.getCdate(), dto.getUdate())
        ));
        //실시간 댓글 비동기 새로고침을 위한 데이터
        //replyService.getAllReply
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
               r.getOrigin(),
               r.getUser(),
               r.getId()
            ));
        }
        return replyDTOList;
    }
}
