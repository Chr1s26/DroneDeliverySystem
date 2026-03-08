pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "htetkyawswarlinn/drone-delivery"
    }

    stages {

        stage('Clone Repository') {
            steps {
                git 'https://github.com/Chr1s26/DroneDeliverySystem.git'
            }
        }

        stage('Build Jar') {
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
                    credentialsId: 'dockerhub',
                    usernameVariable: 'htetkyawswarlinn',
                    passwordVariable: 'Htetkyawswarlinn682'
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
}