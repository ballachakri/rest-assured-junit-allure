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
    }

    // ✅ PUBLISH WITH ALLURE PLUGIN — CORRECT SYNTAX!
    post {
        always {
            // ✅ PUBLISH ALLURE REPORT — Standard Plugin Syntax
            // This creates a CLICKABLE "Allure Report" link on your build page!
            allure([
                includeProperties: false,
                jdk: '',
                results: [
                    [path: 'target/allure-results']
                ]
            ])
        }
    }
}