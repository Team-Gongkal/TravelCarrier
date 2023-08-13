package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tc.travelCarrier.domain.AttachUser;
import tc.travelCarrier.domain.User;
import tc.travelCarrier.domain.Weekly;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final AttachService attachService;
    /**
     * 위클리 등록
     */
    public void register(MultipartFile file, User user) throws Exception {
        // 프로필 사진 정보 저장
        //weeklyRepository.save(weekly);

        // 서버에 프사 저장
        attachService.saveAttachUser(file, user);
    }
}
