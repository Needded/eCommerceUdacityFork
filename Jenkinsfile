pipeline {
    agent any

    environment {
        MAVEN_OPTS = "-Dmaven.repo.local=/var/lib/jenkins/.m2/repository"
    }

    stages {
        stage('Clone Repository') {
            steps {
                // Clone the Git repository
                git branch: 'my-feature', url: 'https://github.com/Needded/eCommerceUdacityFork.git'
            }
        }

        stage('Build with Maven') {
            steps {
                // Create Maven cache directory if it doesn't exist
                sh 'mkdir -p /var/lib/jenkins/.m2/repository'
                echo 'Using Maven cache directory'
                // Build the project
                sh 'mvn clean package'
            }
        }

        stage('Deploy Application') {
            steps {
                // Stop any running app
                sh 'pkill -f "java -jar" || true'
                // Start new app in background
                sh 'nohup java -jar target/eCommerceApi.jar > app.log 2>&1 &'
            }
        }
    }
}
