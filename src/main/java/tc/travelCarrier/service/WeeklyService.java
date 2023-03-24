package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.repository.AttachRepository;
import tc.travelCarrier.repository.WeeklyRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class WeeklyService {

    private final WeeklyRepository weeklyRepository;
    private final AttachRepository attachRepository;
    @Value("${file.dir}")
    private String fileDir;


    /**
     * 위클리 등록
     */
    public int register(MultipartFile file, Weekly weekly) throws IOException {
        weeklyRepository.save(weekly);
        saveAttachWeekly(file,weekly);
        return weekly.getId();
    }


    /**
     * 위클리 첨부파일 저장
     * */
    public int saveAttachWeekly(MultipartFile file, Weekly weekly) throws IOException {
        if(file.isEmpty()){
            return 0;
        }

        // 1. 새로운 파일이름 생성
        String uuid = UUID.randomUUID().toString();
        // 2. 원래 파일이름으로부터 확장자 분리
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        // 3. UUID+확장자명으로 새 파일제목 완성
        String newTitle = uuid+extension;

        // 원본파일 저장경로
        String orgPath = fileDir+"original/"+newTitle;
        // 썸네일로 변환한 파일의 저장경로
        String thumbPath = fileDir+"thumbnails/"+newTitle;

        // AttachWeekly 엔티티 생성
        AttachWeekly attachWeekly = AttachWeekly.builder()
                .attachTitle(newTitle)
                .thumb(thumbPath)
                .weekly(weekly)
                .build();

        // 파일 저장
        // DB구조상 위클리 사진은 썸네일형태로만 저장하기로 되어있음!!
        file.transferTo(new File(thumbPath));
        // DB에 저장 - 이거 if로 나눠서 Repository만 바꾸면 파일저장은 이 메소드에서 끝낼수있을듯
        attachRepository.save(attachWeekly);

        return 1;
    }

    /**
     * 위클리 목록조회
     */
    @Transactional(readOnly = true)
    public List<Weekly> findWeeklies(User user) {
        return weeklyRepository.findByUserId(user);
    }

    /**
     * 위클리 내용조회
     * */
    public Weekly findWeekly(int weeklyId) { return weeklyRepository.findOne(weeklyId);}

    /**
     * 위클리 수정
     */
    /**
     * 위클리 삭제
     */
}
