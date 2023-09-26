/*
$(document).ready(function () {
  // datepicker 설정 및 옵션 변경 - by윤아
  $.datepicker.setDefaults($.datepicker.regional["ko"]);
  $("#sdate").datepicker({
    // showOn: 'both',
    // buttonImage: "/", // 버튼 이미지
    // buttonImageOnly: true, // 버튼에 있는 이미지만 표시한다.
    changeMonth: true,
    changeYear: true,
    nextText: "다음달",
    prevText: "이전달",
    dayNames: [
      "일요일",
      "월요일",
      "화요일",
      "수요일",
      "목요일",
      "금요일",
      "토요일",
    ],
    dayNamesMin: ["일", "월", "바뀜", "수", "목", "금", "토"],
    monthNamesShort: [
      "1월",
      "2월",
      "3월",
      "4월",
      "5월",
      "6월",
      "7월",
      "8월",
      "9월",
      "10월",
      "11월",
      "12월",
    ],
    monthNames: [
      "1월",
      "2월",
      "3월",
      "4월",
      "5월",
      "6월",
      "7월",
      "8월",
      "9월",
      "10월",
      "11월",
      "12월",
    ],
    dateFormat: "yy-mm-dd",
    shoMonthAfterYear: true,
    showOtherMonths: true,
    yearRange: "1960:2023", //연도 범위
    todayHighlight: true, //오늘 날짜 표시
    maxDate: 0, //선택할 수 있는 최소 날짜로 (0: 오늘 이후 날짜 선택 불가하도록 함)
    onClose: function (selectedDate) {
      //시작일(startDate) datepicker가 닫힐때
      //종료일(endDate)의 선택할수있는 최소 날짜(minDate)를 선택한 시작일로 지정
      $("#edate").datepicker("option", "minDate", selectedDate);
      if($("#edate").val() != "") $("#periodValidation").text("");
    },
  });
  //end Date
  $("#edate").datepicker({
    // showOn: 'both',
    // buttonImage: "/", // 버튼 이미지
    // buttonImageOnly: true, // 버튼에 있는 이미지만 표시한다.
    changeMonth: true,
    changeYear: true,
    nextText: "다음달",
    prevText: "이전달",
    dayNames: [
      "일요일",
      "월요일",
      "화요일",
      "수요일",
      "목요일",
      "금요일",
      "토요일",
    ],
    dayNamesMin: ["일", "월", "바뀜", "수", "목", "금", "토"],
    monthNamesShort: [
      "1월",
      "2월",
      "3월",
      "4월",
      "5월",
      "6월",
      "7월",
      "8월",
      "9월",
      "10월",
      "11월",
      "12월",
    ],
    monthNames: [
      "1월",
      "2월",
      "3월",
      "4월",
      "5월",
      "6월",
      "7월",
      "8월",
      "9월",
      "10월",
      "11월",
      "12월",
    ],
    dateFormat: "yy-mm-dd",
    shoMonthAfterYear: true,
    showOtherMonths: TransformStreamDefaultController,
    yearRange: "1960:2023", //연도 범위
    todayHighlight: true, //오늘 날짜 표시
    maxDate: 0, //선택할 수 있는 최소 날짜로 (0: 오늘 이후 날짜 선택 불가하도록 함)
    onClose: function (selectedDate) {
      //시작일(startDate) datepicker가 닫힐때
      //종료일(endDate)의 선택할수있는 최소 날짜(minDate)를 선택한 시작일로 지정
      $("#sdate").datepicker("option", "maxDate", selectedDate);
      if($("#sdate").val() != "") $("#periodValidation").text("");
    },
  });
  //input태그 옆의 이미지 클릭시 datepicker열기 - by윤아
  $(".dateClick").on("click", function (e) {
    $(e.target).prev("input").focus();
    console.log(e.target);
  });



  // 동행인 선택 모달창 띄우기 - by윤아
  $("#plus_companion").on("click", function () {
    $(".companion_modal_bg").addClass("show");
  });
  $(".companion_modal > button").on("click", function () {
    $(".companion_modal_bg").removeClass("show");
  });

  // 선택된 동행인 우선 정렬 - by윤아
  const checkboxes = document.querySelectorAll(
    '.search_friends input[type="checkbox"]'
  );
  checkboxes.forEach((checkbox) => {
    checkbox.addEventListener("change", (e) => {
      const li = e.target.closest("li");
      if (e.target.checked) {
        document.querySelector(".checked_friends").appendChild(li);
      } else {
        document.querySelector(".search_friends").appendChild(li);
      }
    });
  });


});

// 사진첨부시 미리보기를 생성하는 메소드 by서현
$(document).on("change", "#thumbnail_change", function (event) {
  $(".thumbnail_img.circle").empty();
  var file = event.target.files[0];
  var img = $("<img>").attr("src", URL.createObjectURL(file));
  $(".thumbnail_img.circle").append(img);
});

// 이미지 삭제버튼 메소드 by서현
$(document).on("click", ".removeBtn", function (event) {
  $(".thumbnail_img.circle").empty();
});
// 선택된 동행인을 폼에 저장하는 메소드 by서현
$(document).on("click", ".goWithBtn", function (event) {
  const companions = $(".checked_friends li");
  const selCompanion = $(".sel_companion");

  //$(".sel_companion li:not(:last)").remove();
  $(".sel_companion:last li:not(#plus_companion)").remove();

  companions.each(function () {
    const fid = $(this).data("fid");
    const fname = $(this).data("fname");
    const profileImg = $(this).find(".profile_circle img").attr("src");

    const li = $("<li>")
      .attr({
        "data-fid": fid,
        "data-fname": fname,
        name: "gowiths",
        style: `background-image: url(${profileImg})`,
      })
      .addClass("circle");

    $(".sel_companion #plus_companion").before(li);
  });
});

$(".weekly_saveBtn").click(function (event) {

  // 제출전 유효성 검사, false면 제출 X
  if(!checkValidation()) return;

  // nation file sdate edate title text gowiths[] status
  var formData = new FormData();
  formData.append("file", $("#thumbnail_change")[0].files[0]);
  formData.append(
    "nation",
    $('select[name="nation"] option:selected').attr("value")
  );
  formData.append("sdate", $("#sdate").val());
  formData.append("edate", $("#edate").val());
  if ($("div.title input").val() === "") {
    formData.append("title", $('select[name="nation"] option:selected').text());
  } else {
    formData.append("title", $("div.title input").val());
  }
  formData.append("text", $("#addText").val());

  $("ul.sel_companion li:not(:last)").each(function () {
  console.log($(this).data("fid"));
    formData.append("gowiths[]", $(this).data("fid"));
  });

  formData.append("status", $("input[name='status']:checked").val());

  $.ajax({
    type: "POST",
    url: "/weeklyForm",
    data: formData,
    processData: false,
    contentType: false,
    success: function (data) {
      alert("성공");
      window.location.href = "/weekly/" + data;
    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert("실패");
    },
  });
});

// 제출전 유효성 최종검사
function checkValidation(){
    // 날짜가 없을때만 제출 불가!
    if( $("#sdate").val() == null ||  $("#edate").val() == null ||
     $("#sdate").val() == undefined ||  $("#edate").val() == undefined ||
     $("#sdate").val() == "" ||  $("#edate").val() == ""){
        alert("날짜는 필수입력항목 입니다.");
        return false;
    }
    return true;
}
// 글자수세기
$('#addText').keyup(function (e){
    var content = $(this).val();
    $('#countText').html("("+content.length+" / 100)");    //글자수 실시간 카운팅

    if (content.length > 100){
        alert("최대 100자까지 입력 가능합니다.");
        $(this).val(content.substring(0, 100));
        $('#countText').html("(100 / 100)");
    }
});
*/


// datepicker 설정 및 옵션 변경 - by윤아
$(document).ready(function () {
  $.datepicker.setDefaults($.datepicker.regional["ko"]);
  $("#sdate").datepicker({
    // showOn: 'both',
    // buttonImage: "/", // 버튼 이미지
    // buttonImageOnly: true, // 버튼에 있는 이미지만 표시한다.
    changeMonth: true,
    changeYear: true,
    nextText: "다음달",
    prevText: "이전달",
    dayNames: [
      "일요일",
      "월요일",
      "화요일",
      "수요일",
      "목요일",
      "금요일",
      "토요일",
    ],
    dayNamesMin: ["일", "월", "화", "수", "목", "금", "토"],
    monthNamesShort: [
      "1월",
      "2월",
      "3월",
      "4월",
      "5월",
      "6월",
      "7월",
      "8월",
      "9월",
      "10월",
      "11월",
      "12월",
    ],
    monthNames: [
      "1월",
      "2월",
      "3월",
      "4월",
      "5월",
      "6월",
      "7월",
      "8월",
      "9월",
      "10월",
      "11월",
      "12월",
    ],
    dateFormat: "yy-mm-dd",
    shoMonthAfterYear: true,
    showOtherMonths: true,
    yearRange: "1960:2023", //연도 범위
    todayHighlight: true, //오늘 날짜 표시
    maxDate: 0, //선택할 수 있는 최소 날짜로 (0: 오늘 이후 날짜 선택 불가하도록 함)
    onClose: function (selectedDate) {
      //시작일(startDate) datepicker가 닫힐때
      //종료일(endDate)의 선택할수있는 최소 날짜(minDate)를 선택한 시작일로 지정
      $("#edate").datepicker("option", "minDate", selectedDate);
      if ($("#edate").val() != "") $("#periodValidation").text("");
    },
  });
  //end Date
  $("#edate").datepicker({
    // showOn: 'both',
    // buttonImage: "/", // 버튼 이미지
    // buttonImageOnly: true, // 버튼에 있는 이미지만 표시한다.
    changeMonth: true,
    changeYear: true,
    nextText: "다음달",
    prevText: "이전달",
    dayNames: [
      "일요일",
      "월요일",
      "화요일",
      "수요일",
      "목요일",
      "금요일",
      "토요일",
    ],
    dayNamesMin: ["일", "월", "화", "수", "목", "금", "토"],
    monthNamesShort: [
      "1월",
      "2월",
      "3월",
      "4월",
      "5월",
      "6월",
      "7월",
      "8월",
      "9월",
      "10월",
      "11월",
      "12월",
    ],
    monthNames: [
      "1월",
      "2월",
      "3월",
      "4월",
      "5월",
      "6월",
      "7월",
      "8월",
      "9월",
      "10월",
      "11월",
      "12월",
    ],
    dateFormat: "yy-mm-dd",
    shoMonthAfterYear: true,
    showOtherMonths: TransformStreamDefaultController,
    yearRange: "1960:2023", //연도 범위
    todayHighlight: true, //오늘 날짜 표시
    maxDate: 0, //선택할 수 있는 최소 날짜로 (0: 오늘 이후 날짜 선택 불가하도록 함)
    onClose: function (selectedDate) {
      //시작일(startDate) datepicker가 닫힐때
      //종료일(endDate)의 선택할수있는 최소 날짜(minDate)를 선택한 시작일로 지정
      $("#sdate").datepicker("option", "maxDate", selectedDate);
      if ($("#sdate").val() != "") $("#periodValidation").text("");
    },
  });
  //날짜를 오늘로 기본설정하기 - by윤아
  //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, -1M:한달후, -1Y:일년후)
  //시작날짜 초기값을 오늘 날짜로 설정
  $('#sdate').datepicker('setDate', 'today');
  //끝나는 날짜 초기값을 내일로 설정
  $('#edate').datepicker('setDate', 'today');

  //input태그 옆의 이미지 클릭시 datepicker열기 - by윤아
  $(".dateClick").on("click", function (e) {
    $(e.target).prev("input").focus();
    console.log(e.target);
  });

  // 동행인 선택 모달창 활성화 - by윤아
  $("#plus_companion").on("click", function () {
    if (selfAuth == "GRANTED") $(".companion_modal_bg").addClass("show");
    else if (selfAuth == "DENIED")
      alertModal2("친구 게시글의 동행인은 수정 할 수 없습니다");
  });
  $(".companion_modal > button").on("click", function () {
    $(".companion_modal_bg").removeClass("show");
  });

  // 선택된 동행인 우선 정렬 - by윤아
  const checkboxes = document.querySelectorAll(
    '.search_friends input[type="checkbox"]'
  );
  checkboxes.forEach((checkbox) => {
    checkbox.addEventListener("change", (e) => {
      const li = e.target.closest("li");
      if (e.target.checked) {
        document.querySelector(".checked_friends").appendChild(li);
      } else {
        document.querySelector(".search_friends").appendChild(li);
      }
    });
  });
});

//동행인이 없을 시 동행인 추가 문구 안내 - by윤아
//만약 동행인 리스트의 값이 0일 경우 보여질 것과 안보여질 것 ㅋ클래스 변경
$('#plus_companion').on('click',function(){
if($('.checked_friends li').length == 0 && $('.search_friends li').length == 0){
  $('.friendsList').hide();
  $('.if_no_friends').show();
}else{
  $('.if_no_friends').hide();
  $('.friendsList').show();
}
})

// 사진첨부시 미리보기를 생성하는 메소드 by서현
$(document).on("change", "#thumbnail_change", function (event) {
  $(".thumbnail_img.circle").empty();
  var file = event.target.files[0];
  var img = $("<img>").attr("src", URL.createObjectURL(file));
  $(".thumbnail_img.circle").append(img);
});

// 이미지 삭제버튼 메소드 by서현
$(document).on("click", ".removeBtn", function (event) {
  $(".thumbnail_img.circle").empty();
  var img = $("<img>").attr(
    "src",
    "/image/default/weekly_default_thumbnail.png"
  );
  $(".thumbnail_img.circle").append(img);
});

// 선택된 동행인을 폼에 저장하는 메소드 by서현
$(document).on("click", ".goWithBtn", function (event) {
  const companions = $(".checked_friends li");
  const selCompanion = $(".sel_companion");

  //$(".sel_companion li:not(:last)").remove();
  $(".sel_companion:last li:not(#plus_companion)").remove();

  companions.each(function () {
    const fid = $(this).data("fid");
    const fname = $(this).data("fname");
    const profileImg = $(this).find(".profile_circle img").attr("src");

    const li = $("<li>")
      .attr({
        "data-fid": fid,
        "data-fname": fname,
        name: "gowiths",
        style: `background-image: url(${profileImg})`,
      })
      .addClass("circle");

    $(".sel_companion #plus_companion").before(li);
  });
});

$(".weekly_saveBtn").click(function (event) {
  event.preventDefault();

  // 제출전 유효성 검사, false면 제출 X
  if (!checkValidation()) return;

  // nation file sdate edate title text gowiths[] status
  var formData = new FormData();
  if (
    $(".thumbnail_img.circle img").attr("src") !=
    "/image/default/weekly_default_thumbnail.png"
  ) {
    formData.append("file", $("#thumbnail_change")[0].files[0]);
  }

  formData.append("nation",$('select[name="nation"] option:selected').attr("value"));

  formData.append("sdate", $("#sdate").val());
  formData.append("edate", $("#edate").val());
  if ($("div.title input").val() === "") {
    formData.append("title", $('select[name="nation"] option:selected').text());
  } else {
    formData.append("title", $("div.title input").val());
  }
  formData.append("text", $("#addText").val());

  $("ul.sel_companion li:not(:last)").each(function () {
    console.log($(this).data("fid"));
    formData.append("gowiths[]", $(this).data("fid"));
  });

  formData.append("status", $("input[name='status']:checked").val());

  console.log(formData.get("nation"));

  //버튼 저장중 처리
  var clickBtn = $(".weekly_saveBtn");
  clickBtn.attr("disabled",true);
  clickBtn.toggleClass("btn_disable btn");
  clickBtn.html("저장중..");

  $.ajax({
    type: "POST",
    url: "/weeklyForm",
    data: formData,
    processData: false,
    contentType: false,
    success: function (data) {
      window.location.href = "/weekly/" + data;
    },
    error: function (jqXHR, textStatus, errorThrown) {
      alertModal2("저장에 실패했습니다."+jqXHR+textStatus+errorThrown);
    },
  });

  // 추가할곳
});

// 제출전 유효성 최종검사
function checkValidation() {
  // 날짜가 없을때만 제출 불가!
  if (
    $("#sdate").val() == null ||
    $("#edate").val() == null ||
    $("#sdate").val() == undefined ||
    $("#edate").val() == undefined ||
    $("#sdate").val() == "" ||
    $("#edate").val() == ""
  ) {
    alertModal2("날짜","는 필수입력항목 입니다.");
    return false;
  }
  return true;
}
// 텍스트의 글자수세기
$("#addText").keyup(function (e) {
  var content = $(this).val();
  $(".countText:eq(1)").html("(" + content.length + " / 100)"); //글자수 실시간 카운팅

  if (content.length > 100) {
    //alert("최대 100자까지 입력 가능합니다.");
    $(this).val(content.substring(0, 100));
    $(".countText:eq(1)").html("(100 / 100)");
  }
});
// 타이틀의 글자수세기
$("#addTitle").keyup(function (e) {
  var content = $(this).val();
  $(".countText:eq(0)").html("(" + content.length + " / 30)"); //글자수 실시간 카운팅

  if (content.length > 30) {
    //alert("최대 100자까지 입력 가능합니다.");
    $(this).val(content.substring(0, 30));
    $(".countText:eq(0)").html("(30 / 30)");
  }
});

//썸네일 클릭시 파일인풋 활성화 - by윤아
  //(1)프로필 이미지 변경
$(document).on('click', '.thumbnail_img', function(){
$('#thumbnail_change').click();
})