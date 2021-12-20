# apiserver

# docker에 swagger 실행방법
  - docker에 swagger ui 이미지를 구동하여 구동된 container에 swagger json파일과 rest docs json파일을 특정 위치에 복사하여 swagger ui에 swagger api와 rest doc을 통합한다.

* docker를 설치 - OS에 따라 설치방법이 다름.

* swagger image를 docker에 pull
  * docker pull swaggerapi/swagger-ui

* docker에 받은 image를 실행
  * docker run -d -p 80:8080 -e SWAGGER_JSON=docs/swagger.json -e URLS_PRIMARY_NAME=SpringRestdocs -e URLS="[{ url: 'docs/swagger.json', name: 'Swagger' }, { url: 'docs/restdocs.json', name: 'SpringRestdocs' }]" -v g:\project\apiserver\docs:/usr/share/nginx/html/docs/ swaggerapi/swagger-ui

* 빌드 시 ".\gradlew clean build openapi3"
  * clean을 안할경우 빌드 실패가 간혹 발생
  * openapi를 통해서 rest docs을 json으로 변경함.

* copy .\build\api-spec\openapi3.json .\docs\restdocs.json
  * openapi로 생성된 json파일을 docker와 볼륨 바운딩(-v)된 폴더에 복사함.

* rest docs을 사용하는 이유
  * rest docs을 사용하기 위해서는 test코드를 작성해야 한다.
  * test code를 작성함에 따라 해당 api 함수를 검증할 수 있다.