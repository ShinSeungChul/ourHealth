# OurHealth_Industry

- - -

## 한국항공대학교 GS 네오텍과의 산학프로젝트

- - -

## 프로젝트 목표

* 앱을 이용하여 현실에서 건강을 챙길 수 있도록 도와준다.
* 음식을 사진 찍으면 음식마다 탄수화물, 단백질, 지방 등 영양소를 볼 수 있게 해준다.
* 당일 날 먹은 음식을 토대로 건강을 관리해준다.
* 자신의 식습관을 분석하여 앞으로 식습관을 어떤 방향으로 수정해야 하는지 알려준다.

- - -

## 사용 기술

* AWS Amplify, S3, DynamoDB, Cognito, SageMaker, Lambda, G/W, 
* SamSung Health
* Android

- - -

## 프레임 워크

![Industry_Project_FrameWork](https://github.com/kor-Randy/OurHealth_Industry/blob/master/Img/Industry_Project_FrameWork.PNG)

- - -

## 실행 영상

[![Video Label](http://img.youtube.com/vi/GxYhWtoAvMI/0.jpg)](https://youtu.be/GxYhWtoAvMI?t=0s)

- - - 

## UI

### Google의 OCR를 사용하여 사진 내의 한글 Text를 처리
#### AWS의 Rekognition을 사용하려 했지만 한글 지원이 되지 않아 찾아보던 중 Google의 OCR을 선택

![Industry_Project_Menu](https://github.com/kor-Randy/OurHealth_Industry/blob/master/Img/Industry_Project_Menu.PNG)

- - -

### AWS의 Rekognition으로 전처리를 하고 학습을 시킨 Sagemaker 모델로 후처리
#### Sagemaker만을 사용하려 했지만 학습량과 기대치에서 많은 차이가 보임.
#### Educate으로 받은 $100만으로는 학습량이 무리가 있어 전/후 처리를 각각 담당하게 개발

![Industry_Project_Photo](https://github.com/kor-Randy/OurHealth_Industry/blob/master/Img/Industry_Project_Photo.PNG)
