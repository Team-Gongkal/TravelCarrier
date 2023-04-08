
$(document).ready(function() {
   //첫번째 탭 자동클릭
   $('ul.days_tabSlide li:first').click();
});

//화면 로드되면 바로 click Day1 해주기
var liIndex = "";
//formData day정보와 함께 배열로 저장한다. [ {DAY1,formDataArr[0]},{DAY2,formDataArr[1]},{DAY3,formDataArr[2]} ]
//선택한 tap에 해당하는 formDataArr을 저장 [{file,title, text,thumb},{file,title, text,thumb},{file,title, text,thumb}]
var dataArr = [];
var selectArr = [];

// function : 사진첨부시 동작 (by.서현)
$(document).on('change', '.attach', function(event){
    setDataArr(event);
    getCurrentDataArr();
    drawThumbs(selectArr);
    //처음 로드시엔 맨 처음요소 선택하게 하기
    $('ul.Dform_imglist li:first img').click();
});
// function : 사진첨부시 dataArr 생성 (by.서현)
function setDataArr(event){
    var formDataArr = selectArr;
    for (var file of event.target.files) {
        var formData = new FormData();
        formData.append('file', file);
        formData.append('title', "");
        formData.append('text', "");
        formData.append('thumb', 0);
        formDataArr.push(formData);
    }
    var obj = { day : [$('.days_tabSlide .on').text()],
                data : formDataArr };
    dataArr.push(obj);
    console.log("추가분 : "+obj.day+", "+obj.data);
}

// 오른쪽 폼(제목,메모,경로,대표) 비우기
function removeRightForm(){
    $('li[class="clickImg"]').removeAttr('class');
    $('div.daily_title input[type="text"]').val('');
    $('div.daily_text input[type="textarea"]').val('');
    $('input[type="radio"]').prop('checked', false);
    $('p.filePath').text("파일명.jpg");
}
// 사진 클릭이벤트
$(document).on('click', 'ul.Dform_imglist li img', function(event){
    removeRightForm();

    //만약 selectArr이 0, 즉 defailt가 떠있으면 실행 X
    if(selectArr.length !== 0){
        $(this).closest('li').attr('class','clickImg');
        liIndex = $('ul.Dform_imglist li').index($('li.clickImg'));
        $('div.daily_title input[type="text"]').val(selectArr[liIndex].get('title'));
        $('div.daily_text textarea').val(selectArr[liIndex].get('text'));
        $('div.daily_files p').text(selectArr[liIndex].get('file').name);
        if(selectArr[liIndex].get('thumb')==0){
            $('input[type="radio"]').prop('checked', false);
        } else if(selectArr[liIndex].get('thumb')==1){
            $('input[type="radio"]').prop('checked', true);
        }
    }
});

//제목 수정시 바로 배열에 저장
$(document).on('change', 'div.daily_title input', function(event){
console.log(selectArr);
console.log(liIndex);
    selectArr[liIndex].set('title', event.target.value);
})
//메모 수정시 바로 배열에 저장
$(document).on('change', 'div.daily_text textarea', function(event){
    selectArr[liIndex].set('text', event.target.value);
})
//대표이미지 설정
$(document).on('change', 'input[type="radio"]', function() {
  if($(this).is(':checked')) {
  //라디오버튼 체크되면 이전에 되어있던 id=main 없애고 mai이었던 배열도 thumb=0으로 바꿔야함
    var mainIndex = $('ul.Dform_imglist li').index($('li#main'));
    if(mainIndex !== -1) {
      selectArr[mainIndex].set('thumb', 0);
    }
    selectArr[liIndex].set('thumb', 1);

    $('li[id="main"]').removeAttr('id');
    $('ul.Dform_imglist li:eq(' + liIndex + ')').attr('id', 'main');
  }
});


//탭 처리.. 클릭시 li의 span태그에 class=on
$(document).on('click', 'ul.days_tabSlide li', function() {
    //이전 class=on 없애고 클릭한 탭의 span에 class=on해주기
    $('ul.days_tabSlide li span').removeClass('on');
    $(this).find('span').addClass('on');
    getCurrentDataArr();
    drawThumbs(selectArr);

    //처음 로드시엔 맨 처음요소 선택하게 하기..(사진 없을경우 동작하지 않으므로 미리 없애놓자)
    removeRightForm();
    $('ul.Dform_imglist li:first img').click();
});

//현재 선택한 formDataArr을 전역변수 selectArr에 셋팅
function getCurrentDataArr(){
    //탭이 바뀌면 왼쪽 미리보기도 싹 바뀌어야한다.
    // 클릭한 DAY 정보에 대한 data 셋팅
    var selectDay = $('span.on').text();
    selectArr = dataArr.find(item => item.day[0] === selectDay); //이게 데이터
    if(selectArr !== undefined){
        selectArr = selectArr.data;
    }
    else {
        selectArr = [];
    }
}

// function : formDataArr를 폼에 띄우기 (by.서현)
function drawThumbs(selectArr){
    //전체를 그리는 메소드이므로 그리기 전에 이전 데이터 싹 지우기
    if(selectArr.length > 0){
        $(".default_Dform_imgs").hide();
        $(".Dform_imgs").show();
        $('.Dform_imglist').empty();
        for (const formData of selectArr) {
             //맨뒤에 추가가 아니라 순서대로 화면에 로드만 하면됨
             var img = $('<img>').attr('src', URL.createObjectURL(formData.get('file')));
             //main인 데이터 이미 있으면 붙여줘야됨
             if(formData.get('thumb')==0){
                var li = $('<li>').append(img);
             }else if(formData.get('thumb')==1){
                var li = $('<li>').attr('id', 'main').append(img);
             }
             $('.Dform_imglist').append(li);
         }
         //추가버튼도 다시 생성
        $('.Dform_imglist').append('<li id="moreImgLi" >'+
                   '<input type="file" multiple id="moreImg" class="attach"></input>'+
                   '<label for="moreImg"><i class="xi-plus"></i></label>'+
                 '</li>');
    } else{
        //만약 해당 탭에 아무 데이터가 없다면 default_Dform_imgs를 지워줘야한다.
        $(".Dform_imgs").hide();
        $(".default_Dform_imgs").show();
    }
}


//이미지파일 수정
$(document).on('click', 'div.daily_files img', function() {
    alert("이미지 바꾸기!!!");
});

//이미지 순서 변경
//이미지 삭제


//저장시 ajax
// dataArr : [ {DAY1,formDataArr[0]},{DAY2,formDataArr[1]},{DAY3,formDataArr[2]} ]
// formDataArr : [{formData},{file,title, text,thumb},{file,title, text,thumb}]
$(document).on('click', 'button.Dform_btn_save', function(event) {
    event.preventDefault();
  // 서버로 데이터 전송
  //dataArr : [ {day,data}, {DAY1,formDataArr[0]},{DAY2,formDataArr[1]} ]
  //data: [{file,title,text,thumb},{file,title,text,thumb}]

    //url 구해두기
    var currentUrl = window.location.href;
    var match = currentUrl.match(/weekly\/(\d+)\/daily/);
    var weekyId = match && match[1];
    var url = `/TravelCarrier/weekly/${weekyId}/daily/create`;
    var postData = new FormData();
    for(var i = 0; i < dataArr.length; i++){
        // arr = {day,data} = DAY1, [{file,title,text,thumb},{file,title,text,thumb}]
        // formDataArr : [{file,title,text,thumb},{file,title,text,thumb}]
        var arr = dataArr[i];
        var formDataArr = arr.data;

        for (var j=0; j<formDataArr.length; j++) {
          postData.append('files', formDataArr[j].get('file'));
          postData.append('titles', formDataArr[j].get('title'));
          postData.append('texts', formDataArr[j].get('text'));
          postData.append('thumbs', formDataArr[j].get('thumb'));
          postData.append('days', "DAY"+(i+1));
          postData.append('sorts', j);
        }
        // file끼리 모으고 {title,text,thumb}끼리 묶어서 얘넨 json으로 보내자
        console.log("출발 전 : "+postData);
    }

    $.ajax({
        url: url,
        type: "POST",
        data:  postData,
        processData: false,
        contentType: false,
        success: function(response) {
            alert("성공해따아아아앙악");
        },
        error: function(error) {
            console.error("응 실패 ㅋㅋ");
            console.error(error);
        }
    });

});
