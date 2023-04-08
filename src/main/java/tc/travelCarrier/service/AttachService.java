package tc.travelCarrier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tc.travelCarrier.domain.AttachDaily;
import tc.travelCarrier.domain.AttachWeekly;
import tc.travelCarrier.domain.Daily;
import tc.travelCarrier.domain.Weekly;
import tc.travelCarrier.dto.DailyForm;
import tc.travelCarrier.repository.AttachRepository;
import tc.travelCarrier.repository.WeeklyRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RequiredArgsConstructor
@Service
@Transactional
public class AttachService {
    @Value("${file.dir}")
    private String fileDir;

    private final AttachRepository attachRepository;
    private final WeeklyRepository weeklyRepository;

    /**
     * 데일리 폼 저장하는 메소드
     * */
    public String saveAttachDaily(Weekly weekly, Map<String, List<DailyForm>> dailyFormMap) throws Exception {
        //1.서버에 파일 저장
        for (Map.Entry<String, List<DailyForm>> entry : dailyFormMap.entrySet()) {
            String day = entry.getKey();
            List<DailyForm> dfList = entry.getValue();
            for (DailyForm form : dfList) {
                //각 첨부파일을 서버에 저장
                String[] saveArr = saveAttach(form.getFile(), "daily"); //{이름, 첨부파일경로}
                form.setNewFileName(saveArr[0]);
                form.setPath(saveArr[1]);
            }
            // DAY1에 대한 모든 첨부파일을 데일리 엔티티 세팅해서
            // DB저장
            weekly.addDailies(Daily.createDaily(day, dfList));
        }
        //DB에 저장
        weeklyRepository.save(weekly);
        return "success";
    }

    /**
     * 위클리 폼 저장하는 메소드
     * */
    public int saveAttachWeekly(MultipartFile file, Weekly weekly) throws Exception {
        //1.서버에 파일 저장
        String[] saveArr = saveAttach(file,"weekly");

        //2.AttachWeekly 엔티티 생성해서 DB에도 저장
        AttachWeekly attachWeekly = AttachWeekly.builder()
                .attachTitle(saveArr[0])
                .thumb(saveArr[1])
                .weekly(weekly)
                .build();
        attachRepository.save(attachWeekly);

        return 1;
    }

    /**
     * 첨부파일을 서버에 저장하는 메소드
     * */
    public String[] saveAttach(MultipartFile file, String folder) throws Exception {
        if(file.isEmpty()){
            //return 0;
        }

        // 1. 새로운 파일이름 생성
        String uuid = UUID.randomUUID().toString();
        // 2. 원래 파일이름으로부터 확장자 분리
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        // 3. UUID+확장자명으로 새 파일제목 완성
        String newTitle = uuid+"."+extension;

        // 썸네일로 변환한 파일의 저장경로
        String thumbPath = fileDir+folder+"/"+newTitle;

        // 이미지 저장
        boolean a = ImageIO.write(resizeImageFile(file), extension,
                new File(thumbPath));

        String[] saveArr = {newTitle,thumbPath};

        return saveArr;
    }

    /**
     * 이미지 크기 리사이징(크기는 데일리 슬라이드에 대략 맞춤)
     * */
    private BufferedImage resizeImageFile(MultipartFile file) throws Exception {
        try (InputStream inputStream = file.getInputStream()){
            // 이미지 읽어 오기
            BufferedImage inputImage = ImageIO.read(file.getInputStream());
            // 이미지 세로 가로 측정
            int originWidth = inputImage.getWidth();
            int originHeight = inputImage.getHeight();
            // 변경할 길이
            int newWidth = originWidth;
            int newHeight = originHeight;
            if(originWidth == originHeight) {
                newWidth = 320;
                newHeight = 320;
            }
            else if(originWidth > originHeight) {
                int min = Math.min(originWidth/480,originHeight/320);
                min = Math.max(min,1);
                newWidth /= min;
                newHeight /= min;
            }
            else if(originWidth < originHeight) {
                int min = Math.min(originWidth/240,originHeight/320);
                min = Math.max(min,1);
                newWidth /= min;
                newHeight /= min;
            }
            Image resizeImage = inputImage.getScaledInstance(newWidth, newHeight, Image.SCALE_FAST);
            BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = newImage.getGraphics();
            try {
                graphics.drawImage(resizeImage, 0, 0, null);
            } finally {
                graphics.dispose(); // Graphics 객체 close
            }
            return newImage;
        } catch (IOException e){
            throw new Exception("Failed to resize image", e);
        }
    }

}
