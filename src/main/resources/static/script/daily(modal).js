let slideInterval;
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

var dailies;
$(document).ready(function () {
  $.ajax({
    type: "GET",
    url: "/weekly/" + weeklyId + "/dailies",
    dataType: "json",
    success: function (resp) {
      console.log(resp);
      dailies = resp;
      setFirst(dailies);

      //슬라이드 셋팅
      setSlideClass();
      setSlide();
      //cloneSlide();
      startSlide(); //슬라이드 실행
      setSlideClass();

      //모달 셋팅
      getCurrentDataArr();
      drawThumbs();
      //첫번째 탭 자동클릭
      $("ul.days_tabSlide li:first").click();
    },
    error: function (jqXHR, textStatus, errorThrown) {
      alert("실패 : " + textStatus);
    },
  });
});

function reloadDailies(newDailies) {
  liIndex = "";
  selectArr = [];
  dataArr = [];
  $(".diary_slides").empty();

  dailies = newDailies;
  setFirst(dailies);

  setSlideClass();
  setSlide(); //슬라이드 셋팅
  //cloneSlide(); //슬라이드복제
  startSlide(); //슬라이드 실행
  setSlideClass();

  //모달 셋팅
  getCurrentDataArr();
  drawThumbs();
  //첫번째 탭 자동클릭
  $("ul.days_tabSlide li:first").click();
}

// 슬라이드 셋팅
function setSlide() {
  var newUl = $("<ul>").addClass("diary_list");
  for (var i = 0; i < dataArr.length; i++) {
    for (var j = 0; j < dataArr[i].data.length; j++) {
      var newLi = $("<li>")
        //.attr("id", dataArr[i].data[j].get("attachNo"))
        .attr("data-text", dataArr[i].data[j].get("text"))
        .attr("data-title", dataArr[i].data[j].get("title"))
        .addClass("d_slide square sqr");
      console.log(dataArr[i].data[j].get("file"));
      var newImg = $("<img>").attr({
        src: dataArr[i].data[j].get("file"),
        alt: "사진표시할수없음",
        "data-attachNo": dataArr[i].data[j].get("attachNo"),
      });
      newImg.on("error", function () {
        $(this).attr("src", "/image/daily/icon/changeImg.svg");
      });
      newLi.append(newImg);
      newImg.addClass("drag"); //이미지 드래그잔상방지
      newLi.append(`<div class="reply_icon">
                              <img src="/image/daily/icon/message.png" alt="댓글아이콘이미지">
                            </div>`);
      newUl.append(newLi);
    }

    $(".diary_slides").append(newUl);
  }
}

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
  $('li[class="clickImg icon_bg"]').removeAttr("class");
  $('div.daily_title input[type="text"]').val("");
  $("div.daily_text textarea").val("");
  $('input[type="radio"]').prop("checked", false);
  $("div.filePath p").text("");
}
// 사진 클릭이벤트
$(document).on("click", "ul.Dform_imglist li img", function (event) {
  removeRightForm();
  //선택항목 검은 배경 지우기
  $("ul.Dform_imglist li span").removeAttr("class", "icon_bg_dark");
  //만약 selectArr이 0, 즉 defailt가 떠있으면 실행 X
  if (selectArr.length !== 0) {
    $(this).parent("span").parent("li").attr("class", "clickImg icon_bg");
    //선택항목 검은 배경 추가
    $(this).parent("span").attr("class", "icon_bg_dark");
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
        var li = $("<li>")
          .attr("data-index", tmp)
          .append($("<span>").append(img));
      } else if (formData.get("thumb") == 1) {
        var li = $("<li>")
          .attr("id", "main")
          .attr("data-index", tmp)
          .append($("<span>").append(img));
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
  if (tmpIndex != 0) $("ul.Dform_imglist li").eq(tmpIndex).find("img").click();
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
  } else return;

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
      if (formDataArr.length > 0) {
        formDataArr[0].set("thumb", 1);
        result = true;
      } else result = false;
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
    //alert("저장할 사진이 없습니다!");
    return;
  }
  $("#loading").addClass("show");
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
  //  if (checkThumbnails(dataArr))
  //    alert(
  //      "대표이미지를 선택하지 않은 데일리가 있습니다.\n 자동으로 가장 첫 사진을 썸네일로 설정합니다."
  //    );
  for (var i = 0; i < dataArr.length; i++) {
    // arr = {day,data} = DAY1, [{file,title,text,thumb},{file,title,text,thumb}]
    // formDataArr : [{file,title,text,thumb},{file,title,text,thumb}]
    var arr = dataArr[i];
    //return;

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
      postData.append("days", arr.day);
      postData.append("sorts", j);
      postData.append("attachNos", formDataArr[j].get("attachNo"));
      postData.append("dupdate", formDataArr[j].get("dupdate"));
    }
    // file끼리 모으고 {title,text,thumb}끼리 묶어서 얘넨 json으로 보내자
  }

  postData.append("deleteNos", deleteArr);

  $.ajax({
    url: "/weekly/" + weeklyId + "/daily" + "/create",
    type: "POST",
    data: postData,
    dataType: "json",
    processData: false,
    contentType: false,
    success: function (data) {
      $(clickBtn).attr("disabled", false);
      $(clickBtn).toggleClass("Dform_btn_disable Dform_btn_save");
      $(clickBtn).html("저장하기");
      alertModal2("", "저장되었습니다.");
      reloadDailies(data);

      $("#loading").removeClass("show");
      $(".daily_form_bg").removeClass("show");
    },
    error: function (error) {
      $(clickBtn).attr("disabled", false);
      $(clickBtn).toggleClass("Dform_btn_disable Dform_btn_save");
      $(clickBtn).html("저장하기");
      alertModal2("실패 : ", error);
      $("#loading").removeClass("show");
    },
  });
});

// 데일리 작성폼 모달창 띄우기 - by윤아
$(".writing").on("click", function () {
  $(".daily_form_bg").addClass("show");
});
$(".modal_title > .close").on("click", function (e) {
  e.preventDefault();
  $(".daily_form_bg").removeClass("show");
});

//슬라이드 실행
function startSlide() {
  let origin_slide = $("ul.diary_list");
  origin_slide.attr("id", "slide1");
  // origin_slide.id = "slide1";

  var slide_width = $("ul.diary_list#slide1").outerWidth();
  var veiw_width = $(".diary_viewport").outerWidth();
  console.log(slide_width + "////" + veiw_width);
  //화면 크기에 따라 실행
  if (slide_width > veiw_width) {
    //슬라이드 복제
    let clone_slide = origin_slide.clone(); //(true)로 자식요소까지 복제해서 변수에 할당함 스크립트해당
    document.querySelector("div.diary_slides").appendChild(clone_slide[0]); //복제된 슬라이드를 자식요소로 붙여넣음
    origin_slide.prop("id", "slide2");
    // clone_slide.id = "slide2";
    $(".diary_viewport").removeClass("center");
    //슬라이드 실행
    playSlide();
    // //만약 슬라이드가 2개일 경우 재생, 일시정지 기능 추가 및 드래그 기능 추가
    // if ($(".diary_list").length <= 1) return;
    // $(document).on("mouseenter", ".diary_viewport", function (e) {
    //   stopSlide();
    //   dragSlide();
    // });
    // $(document).on("mouseleave", ".diary_viewport", function (e) {
    //   playSlide();
    // });
  } else {
    //슬라이드 멈춤 및 가운데 정렬
    stopSlide();
    $(".diary_viewport").addClass("center");
    console.log("슬라이드야 멈처라ㅓ");
  }
}
//슬라이드 복제 함수 작성
function cloneSlide() {
  let origin_slide = document.querySelector("ul.diary_list#slide1");
  let clone_slide = origin_slide.cloneNode(true); //(true)로 자식요소까지 복제해서 변수에 할당함
  clone_slide.id = "slide2";
  clone_slide.classList = origin_slide.classList;

  document.querySelector("div.diary_slides").appendChild(clone_slide); //복제된 슬라이드를 자식요소로 붙여넣음

  document.querySelector("#slide1").style.left = "0px";
  document.querySelector("#slide2").style.left = "0px";

  //슬라이드 실행
  // origin_slide.classList.add("original");
  // clone_slide.classList.add("clone");
  // $(".diary_viewport").removeClass("center");
  // playSlide();
  // } else {
  //   //슬라이드 멈춤 및 가운데 정렬
  //   stopSlide();
  //   $(".diary_viewport").addClass("center");
  //   console.log("슬라이드야 멈처라ㅓ");
  // }
}
//인터벌 메서드로 애니메이션 생성
function playSlide() {
  let move = 1; //이동 크기 - 정수여야 함
  let slide = $(".diary_slides"); // jQuery로 .diary_slides 요소 선택

  // if ($(".diary_list").length > 1) {
  slideInterval = window.setInterval(
    () => movingSlide(move, slide),
    parseInt(1000 / 100)
  );
  // }
  // 인터벌 애니메이션 함수(공용)
  function movingSlide(d, slide) {
    let left = parseInt($(".diary_slides").css("left"));
    let ulleft = parseInt($(".diary_list").eq(0).width()); //original 슬라이드의 너비
    left = parseInt(left, 10); // 10진수로 변환
    slide.css("left", left - d + "px"); //이동
    // slide1이 화면밖으로 이동하면 append 하기
    if (-left === ulleft) {
      //이동값과 슬라이드1개의 너비가 일치하면 실행
      $(".diary_slides").append($(".diary_list").eq(0));
      $(".diary_slides").css("left", "0px");
    }
  }
}
//슬라이드 멈춤, 인터벌 멈춤
function stopSlide() {
  clearInterval(slideInterval);
}

// daily 일기화면(mouseenter)시 효과 - by.윤아
$(document).on("mouseenter", ".diary_viewport li.d_slide > img", function (e) {
  // 복제된 diary_slides에는 mouseenter 이벤트가 적용되지 않아 위임 방식을 사용하여, 부모 요소인 .diary_viewport에 이벤트를 바인딩함
  mouseoverEffect(e);
  stopSlide();
  if ($(".diary_list").length > 1) {
    dragSlide();
  }
});
//daily 일기화면(mouseleave)시 효과
$(".diary_slides").on("mouseleave", function (e) {
  mouseleaveEffect(e);
  if ($(".diary_list").length > 1) {
    playSlide();
  }
});
//슬라이드 마우스오버시 변화
function mouseoverEffect(e) {
  //1. 백그라운드 바꾸기
  var img_src = e.target.src;
  $(".diary_noH").css({
    "background-image": `url(${img_src})`,
    "background-repeat": "no-repeat",
    "background-position": "center center",
    "background-size": "cover",
  });

  //1.제목숨기기 title, period 숨기기
  $(".diary_titlebox").addClass("hide");

  //2. 일기글 보이기
  $(".diary_textbox").addClass("on");

  //3. 댓글 아이콘 활성화
  $(".reply_icon").addClass("show");

  //4.필터 활성화
  $(".filter").addClass("on");
  $(".diary_viewport").addClass("black");
}
//슬라이드 마우스리브시 효과
function mouseleaveEffect() {
  //배경화면 색 변경하기
  $(".diary_noH").css({
    background: "#efeee9",
  });

  // 1. title, period 보이기
  $(".diary_titlebox").removeClass("hide");

  //2. 댓글 아이콘 비활성화
  $(".reply_icon").removeClass("show");

  //3. 일기글 숨기기
  $(".diary_textbox").removeClass("on");

  //4.필터 비활성화
  $(".filter").removeClass("on");
  $(".diary_viewport").removeClass("black");
}

//슬라이드 드래그
// dragSlide();
// $(".diary_viewport").on("mouseenter", function () {
// dragSlide();
// });
//드래그를 위해 변수 초기화 해주기(각각 시작지점,끝지점)

function dragSlide(e) {
  // if ($(".diary_list").length < 1) return;
  let viewport = $(".diary_viewport"); // jQuery 객체를 DOM 요소로 변환
  let slide = $(".diary_slides");
  let pressed = false; // 눌려진 상태를 의미
  let startPoint = 0; // 시작점
  let dragStartPoint = 0; // 드래그 시작점

  // 드래그한 거리를 저장할 변수 추가
  let currentPoint = 0;

  // 마우스를 눌렀을 때
  viewport.on("mousedown", function (e) {
    // e.preventDefault();
    pressed = true; // 마우스가 눌려져있음
    startPoint = e.clientX;
    dragStartPoint = parseInt(slide.css("left")) || 0;
    currentPoint = startPoint;
  });
  viewport.on("mouseup", function (e) {
    pressed = false; // 마우스가 떼어져있음
  });

  viewport.on("mousemove", function (e) {
    if (!pressed) return;
    e.preventDefault();
    endPoint = e.clientX;
    let moveD = endPoint - currentPoint;
    let newLeft = dragStartPoint + moveD;
    // console.log(endPoint + " - " + currentPoint + " =  moveD : " + moveD);
    // console.log("😍left :" + dragStartPoint + "/ + " + moveD + " = " + newLeft);
    slide.css("left", newLeft + "px");

    // 슬라이드의 현재 left 값을 가져오기
    // 조건설정시 필요 변수들
    let slideleft = parseInt(slide.css("left"));
    let slideWidth = parseInt($(".diary_slides").width());
    let ulWidth = parseInt($(".diary_list#slide1").width());
    let viewportWidth = parseInt($(".diary_viewport").width());
    console.log(
      "좌우 끝까지 이동시 : " +
        slideleft +
        " / " +
        ulWidth +
        " / " +
        (slideWidth - viewportWidth)
    );
    //슬라이드left가 0일때
    //슬라이드의 -left가 전체너비랑 같을 때
    if (slideleft > 0) {
      // 슬라이드가 처음에 있을 때
      slide.css("left", -ulWidth + "px");
    } else if (-slideleft >= slideWidth - viewportWidth) {
      // 슬라이드가 오른쪽으로 이동한 경우
      slide.css("left", -(slideWidth - viewportWidth - ulWidth) + "px");
      // $(".diary_slides").prepend($(".diary_list:last-child"));
    }
    // else if (-ulWidth >= slideWidth - viewportWidth) {
    //   // 슬라이드가 오른쪽으로 이동한 경우
    //   console.log("끝에 도착했을떄");
    //   // slide.css("left", -ulWidth + "px");
    //   $(".diary_slides").prepend($(".diary_list:last-child"));
    // } else if (slideleft < -ulWidth) {
    //   // 슬라이드가 왼쪽으로 이동한 경우
    //   $(".diary_slides").append($(".diary_list:first-child"));
    // }

    startPoint = endPoint; // startPoint를 현재 endPoint로 업데이트
  });
}

function checkBoundary() {
  let slide = $(".diary_slides");
  let slideLeft = parseInt(slide.css("left"));

  if (slideLeft === 0) {
    let ulLeft = parseInt($(".diary_list").eq(0).width());
    if (-slideLeft >= ulLeft) {
      // 이동값과 슬라이드 1개의 너비가 일치하면 실행
      $(".diary_slides").append($(".diary_list").eq(0).clone());
      $(".diary_slides").css("left", "0px");
    }
  }

  // if (inner.left > outer.left) {
  //   slide.style.left = "0px";
  // } else if (inner.right < outer.right) {
  //   slide.style.left = `-${inner.width - outer.width}px`;
  //   let left = parseInt($(".diary_slides").css("left"));
  //   let ulleft = parseInt($(".diary_list").eq(0).width());

  // }
}

// 슬라이드 사진 크기에 따라 클래스명 변경 - by.서현
function setSlideClass() {
  $(".d_slide > img").each(function (index) {
    const $img = $(this);
    const img = new Image();
    img.src = $(this).attr("src");
    img.onload = function () {
      var ratio = img.width / img.height;
      // console.log(ratio);
      $img.parent().removeClass("sqr");
      if (0.9 <= ratio && ratio <= 1.1) {
        $img.parent().addClass("sqr");
      } else if (ratio < 0.9) {
        $img.parent().addClass("rectL");
      } else if (1.1 < ratio) {
        $img.parent().addClass("rectW");
      }
    };
  });
}

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

//슬라이드 호버시 텍스트 바꿔주기 -by윤아
$(document).on("mouseenter", ".diary_viewport .d_slide", function (e) {
  var hover_attachNo = $(e.target).data("attachno");
  //console.log(hover_attachNo); //숫자로 잘 출력됨

  function making_slideArray() {
    var diary_attachNo = []; //비어있는 ARRAY배열 생성
    for (var i = 0; i < dataArr.length; i++) {
      for (var j = 0; j < dataArr[i].data.length; j++) {
        var diaryData = {
          text: dataArr[i].data[j].get("text"),
          title: dataArr[i].data[j].get("title"),
          file: dataArr[i].data[j].get("file"),
          attachNo: dataArr[i].data[j].get("attachNo"),
        }; //object자료형으로 생성
        diary_attachNo.push(diaryData); //비어있는 array자료형 안에 담아주기
      }
    }
    return diary_attachNo; // 결과값
  }

  var made_slideArray = making_slideArray(); //결과 변수에 담아주기
  //console.log("결과");
  //console.log(made_slideArray); //(5) [{…}, {…}, {…}, {…}, {…}] 예시 출력

  function find_attachNo(num) {
    return num.attachNo == hover_attachNo;
  } //array 안에 object 자료형이 있을 때 해당 배열 찾기

  var hover_data = made_slideArray.find(find_attachNo);
  //console.log(hover_data); // {text: '텍스트.', title: '난쟁이01', file: 'd41e22a73e52.jpg', attachNo: '203'} 출력예시
  //이제 text꺼내서 html에 붙여넣어주면 됨
  //console.log(hover_data.title); //난쟁이01
  $(".diary_textbox ul li h6").text(hover_data.title);
  $(".diary_textbox ul li p").text(hover_data.text);
});
