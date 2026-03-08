pipeline {

```
agent any

environment {
    DOCKER_IMAGE = "htetkyawswarlinn/drone-delivery"
}

stages {

    stage('Checkout Source') {
        steps {
            git branch: 'main', url: 'https://github.com/Chr1s26/DroneDeliverySystem.git'
        }
    }

    stage('Build JAR') {
        agent {
            docker {
                image 'maven:3.9.9-eclipse-temurin-21'
                args '-v $HOME/.m2:/root/.m2'
            }
        }
        steps {
            sh 'mvn clean package -DskipTests'
        }
    }

    stage('Build Docker Image') {
        steps {
            sh 'docker build -t $DOCKER_IMAGE .'
        }
    }

    stage('Login DockerHub') {
        steps {
            withCredentials([usernamePassword(
                credentialsId: 'docker-hub-credentials',
                usernameVariable: 'DOCKER_USER',
                passwordVariable: 'DOCKER_PASS'
            )]) {
                sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
            }
        }
    }

    stage('Push Docker Image') {
        steps {
            sh 'docker push $DOCKER_IMAGE'
        }
    }

}

post {
    success {
        echo '✅ Build and push successful!'
    }
    failure {
        echo '❌ Pipeline failed'
    }
}
```

}
