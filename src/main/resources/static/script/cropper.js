$("#profile_img_change").on("change", function (event) {
  // 파일 선택이 완료되었을 때 실행할 코드 작성
  console.log("파일이 선택되었습니다.");

  //이미지 비우고 append하기
  $(".crop_img")
    .empty()
    .append(
      '<img id="cropImg" src="" alt="편집할 이미지" class="preview_image">'
    );

  //[1]선택한 이미지 가져오기
  //내가 선택한 파일을 변수로 담아줌/다중파일 업로드가 가능한데, 우리는 1개만 선택하니 [0]번으로 고정함
  var selectedFile = event.target.files[0];
  //이미지 url생성해주기, 보안상 url을 가져오는 것이 안돼, 임의로 만들어줌
  $(".crop_img > img").attr("src", URL.createObjectURL(selectedFile));

  //[2] cropper.js 플러그인사용
  const image = document.getElementById("cropImg");
  // 1.옵션설정
  const cropper = new Cropper(image, {
    aspectRatio: 4 / 1, // 자르기 비율 설정
    crop(event) {
      // 자르기 영역이 변경될 때마다 실행되는 콜백 함수
      console.log(event.detail.x);
      console.log(event.detail.y);
      console.log(event.detail.width);
      console.log(event.detail.height);
      console.log(event.detail.rotate);
      console.log(event.detail.scaleX);
      console.log(event.detail.scaleY);
    },
  });
  // 기본 형식
  cropper.getCroppedCanvas();
  cropper.getCroppedCanvas({
    width: 100 + "%",
    height: 100 + "%",
    // minWidth: 256,
    // minHeight: 256,
    // maxWidth: 4096,
    // maxHeight: 4096,
    fillColor: "#fff", //투명도 설정 가능
    imageSmoothingEnabled: false,
    imageSmoothingQuality: "high",
    autoCropArea: 1, //잘라질 박스의 크기지정 0.1~1
    zoomOnWheel: true,
    viewMode: 1,
    dragMode: "move",
    cropBoxResizable: false,
  });

  //2. 서버에 올릴 수 있도록 파일로 변환

  function saveCrop() {
    cropper.getCroppedCanvas().toBlob(
      (blob) => {
        //HTMLCanvasElement를 return 받아서 blob파일로 변환해준다
        const formData = new FormData(); //데이터폼을 생성

        formData.append("croppedImage", blob /*, 'example.png' , 0.7*/);
        //새로운 formData를 생성해서 앞에서 변경해준 blob파일을 삽입한다.(이름 지정 가능, 맨뒤 매개변수는 화질 설정)
        console.log(formData);
        for (const [key, value] of formData.entries()) {
          console.log(key, value);
        }
        // jQuery.ajax이용해서 서버에 업로드
        $.ajax("https://codingapple1.github.io/hello.txt", {
          method: "POST",
          data: formData, //앞에서 생성한 formData
          processData: false, // data 파라미터 강제 string 변환 방지
          contentType: false, // application/x-www-form-urlencoded; 방지
          success() {
            console.log("Upload success");
          },
          error() {
            console.log("Upload error");
          },
        });
      } /*, 'image/png' */
    ); //서버에 저장 형식 사용 가능
  }
  $(".edit_img_saveBtn > .btn").on("click", function () {
    saveCrop();
  });
  //이미지 파일명 가져오기
  $(".crop_img_wrap > .filePath > p > span").text(selectedFile.name);
  $(".crop_img_modal").addClass("show");
});
