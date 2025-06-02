pipeline {
    agent any

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'my-feature', url: 'https://github.com/Needded/eCommerceUdacityFork.git'
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Deploy Application') {
            steps {
                // Stop any running instance of your app (if needed)
                sh 'pkill -f "java -jar" || true'

                // Run the new jar
                sh 'nohup java -jar target/eCommerceApi.jar > app.log 2>&1 &'
            }
        }
    }
}
