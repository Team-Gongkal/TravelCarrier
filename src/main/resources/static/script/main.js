
console.log(randImages);

//randImages에 key:valueObject(5) 형태로 값이 셋팅되어있음. key는 클래스값이므로 이를 기준으로 맵을 셋팅

// SVG 파일영역(맵) 가져오기
const svg = document.querySelector('svg');

// 'defs' 요소 생성 (이 태그 안에 pattern들을 저장해둘것임)
createPattern();
function createPattern(){
    const defs = document.createElementNS('http://www.w3.org/2000/svg', 'defs');
    svg.appendChild(defs);

    for (const country in randImages) {
        console.log(`Country: ${country}`);
        const urlArray = randImages[country];
        var index = 1;
        urlArray.forEach((url) => {
            console.log(url);
            //패턴 생성
            const pattern = document.createElementNS('http://www.w3.org/2000/svg', 'pattern');
            pattern.setAttribute('id', `${country}${index++}`);
            pattern.setAttribute('class', `image-${country}`);
            pattern.setAttribute('x', '0');
            pattern.setAttribute('y', '0');
            pattern.setAttribute('width', '100%');
            pattern.setAttribute('height', '100%');
            pattern.setAttribute("patternContentUnits","objectBoundingBox");
            //패턴안의 img태그 생성
            const image = document.createElementNS('http://www.w3.org/2000/svg', 'image');
            image.setAttribute('x', '0');
            image.setAttribute('y', '0');
            image.setAttribute('width', '1');
            image.setAttribute('height', '1');
            image.setAttribute("preserveAspectRatio", "xMidYMid slice");
            image.setAttribute('xmlns:xlink', "http://www.w3.org/1999/xlink");
            image.setAttribute('xlink:href', url);

            //패턴에 이미지태그 넣고 dfs에 패턴 넣는다
            pattern.appendChild(image);
            defs.appendChild(pattern);
        });
    }
}
// 1초마다 이미지 변경하는 함수
//let currentIndex = 0;
//function changeImage() {
//    const russiaPaths = svg.querySelectorAll('.RUSSIA');
//    if (russiaPaths.length > 0) {
//        const currentImageUrl = imageUrls[currentIndex];
//
//
//        currentIndex = (currentIndex + 1) % imageUrls.length;
//    }
//        path.style.fill = `url(#image-pattern)`;
//}
//
//// 1초마다 이미지 변경 실행
//setInterval(changeImage, 1000);