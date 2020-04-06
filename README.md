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

### 전체
![Industry_Project_FrameWork](https://github.com/kor-Randy/OurHealth_Industry/blob/master/Img/Industry_Project_FrameWork.PNG)

### App에서 S3 연동 및 파일 저장

* Client가 칼로리/단백질 등 영양소를 알고 싶은 Image File을 S3에 업로드
![Industry_Project_AppToS3](https://github.com/kor-Randy/OurHealth_Industry/blob/master/Img/Industry_Project_AppToS3.PNG)


### SageMaker에서 S3에 접근하여 이미지 파일을 기반으로 학습 및 S3에 업로드된 사진을 분석

* 이미 학습된 SageMaker 모델이 Client가 업로드한 이미지 파일에 접근하여 분석
![Industry_Project_SageMakerToS3](https://github.com/kor-Randy/OurHealth_Industry/blob/master/Img/Industry_Project_SageMakerToS3.PNG)


### Client가 등록한 사진에 대한 결과값 도출

* Client가 S3에 파일을 업로드
* Client가 Lambda를 통해 HTTP 통신
* Lambda에서 S3에 파일을 Rekognition을 통해 전처리
  * 이때 저장되어있는 결과값 ex) Sushi, Hamburger 등의 결과가 나오면 그대로 결과값 도출하여 속도 개선
  * 만약 음식물로 결과값을 도출해내지 못하면 SageMaker를 통해 결과값 도출
* 전처리를 통해 속도를 개선하였지만, 학습량이 많지 않아 좋지 않은 정확도를 가진 모델
  * 해결방법 : 음식이름이 잘못 도출되면 User에게 입력을 받아 정확한 결과값을 저장해놓은 뒤, 일정량의 파일들이 S3에 축적되면 모델을 재학습
![Industry_Project_AppToSagemaker](https://github.com/kor-Randy/OurHealth_Industry/blob/master/Img/Industry_Project_AppToSagemaker.PNG)


### 

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
