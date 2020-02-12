node {
  stage('Preparation'){
    git 'https://github.com/skfarhat/game-of-life'
  }
  stage('Build'){
    sh './gradlew build -x test'
  }
  stage('Test'){
    sh './gradlew test'
  }
}