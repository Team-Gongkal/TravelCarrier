//화면 로드되면 바로 click Day1 해주기
var liIndex = "";
//formData day정보와 함께 배열로 저장한다. [ {DAY1,{file,title, text,thumb}},{DAY2,formDataArr[1]},{DAY3,formDataArr[2]} ]
//선택한 tap에 해당하는 formDataArr을 저장 [{file,title, text,thumb},{file,title, text,thumb},{file,title, text,thumb}]
var selectArr = [];
var dataArr = [];

// 맨처음 dataArr 배열 초기화 (이전 데이터 존재하면 그걸로 초기화 해줘야함)
function setFirst(dailies) {
  var result = dailies.reduce(function (acc, curr) {
    if (!acc[curr.dailyDate]) {
      acc[curr.dailyDate] = [];
    }
    var daily = {
      file: curr.attachThumb,
      title: curr.attachDailyTitle,
      text: curr.attachDailyText,
      thumb: curr.thumb ? 1 : 0,
      attachNo: curr.attachNo,
      dupdate: "notChange",
    };
    acc[curr.dailyDate].push(daily);
    return acc;
  }, {});

  dataArr = Object.keys(result).map(function (key) {
    return {
      day: [key],
      data: result[key].map(function (item) {
        var formData = new FormData();
        for (var key in item) {
          formData.append(key, item[key]);
        }
        return formData;
      }),
    };
  });
}

$(document).ready(function () {
  setFirst(dailies);
  getCurrentDataArr();
  drawThumbs();
  //첫번째 탭 자동클릭
  $("ul.days_tabSlide li:first").click();
});

// 슬라이드 셋팅
$(document).ready(function () {
  console.log("우무티티티");
  console.log(dataArr);
   var newUl = $("<ul>").addClass("diary_list");
  for (var i = 0; i < dataArr.length; i++) {
    for (var j = 0; j < dataArr[i].data.length; j++) {
      var newLi = $("<li>")
        //.attr("id", dataArr[i].data[j].get("attachNo"))
        .attr("data-text", dataArr[i].data[j].get("text"))
        .attr("data-title", dataArr[i].data[j].get("title"))
        .addClass("d_slide square");
      var newImg = $("<img>").attr({
        src: dataArr[i].data[j].get("file"),
        "data-attachNo" : dataArr[i].data[j].get("attachNo"),
        alt: "사진표시할수없음",
      });
      newLi.append(newImg);
      newLi.append(`<div class="reply_icon">
                              <img src="/image/daily/icon/message.png" alt="댓글아이콘이미지">
                            </div>`);
      newUl.append(newLi);
    }

    $(".diary_slides").append(newUl);
  }
});

// function : 사진첨부시 동작 (by.서현)
$(document).on("change", ".attach", function (event) {
  setDataArr(event);
  getCurrentDataArr();
  drawThumbs();
  //처음 로드시엔 맨 처음요소 선택하게 하기
  $("ul.Dform_imglist li:first img").click();
});
// function : 사진첨부시 dataArr 생성 (by.서현)
function setDataArr(event) {
  var formDataArr = selectArr;
  for (var file of event.target.files) {
    var formData = new FormData();
    formData.append("file", file);
    formData.append("title", "");
    formData.append("text", "");
    formData.append("thumb", 0);
    formData.append("attachNo", -1);
    formData.append("dupdate", "notChange");
    formDataArr.push(formData);
  }

  var days = [$(".days_tabSlide .on").text()];
  var obj = dataArr.find(function (item) {
    return item.day[0] === days[0];
  });
  if (obj) {
    obj.data = formDataArr;
  } else {
    console.log("이전에 데이터가 없네요");
    var obj = { day: days, data: formDataArr };
    dataArr.push(obj);
  }
}

// 오른쪽 폼(제목,메모,경로,대표) 비우기
function removeRightForm() {
  $('li[class="clickImg"]').removeAttr("class");
  $('div.daily_title input[type="text"]').val("");
  $("div.daily_text textarea").val("");
  $('input[type="radio"]').prop("checked", false);
  $("div.filePath p").text("");
}
// 사진 클릭이벤트
$(document).on("click", "ul.Dform_imglist li img", function (event) {
  removeRightForm();
  //만약 selectArr이 0, 즉 defailt가 떠있으면 실행 X
  if (selectArr.length !== 0) {
    $(this).closest("li").attr("class", "clickImg");
    liIndex = $("ul.Dform_imglist li").index($("li.clickImg"));
    $('div.daily_title input[type="text"]').val(
      selectArr[liIndex].get("title")
    );
    $("div.daily_text textarea").val(selectArr[liIndex].get("text"));
    if (selectArr[liIndex].get("file") instanceof File) {
      $("div.daily_files p").text(selectArr[liIndex].get("file").name);
    } else {
      const filePath = selectArr[liIndex].get("file");
      const fileName = filePath.split("/").pop();
      const dailyFileName = fileName.substring("daily/".length);
      $("div.daily_files p").text(dailyFileName);
    }

    if (selectArr[liIndex].get("thumb") == 0) {
      $('input[type="radio"]').prop("checked", false);
    } else if (selectArr[liIndex].get("thumb") == 1) {
      $('input[type="radio"]').prop("checked", true);
    }
  }
});

//제목 수정시 바로 배열에 저장
$(document).on("input", "div.daily_title input", function (event) {
  selectArr[liIndex].set("title", event.target.value);
});
//메모 수정시 바로 배열에 저장
$(document).on("input", "div.daily_text textarea", function (event) {
  selectArr[liIndex].set("text", event.target.value);
});
//대표이미지 설정
$(document).on("change", 'input[type="radio"]', function () {
  if ($(this).is(":checked")) {
    //라디오버튼 체크되면 이전에 되어있던 id=main 없애고 main이었던 배열도 thumb=0으로 바꿔야함
    var mainIndex = $("ul.Dform_imglist li").index($("li#main"));
    if (mainIndex !== -1) {
      selectArr[mainIndex].set("thumb", 0);
    }
    selectArr[liIndex].set("thumb", 1);

    $('li[id="main"]').removeAttr("id");
    $("ul.Dform_imglist li:eq(" + liIndex + ")").attr("id", "main");
  }
});

//탭 처리.. 클릭시 li의 span태그에 class=on
$(document).on("click", "ul.days_tabSlide li", function () {
  //이전 class=on 없애고 클릭한 탭의 span에 class=on해주기
  $("ul.days_tabSlide li span").removeClass("on");
  $(this).find("span").addClass("on");
  getCurrentDataArr();
  drawThumbs();

  //처음 로드시엔 맨 처음요소 선택하게 하기..(사진 없을경우 동작하지 않으므로 미리 없애놓자)
  removeRightForm();
  $("ul.Dform_imglist li").eq(0).find("img").click();
  /*  if (liIndex != "ul.Dform_imglist li".length - 1) {
    $("ul.Dform_imglist li").eq(0).find("img").click();
  } else {
    alert("last");
    $("ul.Dform_imglist li:last img").click();
  }*/

  //$("ul.Dform_imglist li:first img").click();
});

//현재 선택한 formDataArr을 전역변수 selectArr에 셋팅

function getCurrentDataArr() {
  //탭이 바뀌면 왼쪽 미리보기도 싹 바뀌어야한다.
  // 클릭한 DAY 정보에 대한 data 셋팅
  var selectDay = $("span.on").text();
  var selectedData = dataArr.find((item) => item.day[0] === selectDay); //이게 데이터
  if (selectedData !== undefined) {
    selectArr = selectedData.data;
  } else {
    selectArr = [];
  }
  console.log(dataArr);
  console.log(selectArr);
}

// function : formDataArr를 폼에 띄우기 (by.서현)
function drawThumbs() {
  //전체를 그리는 메소드이므로 그리기 전에 이전 데이터 싹 지우기
  if (selectArr.length > 0) {
    $(".default_Dform_imgs").hide();
    $(".Dform_imgs").show();
    $(".Dform_imglist").empty();
    var tmp = 0;
    for (const formData of selectArr) {
      var file = formData.get("file");

      if (file instanceof File) {
        //맨뒤에 추가가 아니라 순서대로 화면에 로드만 하면됨
        var img = $("<img>").attr("src", URL.createObjectURL(file));
      } else if (typeof file === "string") {
        var img = $("<img>").attr("src", file);
      }

      //main인 데이터 이미 있으면 붙여줘야됨
      if (formData.get("thumb") == 0) {
        var li = $("<li>").attr("data-index", tmp).append(img);
      } else if (formData.get("thumb") == 1) {
        var li = $("<li>")
          .attr("id", "main")
          .attr("data-index", tmp)
          .append(img);
      }
      li.attr("data-attachNo", formData.get("attachNo"));
      li.attr("data-update", formData.get("dupdate"));
      $(".Dform_imglist").append(li);
      tmp++;
    }
    //추가버튼도 다시 생성
    $(".Dform_imglist").append(
      '<li id="moreImgLi" >' +
        '<input type="file" multiple id="moreImg" class="attach"></input>' +
        '<label for="moreImg"><i class="xi-plus"></i></label>' +
        "</li>"
    );
  } else {
    //만약 해당 탭에 아무 데이터가 없다면 default_Dform_imgs를 지워줘야한다.
    $(".Dform_imgs").hide();
    $(".default_Dform_imgs").show();
  }
}

//이미지파일 수정
$(document).on("change", "#fileChange", function (event) {
  // 지금 선택중인 파일 요소를 찾아서 이 파일로 수정해야됨
  // 현재 뿌려진 파일 배열 = selectArr, 그중에서도 선택된배열 = selectArr[liIndex]
  var tmpIndex = liIndex;
  var formData = new FormData();
  formData.append("file", event.target.files[0]);
  formData.append("title", selectArr[liIndex].get("title"));
  formData.append("text", selectArr[liIndex].get("text"));
  formData.append("thumb", selectArr[liIndex].get("thumb"));
  formData.append("attachNo", selectArr[liIndex].get("attachNo"));
  formData.append("dupdate", "change");
  selectArr[liIndex] = formData;
  // 데이터 바꿨으니까 화면 리로드
  // day 클릭한번, 이미지 클릭 한번
  $("ul.days_tabSlide .on").click();
  $("ul.Dform_imglist li").eq(tmpIndex).find("img").click();
});

//이미지 삭제 : 선택된 이미지 한번더 클릭
//sort는 전송시 인덱스를 넣어주는것이므로 이미지 삭제는 그냥 배열에서 없애주기만 하면됨!
var deleteArr = []; //삭제할 attachNo를 넣는 배열
$(document).on("click", "li.clickImg", function (event) {
  var tmpIndex = liIndex;

  var isLast = false;
  if (tmpIndex == selectArr.length - 1) {
    isLast = true;
  }

  if (confirm("삭제하시겠습니까?")) {
    //만약 attachNo=-1이면 그냥 삭제, attachNo!=-1이면 삭제배열에 attachNo 넣어두기
    if (selectArr[liIndex].get("attachNo") !== "-1") {
      deleteArr.push(selectArr[liIndex].get("attachNo"));
    }
    selectArr.splice(liIndex, 1);
    $("ul.days_tabSlide .on").click();
  }

  //방금 삭제한게 마지막이면 마지막요소 클릭하도록
  if (isLast && selectArr.length != 1) {
    //but 요소가 단 한개라면 클릭 X
    $("ul.Dform_imglist li")
      .eq(selectArr.length - 1)
      .find("img")
      .click();
  }
});

//이미지 순서 변경
$("ul.Dform_imglist").sortable({
  appendTo: "ul.Dform_imglist",
  items: "li:not(:last-child)",
  update: function (event, ui) {
    var newIndex = ui.item.index();
    var oldIndex = ui.item.attr("data-index");
    var item = selectArr[oldIndex];

    selectArr.splice(oldIndex, 1); // 기존 위치에서 삭제
    selectArr.splice(newIndex, 0, item); // 새 위치에 삽입

    $("ul.Dform_imglist li").each(function (index) {
      $(this).attr("data-index", index);
    });
  },
});

// 썸네일 설정 안한 부분이 있는지 검사한다.
function checkThumbnails(dataArr) {
  var result = false;
  for (var i = 0; i < dataArr.length; i++) {
    var arr = dataArr[i];
    var formDataArr = arr.data;
    var flag = false;
    for (var j = 0; j < formDataArr.length; j++) {
      if (formDataArr[j].get("thumb") == 1) {
        //해당 DAY에 썸네일이 있으면 flag=true;
        flag = true;
        break;
      }
    }
    if (!flag) {
      formDataArr[0].set("thumb", 1);
      result = true;
    }
  }
  console.log(result);
  return result;
}
//url 구해두기
var currentUrl = window.location.href;
var weeklyId = currentUrl.match(/weekly\/(\d+)\/daily/)[1];

//저장시 ajax
// dataArr : [ {DAY1,formDataArr[0]},{DAY2,formDataArr[1]},{DAY3,formDataArr[2]} ]
// formDataArr : [{formData},{file,title, text,thumb},{file,title, text,thumb}]
$(document).on("click", "button.Dform_btn_save", function (event) {
  event.preventDefault();
  // 유효성검사
  if (dataArr.length == 0) {
    alert("저장할 사진이 없습니다!");
    return;
  }
  // 버튼변화이벤트
  var clickBtn = $(this);
  $(clickBtn).attr("disabled", true);
  $(clickBtn).toggleClass("Dform_btn_save Dform_btn_disable");
  $(clickBtn).html("저장중..");
  // 서버로 데이터 전송
  //dataArr : [ {day,data}, {DAY1,formDataArr[0]},{DAY2,formDataArr[1]} ]
  //data: [{file,title,text,thumb},{file,title,text,thumb}]
  var postData = new FormData();
  console.log("===============================");
  if (checkThumbnails(dataArr))
    alert(
      "대표이미지를 선택하지 않은 데일리가 있습니다.\n 자동으로 가장 첫 사진을 썸네일로 설정합니다."
    );
  for (var i = 0; i < dataArr.length; i++) {
    // arr = {day,data} = DAY1, [{file,title,text,thumb},{file,title,text,thumb}]
    // formDataArr : [{file,title,text,thumb},{file,title,text,thumb}]
    var arr = dataArr[i];
    var formDataArr = arr.data;
    for (var j = 0; j < formDataArr.length; j++) {
      if (formDataArr[j].get("file") instanceof File) {
        postData.append("files", formDataArr[j].get("file"));
      } else {
        postData.append(
          "files",
          new File([], "tmp.txt", {
            type: "text/plain",
            lastModified: Date.now(),
          })
        );
      }

      if (formDataArr[j].get("title") == "") postData.append("titles", " ");
      else postData.append("titles", formDataArr[j].get("title"));
      if (formDataArr[j].get("text") == "") postData.append("texts", " ");
      else postData.append("texts", formDataArr[j].get("text"));

      postData.append("thumbs", formDataArr[j].get("thumb"));
      postData.append("days", "DAY" + (i + 1));
      postData.append("sorts", j);
      postData.append("attachNos", formDataArr[j].get("attachNo"));
      postData.append("dupdate", formDataArr[j].get("dupdate"));
    }
    // file끼리 모으고 {title,text,thumb}끼리 묶어서 얘넨 json으로 보내자
  }

  postData.append("deleteNos", deleteArr);

  $.ajax({
    url: "/TravelCarrier/weekly/" + weeklyId + "/daily" + "/create",
    type: "POST",
    data: postData,
    processData: false,
    contentType: false,
    success: function (dailies) {
      $(clickBtn).attr("disabled", false);
      $(clickBtn).toggleClass("Dform_btn_disable Dform_btn_save");
      $(clickBtn).html("저장하기");
      alert("저장되었습니다.");
      //바뀐 attachNo를 업데이트 해줘야함!!
      setFirst(dailies);
      getCurrentDataArr();
      drawThumbs();
      //첫번째 탭 자동클릭
      $("ul.days_tabSlide li:first").click();
    },
    error: function (error) {
      $(clickBtn).attr("disabled", false);
      $(clickBtn).toggleClass("Dform_btn_disable Dform_btn_save");
      $(clickBtn).html("저장하기");
      alert("응 실패 ㅋㅋ" + error);
    },
  });
});

// 시작 폼 모달창 띄우기 - by윤아
$(".writing").on("click", function () {
  $(".daily_form_bg").addClass("show");
});
$(".modal_title > .close").on("click", function () {
  $(".daily_form_bg").removeClass("show");
});

//데일리 이미지 슬라이드 구현 - by윤아
$(window).on("load", function () {
  var slide_width = $(".diary_slides").outerWidth();
  console.log(
    "비교 : 윈도우 :" + $(window).width() + "  /  슬라이드 :" + slide_width
  );
  //슬라이드 이동함수
  function moving(x, slide) {
    let left = parseInt(slide.css("left"));
    slide.css("left", left - x + "px"); //이동
    // 위치 리셋 시키기
    if (slide_width + (left - x) <= 0) {
      slide.css("left", slide_width + "px");
    }
    if ($(".diary_slides").eq(0).outerWidth() + (left - x) <= 0) {
      slide.css("left", slide_width + "px");
    }
  }
  if (slide_width > $(window).width()) {
    alert("복사하고 무브");

    // 슬라이드 복제하기 (clone-복제 / append-붙여넣기)
    let clone_slide = $(".diary_slides").eq(0).clone();
    $(".diary_viewport").append(clone_slide);

    //위치값이 0,0이라 곂치지 않게 두 슬라이드의 위치 지정
    $(".diary_slides").eq(0).css("left", "0px");
    $(".diary_slides")
      .eq(1)
      .css("left", slide_width + "px");

    //이동 거리
    var movingDistance = 1;
    //이동 함수 적용하기
    var originMove = setInterval(() => {
      moving(movingDistance, $(".diary_slides").eq(0));
    }, parseInt(1000 / 100));

    var cloneMove = setInterval(() => {
      moving(movingDistance, $(".diary_slides").eq(1));
    }, parseInt(1000 / 100));

    // mouseenter시 슬라이드 일시정지
    $(".diary_slides").on("mousemove", function () {
      clearInterval(originMove);
      clearInterval(cloneMove);
      movingDistance = 0;
    });

    //mouseleave시 슬라이드 재생
    $(".diary_slides").on("mouseleave", function () {
      movingDistance = 1;
      //이동 함수 적용하기
      originMove = setInterval(() => {
        moving(movingDistance, $(".diary_slides").eq(0));
      }, parseInt(1000 / 100));

      cloneMove = setInterval(() => {
        moving(movingDistance, $(".diary_slides").eq(1));
      }, parseInt(1000 / 100));
    });
  } else {
  }

  // 댓글 모달창 활성화 - by윤아
  $(".diary_viewport").on("click", ".d_slide > .reply_icon", function (e) {
    $(".reply_modal").addClass("show");
    var reply_img = $(e.target).parent().parent().find("img").attr("src");
    $(".reply_img img").attr("src", `${reply_img}`);
    $(".diary_noH .writing").hide();

    // 댓글 모달창 세로선 자동 생성 및 길이 수정
    var reply_height = $(".reply_scroll").height();
    console.log("댓글 스크롤 길이" + reply_height);
    $(".reply_screen::before").css("height", (reply_height / 80) * 100 + "%");
  });

  //댓글모달창비활성화
  $(".reply_modal .close").click(function () {
    $(".reply_modal").removeClass("show");
    $(".diary_noH .writing").show();
  });

  //슬라이드 호버시
  $(".diary_slides").on("mousemove", function () {
    // title, period 숨기기
    $(".diary_titlebox").addClass("hide");
    // 댓글 아이콘 활성화
    $(".reply_icon").addClass("show");
    //2.배경 어둡게
    $(".filter").addClass("on");
    $(".diary_viewport").addClass("black");
    $(".diary_noH .writing").hide();
  });
  $(".diary_slides").on("mouseleave", function () {
    // title, period 보이기
    $(".diary_titlebox").removeClass("hide");
    // 댓글 아이콘 비활성화
    $(".reply_icon").removeClass("show");
    //슬라이드 양끝 흐림효과
    $(".diary_noH").css({
      background: "#efeee9",
    });
    //3.배경 흐리게 제거
    $(".filter").removeClass("on");
    //슬라이드 양끝 흐림효과
    $(".diary_viewport").removeClass("black");
    $(".diary_noH .writing").show();
  });
});

// daily 일기화면(hover) - by.윤아
$(".diary_viewport").on("mouseenter", ".d_slide", function (e) {
  // 복제된 diary_slides에는 mouseenter 이벤트가 적용되지 않아 위임 방식을 사용하여, 부모 요소인 .diary_viewport에 이벤트를 바인딩함

  //1. 백그라운드 바꾸기
  var img_src = e.target.src;
  console.log(e.target);
  $(".diary_noH").css({
    "background-image": `url(${img_src})`,
    "background-repeat": "no-repeat",
    "background-position": "center center",
    "background-size": "cover",
  });

  //3. 일기글 보이기
  $(".diary_textbox").addClass("on");
  var diary_title = $(e.target).parent().data("title");
  var diary_text = $(e.target).parent().data("text");

  $(".diary_textbox h6").html(diary_title);
  $(".diary_textbox p").html(diary_text);

  $(".d_slide").on("mouseleave", function (e) {
    //4.일기 숨기기
    $(".diary_textbox").removeClass("on");
  });
});

// 슬라이드 사진 크기에 따라 클래스명 변경 - by.서현
$(document).ready(function () {
  $(".d_slide > img").each(function (index) {
    const $img = $(this);
    const img = new Image();
    img.src = $(this).attr("src");
    img.onload = function () {
      var ratio = img.width / img.height;
      console.log(ratio);
      if (0.9 <= ratio && ratio <= 1.1) {
        $img.parent().addClass("sqr");
      } else if (ratio < 0.9) {
        $img.parent().addClass("rectL");
      } else if (1.1 < ratio) {
        $img.parent().addClass("rectW");
      }
    };
  });
});

//텍스트 박스 안의 ID와  다이어리리스트 안의 ID가 같을 때

// `diary_textbox`와 `diary_list`의 각각의 `li` 요소들을 가져옵니다.
// const diaryTextboxItems = diaryTextbox.children("li");
// const diaryListItems = diaryList.children("li");

// `diaryTextboxItems`와 `diaryListItems`를 반복하며, `th:id` 값이 같은 것을 찾습니다.
// for (let i = 0; i < diaryTextboxItems.length; i++) {
//   for (let j = 0; j < diaryListItems.length; j++) {
//     if (
//       diaryTextboxItems[i].getAttribute("th:id") ===
//       diaryListItems[j].getAttribute("th:id") // 주어진 요소의 특정 속성의 값을 가져오는 함수입니다. 즉, getAttribute("th:id")는 해당 요소의 th:id 속성 값을 반환
//     ) {
//       // `th:text` 값들을 가져와서 출력합니다.
//       const title = diaryTextboxItems[i]
//         .querySelector("th")
//         .getAttribute("th:text");
//       const text = diaryTextboxItems[i]
//         .querySelector("td")
//         .getAttribute("th:text");
//       console.log(`Title: ${title}, Text: ${text}`);
//     }
//   }
// }