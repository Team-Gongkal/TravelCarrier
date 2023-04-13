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
    },
  });
  //Date Click
  $(".dateClick").on("click", function (e) {
    $(e.target).prev("input").focus();
    console.log(e.target);
  });
  //edate Click]

  // 동행인 모달창--------------------------------------
  $("#plus_companion").on("click", function () {
    $(".companion_modal_bg").addClass("show");
  });
  $(".companion_modal > button").on("click", function () {
    $(".companion_modal_bg").removeClass("show");
  });

  // 선택된 동행인 우선 정렬
  const checkboxes = document.querySelectorAll(
    '.serch_friends input[type="checkbox"]'
  );
  checkboxes.forEach((checkbox) => {
    checkbox.addEventListener("change", (e) => {
      const li = e.target.closest("li");
      if (e.target.checked) {
        document.querySelector(".checked_friends").appendChild(li);
      } else {
        document.querySelector(".serch_friends").appendChild(li);
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

  $(".sel_companion li:not(:last)").remove();

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

$("#weeklyForm").submit(function (event) {
  event.preventDefault();
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
    formData.append("gowiths[]", $(this).data("fid"));
    console.log($(this).data("fid"));
  });
  console.log($("input[name='status']:checked").val());
  formData.append("status", $("input[name='status']:checked").val());

  $.ajax({
    type: "POST",
    url: "/TravelCarrier/weeklyForm",
    data: formData,
    processData: false,
    contentType: false,
    success: function (data) {
      alert("성공");
      window.location.href = "/TravelCarrier/weekly/" + data;
    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert("실패");
    },
  });
});
