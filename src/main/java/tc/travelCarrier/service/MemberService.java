package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.dto.MemberInfoDTO;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    public void updateMemberInfo(MemberInfoDTO dto, User user){
        user.setName(dto.getNickName());
    }
}
