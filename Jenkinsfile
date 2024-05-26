pipeline {
  agent any
  stages {
    stage('Checkout') {
      steps {
        echo 'Cloning repository...'
        git(branch: 'main', url: "${env.GIT_REPO_URL}")
        echo 'Cloning complete'
      }
    }

    stage('Build and Test') {
      steps {
        echo 'Building...'
        sh 'mvn -B clean package'
        echo 'Build complete!'
      }
    }

  }
  tools {
    maven 'jenkins-maven'
  }
  environment {
    GIT_REPO_URL = 'https://github.com/feramaro/simplified_picpay.git'
  }
  post {
    success {
      echo 'Pipeline passed!'
    }

    failure {
      echo 'Pipeline failed!'
    }

    always {
      archiveArtifacts(artifacts: 'target/*.jar', fingerprint: true)
    }

  }
}