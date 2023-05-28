$(document).ready(function () {
  window.onresize = function () {
    document.location.reload();
  };
  var input1 = $(".login form div input[type=text]"),
    input2 = $(".login form div input[type=password]");

  $(input1).on("propertychange change paste input", function () {
    x = input1.val().length;
    if (x !== 0) {
      $(input1).prev("label").addClass("on");
    } else {
      $(input1).prev("label").removeClass("on");
    }
  });

  $(input2).on("propertychange change paste input", function () {
    x = input2.val().length;
    if (x !== 0) {
      $(input2).prev("label").addClass("on");
    } else {
      $(input2).prev("label").removeClass("on");
    }
  });

  /*  //빈칸있을 경우 경고창띄우기
  $("button[type=submit]").click(function chkValue() {
    // 필수입력사항에 누락이 있으면 진행금지
    if (!validCheck()) return;
    form.submit();
  });*/
});

function validCheck() {
  // 공통입력폼내의 모든 입력오브젝트
  var inputObjs = $("form input");
  // 미입력여부(경우에 따라 사용)
  var bEmpty = true;
  var focus;

  // 각 오브젝트에 대해 입력체크
  inputObjs.each(function (index) {
    if ($(this).val() == "") {
      focus = $(this);
      bEmpty = false;

      alert($(this).attr("title") + "를 입력해주세요.");
      focus.focus();
    }
  });
  return bEmpty;
}

/*
  $("#password").on("keyup", function(e){
    if(e.key == "Enter") fnSubmit();
  });
*/

/*
  $("#loginBtn").on("click", function(e){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    e.preventDefault();
    $.ajax({
      type: "POST",
      url: "/TravelCarrier/login/action",
      data : {
                username : $("#username").val(),
                password : $("#password").val()
      			},
      beforeSend : function(xhr){
                      		xhr.setRequestHeader(header, token);
                      	},
      success: function (response) {
        sessionStorage.setItem('username', $("#username").val());
        alert("로그인 성공 : "+response);
        notification();
        //location.href="/TravelCarrier/";
      },
      error: function (jqXHR, textStatus, errorThrown) {
        alert("로그인 실패 : "+textStatus);
      }
    });
  });
*/

//애니메이션 실행 -by윤아
$(window).on("load", function () {
  $(".start_title").addClass("on");

  $(".start_title button").click(function () {
    $("#starting_page").addClass("hide");
    $(".example_img_wrap").addClass("show");
  });
});

// 로그인 모달창 활성화 및 비활성화 -by윤아
$(".quick_login").click(function () {
  $("#login_wrap").addClass("show");
});
$("#login_wrap > div > button.close").click(function () {
  $("#login_wrap").removeClass("show");
});

// 원페이지 스크롤 구현 - by 윤아
let num = 0; //첫페이지에서 스크롤되는 횟수(첫페이지 동영상 크기 변환) -스크롤을 할 때마다 증가하며 그에 따라서 비디오의 크기가 변한다
let idx = 0; //첫페이지에서 다음페이지로 넘어갈 때부터 카운트 되는 스크롤 횟수(페이지 이동을 하기위한 변수)- num이 일정 숫자에 달하면 (= 비디오가 화면을 꽉 채우게 되는 수치) 증가가 멈추며, idx가 증가하게 된다.
let delta; //스크롤의 방향을 판별하기 위한 변수로 할당은 스크롤이벤트 안에서 함
const mainVideo = document.querySelector("#example_main_video video");
const gally = document.querySelector(".example_img_wrap");
const lastPage = document.querySelectorAll("#start section").length;

//mousewheel DOMMouseScroll 스크롤 이벤트
$(window).on("mousewheel DOMMouseScroll", function (e) {
  delta = e.originalEvent.wheelDelta || e.originalEvent.delta * -1;

  if (delta < 0) {
    //스크롤을 아래로 내렸을 때
    if (!(num == 13)) {
      num++;
      console.log(num);
    }
    if (num <= 12) {
      mainVideo.style.clipPath = `inset(${23 - num * 2}% ${
        35 - num * 3
      }% round 20px)`;
      gally.style = `grid-template-columns : 1fr 1fr ${num}fr 1fr 1fr`;
      gally.style.width = `${100 + num * 5}%`;
      // 비주얼 컨텐츠 영역의 넓이가 변하도록 스타일 조정(커짐)
    } else if (num == 13 && idx < lastPage) {
      idx++;
      console.log("최종" + idx);
    }
  } else {
    // if (idx > 0) {
    //   idx--;
    //   console.log("-" + idx);
    // }
    if (idx == 1 && num > 0) {
      num--;
      mainVideo.style.clipPath = `inset(${23 - num}% ${35 - num}% round 20px)`;
      gally.style = `grid-template-columns : 1fr 1fr ${num}fr 1fr 1fr`;
      gally.style.width = `${100 + num * 5}%`;
      console.log(num);
    }
  }
  $("html,body")
    .stop()
    .animate(
      {
        scrollTop: $(window).height() * (idx - 1),
      },
      1000
    );
});
