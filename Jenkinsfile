pipeline {
    agent any

    environment {
        ALLURE_VERSION = '2.29.1'
        GITHUB_TOKEN = credentials('GITHUB_TOKEN')
    }

    stages {
        // Step 1: Check out code
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        // Step 2: Run tests with Maven (Windows uses mvn.cmd)
        stage('Run Tests') {
            steps {
                bat 'mvn.cmd clean test'
            }
        }

        // Step 3: Download & Install Allure CLI (Windows version)
        stage('Install Allure') {
            steps {
                bat '''
                    curl -L -o allure.zip https://github.com/allure-framework/allure2/releases/download/%ALLURE_VERSION%/allure-%ALLURE_VERSION%.zip
                    tar -xf allure.zip
                    set PATH=%CD%\\allure-%ALLURE_VERSION%\\bin;%PATH%
                '''
            }
        }

        // Step 4: Generate Allure HTML Report
        stage('Generate Report') {
            steps {
                bat '''
                    set PATH=%CD%\\allure-%ALLURE_VERSION%\\bin;%PATH%
                    allure.bat generate target\\allure-results -o allure-report --clean
                '''
            }
        }

        // Step 5: Deploy report to GitHub Pages
        stage('Deploy Report') {
            steps {
                bat '''
                    git config --global user.name "jenkins-bot"
                    git config --global user.email "jenkins@example.com"

                    REM Clone or create gh-pages folder
                    git clone --depth=1 --branch=gh-pages https://%GITHUB_TOKEN%@github.com/ballachakri/rest-assured-junit-allure.git gh-pages-repo || mkdir gh-pages-repo
                    cd gh-pages-repo

                    REM Clear old files
                    del /f /s /q *.* 2>nul
                    rmdir /s /q . 2>nul

                    REM Copy new report files
                    xcopy ..\\allure-report\\* . /E /I /Y

                    REM Create .nojekyll file
                    echo. > .nojekyll

                    REM Commit and push
                    git add .
                    git commit -m "Allure Report: Build #%BUILD_NUMBER%" || exit 0
                    git push https://%GITHUB_TOKEN%@github.com/ballachakri/rest-assured-junit-allure.git gh-pages
                '''
            }
        }
    }
}