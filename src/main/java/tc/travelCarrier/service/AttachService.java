package tc.travelCarrier.service;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.Directory;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.ResampleOp;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tc.travelCarrier.domain.AttachWeekly;
import tc.travelCarrier.domain.Daily;
import tc.travelCarrier.domain.Weekly;
import tc.travelCarrier.dto.DailyForm;
import tc.travelCarrier.repository.AttachRepository;
import tc.travelCarrier.repository.WeeklyRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;




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
    public void saveAttachDaily(Weekly weekly, Map<String, List<DailyForm>> dailyFormMap) throws Exception {
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
            // DB저장 - 만약 이전에 해당 DAY가 있었으면 거기에 추가만 하면 됨..
            boolean flag = false;
            for(Daily d : weekly.getDailys()){
                if(day.equals(d.getDailyDate())) {
                    flag = true;
                    d.updateAttachDailies(dfList);
                    break;
                }
            }
            if(!flag){
                // 이전에 해당 DAY가 존재하지 않았다면
                weekly.addDailies(Daily.createDaily(day, dfList));
                //list를 만들어서 attachDaily넣어주고 이어준다.
            }

        }
        //DB에 저장
        weeklyRepository.save(weekly);
    }

    /**
     * 위클리 폼 저장하는 메소드
     * */
    public void saveAttachWeekly(MultipartFile file, Weekly weekly) throws Exception {
        //1.서버에 파일 저장
        String[] saveArr;
        if(file != null) {
            // 파일이 있을경우 서버에 저장후 DB 저장
            saveArr = saveAttach(file,"weekly");
        } else {
            // 파일이 없을경우 서버저장 생략, 기본이미지 경로 DB에 저장
            saveArr = new String[]{weekly.getNation()+".png", fileDir + "weekly/default_thumbnails/" + weekly.getNation() +".png"};
        }

        //2.AttachWeekly 엔티티 생성해서 DB에도 저장
        AttachWeekly attachWeekly = AttachWeekly.builder()
                .attachTitle(saveArr[0])
                .thumb(saveArr[1])
                .weekly(weekly)
                .build();
        attachRepository.save(attachWeekly);
    }


    // attachNo를 삭제하는 메소드
    public void deleteAttachDaily(List<Integer> deleteNos) {
        for(int no : deleteNos){
            attachRepository.deleteById(no);
        }

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
        String thumbPath = fileDir+"user/"+folder+"/"+newTitle;

        // 이미지 저장
        boolean a = ImageIO.write(resizeImageFile(file), extension,
                new File(thumbPath));

        String[] saveArr = {newTitle,thumbPath};

        return saveArr;
    }

    // 이미지 리사이징 - 고민이 더 필요할듯
    private BufferedImage resizeImageFile(MultipartFile mFile) throws IOException {


        File file = new File(System.getProperty("java.io.tmpdir") + mFile.getOriginalFilename());
        mFile.transferTo(file);
        // 회전
        //file = turnImage(file);

        BufferedImage readImage = turnImage(file);
        int originWidth = readImage.getWidth();
        int originHeight = readImage.getHeight();
        System.out.println("=====================");
        System.out.println("width : "+ originWidth+", height : "+ originHeight);
        int newWidth = originWidth, newHeight = originHeight;
        if(originWidth == originHeight) {
            return Scalr.resize(readImage, 900, 900);
        }
        else if(originWidth > originHeight) {
            // 가로가 길면 480x320
            return Scalr.resize(readImage, 1200, 900);
        }
        else if(originWidth < originHeight) {
            // 세로가 길면 240x320
            return Scalr.resize(readImage, 600, 900);
        }

        return readImage;
    }

    private BufferedImage turnImage(File imageFile) throws IOException {
        // 원본 파일의 Orientation 정보를 읽는다.
        int orientation = 1; // 회전정보, 1. 0도, 3. 180도, 6. 270도, 8. 90도 회전한 정보
        int width = 0; // 이미지의 가로폭
        int height = 0; // 이미지의 세로높이

        Metadata metadata; // 이미지 메타 데이터 객체
        Directory directory; // 이미지의 Exif 데이터를 읽기 위한 객체

        try {
            metadata = ImageMetadataReader.readMetadata(imageFile);
            directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if(directory != null){
                orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION); // 회전정보
            }

        }catch (Exception e) {
            orientation=1;
        }

        System.out.println("orientation : "+orientation);
        //imageFile
        BufferedImage srcImg = ImageIO.read(imageFile);
        // 회전 시킨다.
        switch (orientation) {
            case 6:
                srcImg = Scalr.rotate(srcImg, Scalr.Rotation.CW_90, null);
                break;
            case 1:

                break;
            case 3:
                srcImg = Scalr.rotate(srcImg, Scalr.Rotation.CW_180, null);
                break;
            case 8:
                srcImg = Scalr.rotate(srcImg, Scalr.Rotation.CW_270, null);
                break;

            default:
                orientation=1;
                break;
        }

        return srcImg;
    }


}
