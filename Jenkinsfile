node {
  stage('Preparation'){
    git 'https://github.com/skfarhat/oop-assign'
  }
  stage('Build'){
    sh './gradlew build -x test'
  }
  stage('Test'){
    sh './gradlew test'
  }
}