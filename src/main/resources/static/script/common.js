window.addEventListener("load", function () {
  //document 안의 이미지, js 파일 포함 전부 로드가 되었을 경우 실행할 코드
  var header = document.createElement("div"); //

  header.innerHTML = `<header>
<nav>
  <div class="nav">
    <div class="hamburger on">
      <span></span>
      <span></span>
    </div>
    <div class="logo">
      <a href="index.html">
        <img src="../resources/main/image/logo/journal.png" alt="logo">
      </a>
    </div>
  </div>
  <div class="board_link">
    <a href="">Board</a>
  </div>
  <div classo="contact_link">
    <a href="">Contact</a>
  </div>
</nav>
<div class="navigation"></div>
</header>
<!-- utill 유틸 -->
<div class="utill_menu">
<div class="notice">
  <img src="../resources/main/image/icon/notification.png" alt="실시간 알림 아이콘">
</div>
<div class="profile">
  <a href="#none"></a>
</div>
</div>`;

  console.log(header);

  document.querySelector("#common").appendChild(header);
});
