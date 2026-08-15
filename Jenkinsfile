pipeline {
    agent any

    // ✅ AUTOMATICALLY DOWNLOAD & INSTALL JDK 21
    // Jenkins pulls Eclipse Temurin JDK 21 directly — no manual install needed!
    // JAVA_HOME is set automatically by Jenkins
    tools {
        jdk 'JDK-21'
    }

    environment {
        // GitHub token stored as Jenkins credential (ID: GITHUB_TOKEN)
        GITHUB_TOKEN = credentials('GITHUB_TOKEN')
    }

    stages {
        // Step 1: Check out source code from GitHub repository
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        // Step 2: Verify Java 21 installed correctly
        stage('Verify Java') {
            steps {
                bat 'java -version'
            }
        }

        // Step 3: Run tests with Maven
        // Uses JDK-21 from tools section → JAVA_HOME set automatically
        stage('Run Tests') {
            steps {
                bat 'mvn.cmd clean test'
            }
        }

        // Step 4: Install Allure LOCALLY into workspace
        // Avoids ALL system profile / PATH issues on Windows
        stage('Install Allure') {
            steps {
                bat 'npm install allure-commandline --prefix "%WORKSPACE%"'
            }
        }

        // Step 5: Generate Allure HTML Report
        // Uses fixed local path → guaranteed to exist after install
        stage('Generate Report') {
            steps {
                bat '''
                    set ALLURE_BIN=%WORKSPACE%\\node_modules\\.bin\\allure.cmd
                    echo Using Allure at: %ALLURE_BIN%
                    if exist "%ALLURE_BIN%" (
                        "%ALLURE_BIN%" generate target\\allure-results -o allure-report --clean
                    ) else (
                        echo ERROR: Allure not found!
                        exit 1
                    )
                '''
            }
        }

        // Step 6: Deploy report to GitHub Pages
        stage('Deploy Report') {
            steps {
                bat '''
                    git config --global user.name "jenkins-bot"
                    git config --global user.email "jenkins@example.com"

                    git clone --depth=1 --branch=gh-pages https://%GITHUB_TOKEN%@github.com/ballachakri/rest-assured-junit-allure.git gh-pages-repo || mkdir gh-pages-repo
                    cd gh-pages-repo
                    if exist *.* (
                        del /f /s /q *.* 2>nul
                    )
                    xcopy ..\\allure-report\\* . /E /I /Y
                    echo. > .nojekyll
                    git add .
                    git commit -m "Allure Report: Build #%BUILD_NUMBER%" || exit 0
                    git push https://%GITHUB_TOKEN%@github.com/ballachakri/rest-assured-junit-allure.git gh-pages
                '''
            }
        }
    }
}