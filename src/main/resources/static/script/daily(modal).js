//li id에 main을 넣으면 대표사진, 클릭중인건 li의 id가 clickImg

var formDataArr = [];
var liIndex = "";

// function : 사진첨부시 동작 (by.서현)
$(document).on('change', '#moreImg', function(event){
    setDataArr(event);
    drawThumb();
});

// function : 사진첨부시 formDataArr 생성 (by.서현)
function setDataArr(event){
    for (var file of event.target.files) {
        var formData = new FormData();
        formData.append('file', file);
        formData.append('title', "");
        formData.append('text', "");
        formData.append('thumb', 0);
        formDataArr.push(formData);
    }
}

// function : formDataArr를 폼에 띄우기 (by.서현)
function drawThumb(){
    //전체를 그리는 메소드이므로 그리기 전에 이전 데이터 싹 지우기
    $('.Dform_imglist').empty();
    for (const formData of formDataArr) {
         //맨뒤에 추가가 아니라 순서대로 화면에 로드만 하면됨
         var img = $('<img>').attr('src', URL.createObjectURL(formData.get('file')));
         var li = $('<li>').append(img);
         $('.Dform_imglist').append(li);
     }
     //추가버튼도 다시 생성
    $('.Dform_imglist').append('<li id="moreImgLi" >'+
               '<input type="file" multiple id="moreImg"></input>'+
               '<label for="moreImg"><i class="xi-plus"></i></label>'+
             '</li>');
}

// 사진 클릭이벤트
$(document).on('click', 'ul.Dform_imglist li img', function(event){
    $('li[id="clickImg"]').removeAttr('id');
    $('div.daily_title input[type="text"]').val('');
    $('div.daily_text input[type="textarea"]').val('');

    $(this).closest('li').attr('id','clickImg');
    liIndex = $(this).closest('li').index();
    $('div.daily_title input[type="text"]').val(formDataArr[liIndex].get('title'));
    $('div.daily_text textarea').val(formDataArr[liIndex].get('text'));

    if(formDataArr[liIndex].get('thumb')==0){
        $('input[type="radio"]').prop('checked', false);
    } else if(formDataArr[liIndex].get('thumb')==1){
        $('input[type="radio"]').prop('checked', true);
    }
})

//재목 수정시 바로 배열에 저장
$(document).on('change', 'div.daily_title input', function(event){
   // var liIndex = $('ul.Dform_imglist li').index($('li#clickImg'));
    formDataArr[liIndex].set('title', event.target.value);
})
//메모 수정시 바로 배열에 저장
$(document).on('change', 'div.daily_text textarea', function(event){
  //  var liIndex = $('ul.Dform_imglist li').index($('li#clickImg'));
    formDataArr[liIndex].set('text', event.target.value);
})
//대표이미지 설정
$(document).on('change', 'input[type="radio"]', function() {
  if($(this).is(':checked')) {
  //라디오버튼 체크되면 이전에 되어있던 id=main 없애고 mai이었던 배열도 thumb=0으로 바꿔야함
    var mainIndex = $('ul.Dform_imglist li').index($('li#main'));
    if(mainIndex !== -1) {
      formDataArr[mainIndex].set('thumb', 0);
    }
    formDataArr[liIndex].set('thumb', 1);

    $('li[id="main"]').removeAttr('id');
    $('ul.Dform_imglist li:eq(' + liIndex + ')').attr('id', 'main');
  }
});