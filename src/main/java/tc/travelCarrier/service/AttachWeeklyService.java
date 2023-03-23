package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tc.travelCarrier.domain.AttachWeekly;
import tc.travelCarrier.domain.Weekly;
import tc.travelCarrier.repository.AttachWeeklyRepository;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class AttachWeeklyService {

    @Value("${file.dir}")
    private String fileDir;

    private final AttachWeeklyRepository weeklyRepository;

    public int saveAttachWeekly(MultipartFile file) throws IOException{
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

        System.out.println("++++++++++++++");
        System.out.println(orgPath);
        System.out.println(thumbPath);
        Weekly tmp = new Weekly();
        tmp.setId(2);
        // AttachWeekly 엔티티 생성
        AttachWeekly attachWeekly = AttachWeekly.builder()
                .attachTitle(newTitle)
                .thumb(thumbPath)
                .weekly(tmp)
                .build();

        // 파일 저장
        // DB구조상 위클리 사진은 썸네일형태로만 저장하기로 되어있음!!
        file.transferTo(new File(thumbPath));
        // DB에 저장 - 이거 if로 나눠서 Repository만 바꾸면 파일저장은 이 메소드에서 끝낼수있을듯
        weeklyRepository.save(attachWeekly);

        return 1;
    }

}
