#  OurHealth
<img src="https://github.com/kor-Randy/OurHealth_Industry/blob/master/Img/Industry_Project_Icon.PNG" width="250" height="250" />

- - -

## 프로젝트 목표

* 앱을 이용하여 현실에서 건강을 챙길 수 있도록 도와준다.
* 음식을 사진 찍으면 음식마다 탄수화물, 단백질, 지방 등 영양소를 볼 수 있게 해준다.
* 당일 날 먹은 음식을 토대로 건강을 관리해준다.
* 자신의 식습관을 분석하여 앞으로 식습관을 어떤 방향으로 수정해야 하는지 알려준다.

- - -

<table>
   <tr>
     <th align="center">
       <img width="200" alt="1" src="https://user-images.githubusercontent.com/11826495/79061403-f42c3f80-7cca-11ea-901c-ccd0b3c338eb.gif"/>
       <br><br>[Eaten Food]
     </th>
     <th align="center">
       <img width="200" alt="1" src="https://user-images.githubusercontent.com/11826495/79061528-15416000-7ccc-11ea-8e20-e0e40e148bbc.gif"/>
       <br><br>[Take Picture] 
    </th>
     <th align="center">
      <img width="200" alt="1" src="https://user-images.githubusercontent.com/11826495/79061554-533e8400-7ccc-11ea-989b-82dc1a28d039.gif"/>
       <br><br>[InterpretPicture]
    </th>
     <th align="center">
      <img width="200" alt="1" src="https://user-images.githubusercontent.com/11826495/79061698-b2e95f00-7ccd-11ea-8b24-b0b4d281bf4c.gif"/>
       <br><br>[Parse MenuText]
    </th>
  </tr>
</table>

- - -

## 사용 기술

* AWS Amplify, S3, DynamoDB, Cognito, SageMaker, Lambda, G/W, 
* SamSung Health, OCR
* Android

- - - 

## UI

* Google의 OCR를 사용하여 사진 내의 한글 Text를 처리
* AWS의 Rekognition을 사용하려 했지만 한글 지원이 되지 않아 찾아보던 중 Google의 OCR을 선택

![Industry_Project_Menu](https://github.com/kor-Randy/OurHealth_Industry/blob/master/Img/Industry_Project_Menu.PNG)

- - -

### AWS의 Rekognition으로 전처리를 하고 학습을 시킨 Sagemaker 모델로 후처리
#### Sagemaker만을 사용하려 했지만 학습량과 기대치에서 많은 차이가 보임.
#### Educate으로 받은 $100만으로는 학습량이 무리가 있어 전/후 처리를 각각 담당하게 개발

![Industry_Project_Photo](https://github.com/kor-Randy/OurHealth_Industry/blob/master/Img/Industry_Project_Photo.PNG)
