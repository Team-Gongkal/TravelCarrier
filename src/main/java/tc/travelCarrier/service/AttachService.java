package tc.travelCarrier.service;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

import lombok.RequiredArgsConstructor;
import tc.travelCarrier.domain.*;
import tc.travelCarrier.dto.DailyForm;
import tc.travelCarrier.dto.WeeklyForm;
import tc.travelCarrier.exeption.ConvertImgException;
import tc.travelCarrier.exeption.FileNotDeleteException;
import tc.travelCarrier.exeption.FileNotFoundException;
import tc.travelCarrier.repository.AttachRepository;
import tc.travelCarrier.repository.WeeklyRepository;




@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class AttachService {
    @Value("${file.dir}")
    private String tmpFileDir;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;

    private final AttachRepository attachRepository;
    private final WeeklyRepository weeklyRepository;


    /**
     * id에 해당하는 데일리 첨부파일을 찾는 메소드
     * @param attachNo
     * @return AttachDaily
     */
    public AttachDaily findAttachDaily(int attachNo){
        return attachRepository.findAttachDaily(attachNo);
    }

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
                //String[] saveArr = saveAttach(form.getFile(), "daily"); //{이름, 첨부파일경로}
                String[] saveArr = upload(form.getFile(),"daily");
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
            //saveArr = saveAttach(file,"weekly");
            //S3:bucket에 저장하기
            //saveArr = saveAttachS3(file,"weekly");
            saveArr = upload(file,"weekly");
        } else {
            // 파일이 없을경우 서버저장 생략, 기본이미지 경로 DB에 저장
            //saveArr = new String[]{weekly.getNation()+".png", fileDir + "weekly/default_thumbnails/" + weekly.getNation() +".png"};
            //saveArr = new String[]{"weekly_default_thumbnail.png", "https://"+bucket+"/"+"weekly"+"/"+uuid+"."+extension;};
            saveArr = new String[]{"weekly_default_thumbnail.png", "static/image/default/weekly_default_thumbnail.png"};
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
            deleteServerFile(attachRepository.findAttachDaily(no).getFullThumbPath()); //서버에서 삭제
            attachRepository.deleteById(no);
        }

    }

    // 위클리 썸네일을 수정
    //FIle객체면 저장후업데이트,
    // null이면 thumbStatus 확인해서 ORIGIN이면 냅두고 DELETE면 국가사진으로 변경
    public void saveUpdateAttachWeekly(WeeklyForm form, Weekly weekly) throws Exception {
        System.out.println("수정파일 "+form.getFile());
        //1.서버에 파일 저장
        String[] saveArr;
        if(form.getFile() != null){
            //saveArr = saveAttach(form.getFile(),"weekly");
            saveArr = upload(form.getFile(),"weekly");
        } else if(form.getThumbStatus().equals("DELETE")){
            //saveArr = new String[]{weekly.getNation()+".png", fileDir + "weekly/default_thumbnails/" + weekly.getNation() +".png"};
            saveArr = new String[]{"weekly_default_thumbnail.png", "static/image/default/weekly_default_thumbnail.png"};
        } else{
            return;
        }

        //이전 파일데이터 서버에서 삭제
        deleteServerFile(weekly.getAttachWeekly().getFullThumbPath());

        //2. 바뀐 경로로 저장
        weekly.getAttachWeekly().setAttachTitle(saveArr[0]);
        weekly.getAttachWeekly().setThumbPath(saveArr[1]);
    }


    /**
     * 프로필사진 저장하는 메소드
     * */
    public void saveAttachUser(MultipartFile file, User user) throws Exception {
        //1.서버에 파일 저장
        String[] saveArr;
        if(file != null) {
            // 파일이 있을경우 서버에 저장후 DB 저장
            //saveArr = saveAttach(file,"profile");
            saveArr = upload(file,"profile");
        } else throw new FileNotFoundException();

        //2.AttachUser 엔티티 생성해서 DB에도 저장
        AttachUser attachUser = AttachUser.builder()
                                    .user(user)
                                    .attachTitle(saveArr[0])
                                    .thumb(saveArr[1])
                                    .build();

        AttachUser originAttachUser = attachRepository.findAttachUser(attachUser, user); //이전 프사가 있으면 true를 반환함
        //이전에 프사가 있었으면 서버에서 이전 프로필사진 파일을 삭제
        if(originAttachUser != null){
            // originAttachUser의 경로에 있는 서버에 저장된 파일을 수정한다
            deleteServerFile(originAttachUser.getFullThumbPath());
            originAttachUser.editAttachUser(attachUser); //파일 경로 수정
        }else{ //이전에 프사가 없었으면 새로 저장하기만 한다.
            attachRepository.saveAttachUser(attachUser);
        }

    }


    /**
     * 배경사진 저장하는 메소드
     * */
    public void saveAttachUserBackground(MultipartFile file, User user) throws Exception {
        //1.서버에 파일 저장
        String[] saveArr;
        if(file != null) {
            //saveArr = saveAttach(file,"background");
            saveArr = upload(file,"background");
        }
        else throw new FileNotFoundException();

        //2.AttachUserBackground 엔티티 생성해서 DB에도 저장
        AttachUserBackground attachUserBackground = AttachUserBackground.builder()
                .user(user)
                .title(saveArr[0])
                .path(saveArr[1])
                .build();
        AttachUserBackground origin = attachRepository.findBackground(attachUserBackground, user);
        if(origin!=null){
            deleteServerFile(origin.getFullThumbPath());
            origin.editAttachUserBackground(attachUserBackground);
        }else {
            attachRepository.saveAttachUserBackground(attachUserBackground);
        }
    }


    // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    public String[] upload(MultipartFile multipartFile, String dirName) throws IOException {
//        File uploadFile = convert(multipartFile)
//                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        BufferedImage resizedFile = resizeImageFile(multipartFile);
        File uploadFile = convert(resizedFile,multipartFile.getOriginalFilename());
        return upload(uploadFile, dirName);
    }

    private String[] upload(File uploadFile, String dirName) {
        //폴더명/파읾명 커스텀
        String uuid = UUID.randomUUID().toString();
        String extension = uploadFile.getName().substring(uploadFile.getName().lastIndexOf(".")+1);
        if(extension.equals("blob")) extension = "png";
        String fileName = dirName + "/" + uuid + "." + extension;

        //S3에 업로드
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        return new String[]{uuid+"."+extension,uploadImageUrl};      // 업로드된 파일의 S3 URL 주소 반환
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)	// PublicRead 권한으로 업로드 됨
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) log.info(targetFile.getName()+" 파일이 삭제되었습니다.");
        else log.info(targetFile.getName()+" 파일이 삭제되지 못했습니다.");
    }

    /**
     * Multipart를 File객체로 변환
     */
    private Optional<File> convert(MultipartFile file) throws  IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
    /**
     * BufferedImg를 File객체로 변환
     */
    private File convert(BufferedImage file, String originalName) throws  IOException {
        try {
            // 저장할 파일 경로 및 이름 지정
            File outputFile = new File(originalName);
            // BufferedImage를 파일로 저장
            String extension = originalName.substring(originalName.lastIndexOf(".")+1);
            if(extension.equals("blob")) extension = "png";
            ImageIO.write(file, extension, outputFile);

            return outputFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Cannot convert BufferedImage to File");
    }


    // 이미지 리사이징
    private BufferedImage resizeImageFile(MultipartFile mFile) throws IOException {
        File file = new File(tmpFileDir + mFile.getOriginalFilename());
        mFile.transferTo(file);
        // 회전부터
        BufferedImage readImage = turnImage(file);
        removeNewFile(file); //임시파일 삭제

        int originWidth = readImage.getWidth();
        int originHeight = readImage.getHeight();
        if(originHeight > 900){
            double aspectRatio = (double) originHeight / 900;
            int newWidth = (int) Math.round(originWidth / aspectRatio);
            int newHeight = (int) Math.round(originHeight / aspectRatio);
            return Scalr.resize(readImage, newWidth, newHeight);
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
        //imageFile
        BufferedImage srcImg = ImageIO.read(imageFile);
        // 회전 시킨다.
        switch (orientation) {
            case 6:
                srcImg = Scalr.rotate(srcImg, Scalr.Rotation.CW_90, (BufferedImageOp[])null);
                break;
            case 1:

                break;
            case 3:
                srcImg = Scalr.rotate(srcImg, Scalr.Rotation.CW_180, (BufferedImageOp[])null);
                break;
            case 8:
                srcImg = Scalr.rotate(srcImg, Scalr.Rotation.CW_270, (BufferedImageOp[])null);
                break;

            default:
                orientation=1;
                break;
        }

        return srcImg;
    }



/**
     * 첨부파일을 서버에 저장하는 메소드(로컬로 돌릴때 사용) : S3로 변경하였으므로 더이상 사용하지 않음
     * */

/*
    @Deprecated
    public String[] saveAttach(MultipartFile file, String folder) throws Exception {
        if(file.isEmpty()){
            throw new FileNotFoundException("File " + folder + "");
        }

        // 1. 새로운 파일이름 생성
        String uuid = UUID.randomUUID().toString();
        // 2. 원래 파일이름으로부터 확장자 분리
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        if(extension.equals("blob")) extension = "png";
        // 3. UUID+확장자명으로 새 파일제목 완성
        String newTitle = uuid+"."+extension;

        // 썸네일로 변환한 파일의 저장경로
        String thumbPath = fileDir+"user/"+folder+"/"+newTitle;

        // 이미지 저장
        System.out.println("thumbPath : "+thumbPath);
        boolean a = ImageIO.write(resizeImageFile(file), extension,
                new File(thumbPath));

        String[] saveArr = {newTitle,thumbPath};

        return saveArr;
    }
*/


    //파일을 삭제하는 메소드
    @Deprecated
    public void deleteServerFile(String filePath){
        File file = new File(filePath);
        if(file.exists() && file.isFile()){
            file.delete();
            return;
        }
        throw new FileNotDeleteException("deleteServerFile", filePath+" 경로의 파일을 삭제할수없음");
    }



}
