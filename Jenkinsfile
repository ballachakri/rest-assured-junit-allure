pipeline {
    agent any

    // ✅ Auto-download & configure JDK 21
    tools {
        jdk 'JDK-21'
    }

    environment {
        GITHUB_TOKEN = credentials('GITHUB_TOKEN')
    }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Verify Java') {
            steps { bat 'java -version' }
        }

        stage('Run Tests') {
            steps { bat 'mvn.cmd clean test' }
        }

        // Install Allure locally → avoids PATH issues
        stage('Install Allure') {
            steps {
                bat 'npm install allure-commandline --prefix "%WORKSPACE%"'
            }
        }

        // Generate HTML report
        stage('Generate Report') {
            steps {
                bat '''
                    set ALLURE_BIN=%WORKSPACE%\\node_modules\\.bin\\allure.cmd
                    "%ALLURE_BIN%" generate target\\allure-results -o allure-report --clean
                '''
            }
        }

        // ✅ DEPLOY — SIMPLE & RELIABLE!
        stage('Deploy Report') {
            steps {
                bat '''
                    git config --global user.name "jenkins-bot"
                    git config --global user.email "jenkins@example.com"

                    cd allure-report

                    REM Create fresh gh-pages branch RIGHT HERE
                    git init
                    git checkout -b gh-pages

                    REM Add report files
                    git add .

                    REM Critical file for Allure
                    echo. > .nojekyll
                    git add .nojekyll

                    REM Commit and PUSH
                    git commit -m "Allure Report: Build #%BUILD_NUMBER%" --allow-empty
                    git remote add origin https://%GITHUB_TOKEN%@github.com/ballachakri/rest-assured-junit-allure.git
                    git push -u origin gh-pages --force
                '''
            }
        }
    }
}