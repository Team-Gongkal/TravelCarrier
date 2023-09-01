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
      //cropper비율 설정(1:1)
      cropper.options.aspectRatio = 1 / 1;
      cropper.options.autoCropArea = 0.5;
      $(".edit_img_saveBtn  button[type=button]").removeClass("bolbBackground");
      $(".edit_img_saveBtn  button[type=button]").addClass("bolbProfile");

      //[2]배경이미지의 변경일 경우--------------------
    } else if (changedInputId === "profile_bg_change") {
      //cropper비율 설정(4:1)
      cropper.options.autoCropArea = 1;
      cropper.options.aspectRatio = 3 / 1;
      $(".edit_img_saveBtn  button[type=button]").removeClass("bolbProfile");
      $(".edit_img_saveBtn  button[type=button]").addClass("bolbBackground");
    }
  });
  //cropper적용하기-버튼 클릭시
  var profileFormData = null;
  var backgroundFormData = null;

  $(document).on("click", ".bolbProfile", function () {
    console.log("###프로필 데이터 폼 생성");
    profileFormData = new FormData(); //데이터폼을 생성
    cropper.getCroppedCanvas().toBlob((blob) => {
      profileFormData.append("profileImg", blob /*, 'example.png' , 0.7*/);
      var url = URL.createObjectURL(blob);
      $(
        ".edit_modal_con .my_profile_img img, .choose_profile .upload_image img"
      ).attr("src", url);
    });
  });

  $(document).on("click", ".bolbBackground", function () {
    console.log("###배경 데이터 폼 생성");
    backgroundFormData = new FormData(); //데이터폼을 생성
    cropper.getCroppedCanvas().toBlob((blob) => {
      backgroundFormData.append(
        "backgroundImg",
        blob /*, 'example.png' , 0.7*/
      );
      var url = URL.createObjectURL(blob);
      $(".userProfile_bg img, .choose_bg .upload_image img").attr("src", url);
    });
  });
  // 모달창 닫기
  $(".edit_img_saveBtn").click(function () {
    $(".crop_img_modal").removeClass("show");
  });
  //2. 서버에 올릴 수 있도록 파일로 변환
  function saveCrop(url, formData) {
    console.log("---------------------");
    $.ajax({
      url: url,
      type: "POST",
      data: formData, //앞에서 생성한 formData
      processData: false, // data 파라미터 강제 string 변환 방지
      contentType: false, // application/x-www-form-urlencoded; 방지
      success: function () {
        alert("업로드 성공");
        switchImg();
      },
      error: function () {
        alert("업로드 에러");
      },
    });
  }

  $(".imgBtn button").on("click", function () {
      //버튼 저장중 처리
      var clickBtn = $(".imgBtn button");
      clickBtn.attr("disabled",true);
      clickBtn.toggleClass("btn_disable btn");
      clickBtn.html("저장중..");

    console.log("이미지 저장해");
    if (profileFormData !== null) {
      saveCrop("/TravelCarrier/member/profile", profileFormData);
    }
    if (backgroundFormData !== null) {
      saveCrop("/TravelCarrier/member/background", backgroundFormData);
    }

      clickBtn.attr("disabled",false);
      clickBtn.toggleClass("btn_disable btn");
      clickBtn.html("저장하기");

  });

  //ajax 성공시 화면 변경
  function switchImg(){
      if (profileFormData != null) {
         $(".my_profile_img.circle img").attr("src",URL.createObjectURL(profileFormData.get("profileImg")));
         $(".profile.circle").css("background-image", "url(" + URL.createObjectURL(profileFormData.get("profileImg")) + ")");

      }
      if (backgroundFormData != null) {
        $(".userProfile_bg img").attr("src",URL.createObjectURL(backgroundFormData.get("backgroundImg")));
      }
  }


});

