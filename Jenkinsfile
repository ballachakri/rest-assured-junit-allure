pipeline {
    agent any

    // ✅ Auto-download & configure JDK 21
    tools {
        jdk 'JDK-21'
    }

    stages {
        // Step 1: Check out source code
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        // Step 2: Verify Java 21
        stage('Verify Java') {
            steps {
                bat 'java -version'
            }
        }

        // Step 3: Run tests → generates target/allure-results
        stage('Run Tests') {
            steps {
                bat 'mvn.cmd clean test'
            }
        }

        // Step 4: Install Allure CLI locally
        stage('Install Allure') {
            steps {
                bat 'npm install allure-commandline --prefix "%WORKSPACE%"'
            }
        }

        // Step 5: Generate HTML Report
        stage('Generate Allure Report') {
            steps {
                bat '''
                    set ALLURE_BIN=%WORKSPACE%\\node_modules\\.bin\\allure.cmd
                    "%ALLURE_BIN%" generate target\\allure-results -o allure-report --clean
                '''
            }
        }
    }

    // ✅ PUBLISH REPORT DIRECTLY IN JENKINS!
    post {
        always {
            // Archive the RAW test results
            archiveArtifacts artifacts: 'target/allure-results/**', fingerprint: true, allowEmptyArchive: true

            // ✅ Archive the FULL HTML REPORT → Clickable from Jenkins build page!
            archiveArtifacts artifacts: 'allure-report/**', fingerprint: true, allowEmptyArchive: true

            // ✅ Use JENKINS ALLURE PLUGIN → Displays report WITH SIDEBAR!
            // Requires: Allure Plugin installed in Jenkins
            allure includeProperties: false, jdk: '', results: [
                file(path: 'target/allure-results')
            ]
        }
    }
}