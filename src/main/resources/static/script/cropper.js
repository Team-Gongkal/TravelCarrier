$(document).ready(function () {
  var cropper = null;
  var profileImgInput = $("#profile_img_change, #profile_bg_change");

  //공통 실행====================================
  $(profileImgInput).on("change", function (event) {
    // 파일 선택이 완료되었을 때 실행할 코드 작성
    alert("파일이 선택되었습니다.");
    //이미지 편집 모달창 띄우기
    $(".crop_img_modal").addClass("show");

    //이미지 비우고 append하기
    $(".crop_img")
      .empty()
      .append(
        '<img id="cropImg" src="" alt="편집할 이미지" class="preview_image">'
      );

    //선택한 이미지 파일명 가져오기-------------
    var selectedFile = event.target.files[0];
    //내가 선택한 파일을 변수로 담아줌/다중파일 업로드가 가능한데, 우리는 1개만 선택하니 [0]번으로 고정함
    $(".crop_img > img").attr("src", URL.createObjectURL(selectedFile));
    //이미지 url생성해주기, 보안상 url을 가져오는 것이 안돼, 임의로 만들어줌
    $(".crop_img_wrap > .filePath > p > span").text(selectedFile.name);

    //cropper.js 사용하기
    const image = document.getElementById("cropImg");

    cropper = new Cropper(image, {
      //aspectRatio: 4 / 1, // 자르기 비율 설정(프로필과 배경 따로 설정해야함)
      //autoCropArea: 0.5, 잘라질 박스의 크기지정 0.1~1
      width: 100 + "%",
      height: 90 + "%",
      // minWidth: 256,
      // minHeight: 256,
      // maxWidth: 4096,
      // maxHeight: 4096,
      fillColor: "#ffffff00", //투명도 설정 가능
      imageSmoothingEnabled: false,
      imageSmoothingQuality: "high",
      zoomOnWheel: true,
      viewMode: 0, //(0제한없음~3자르기상자와 캔버스의 크기)
      dragMode: "move",
      cropBoxResizable: true,
      ratio: 1,
      /*// 자르기 영역이 변경될 때마다 실행되는 콜백 함수
      crop(event) {
        console.log(event.detail.x);
        console.log(event.detail.y);
        console.log(event.detail.width);
        console.log(event.detail.height);
        console.log(event.detail.rotate);
        console.log(event.detail.scaleX);
        console.log(event.detail.scaleY);
      },*/
    });
    //[1]프로필 변경일 경우---------------------
    var changedInputId = event.target.id; // 변경된 입력 요소의 ID를 가져옴
    if (changedInputId === "profile_img_change") {
      //   //프로필 이미지 객체에 생성
      //   var profileObj = new ProfileChange();
      //cropper비율 설정(1:1)
      cropper.options.aspectRatio = 1 / 1;
      cropper.options.autoCropArea = 0.5;

      //[2]배경이미지의 변경일 경우--------------------
    } else if (changedInputId === "profile_bg_change") {
      //   //배경 이미지 객체에 생성
      //   var backgroundObj = new ProfileChange();
      //cropper비율 설정(4:1)
      cropper.options.autoCropArea = 1;
      cropper.options.aspectRatio = 4 / 1;
    }
  });
  //2. 서버에 올릴 수 있도록 파일로 변환
  function saveCrop() {
    cropper.getCroppedCanvas().toBlob(
      (blob) => {
        //HTMLCanvasElement를 return 받아서 blob파일로 변환해준다
        const formData = new FormData(); //데이터폼을 생성

        formData.append("profileImg", blob /*, 'example.png' , 0.7*/);
        //새로운 formData를 생성해서 앞에서 변경해준 blob파일을 삽입한다.(이름 지정 가능, 맨뒤 매개변수는 화질 설정)
        console.log(formData);
        for (const [key, value] of formData.entries()) {
          console.log(key, value);
        }
        // jQuery.ajax이용해서 서버에 업로드
        $.ajax({
          url: "/TravelCarrier/member/profile",
          type: "POST",
          data: formData, //앞에서 생성한 formData
          processData: false, // data 파라미터 강제 string 변환 방지
          contentType: false, // application/x-www-form-urlencoded; 방지
          success: function () {
            console.log("업로드 성공");
          },
          error: function () {
            console.log("업로드 에러");
          },
        });
      } /*, 'image/png' */
    ); //서버에 저장 형식 사용 가능
  }

  //cropper적용하기-버튼 클릭시
  $(".edit_img_saveBtn  button").on("click", function () {
    if ($(this).attr("type") === "button") {
      saveCrop();
      console.log("크롭실행");
    }
    //+모달창 닫기
    $(".crop_img_modal").removeClass("show");
  });
});
