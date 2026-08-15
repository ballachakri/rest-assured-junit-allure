pipeline {
    // Run on any available Jenkins agent
    agent any

    // Global environment variables
    environment {
        // GitHub token stored as Jenkins credential (ID: GITHUB_TOKEN)
        // Used to authenticate and push report to gh-pages branch
        GITHUB_TOKEN = credentials('GITHUB_TOKEN')
    }

    tools {
        // ✅ AUTOMATICALLY DOWNLOAD & INSTALL JDK 21
        // Jenkins pulls Eclipse Temurin JDK 21 directly — no manual install needed!
        // JAVA_HOME is set automatically by Jenkins
        jdk 'JDK-21'
    }

    stages {
            // Step 1: Check out source code
            stage('Checkout') {
                steps { checkout scm }
            }

            // Step 2: Verify Java is configured correctly
            stage('Verify Java') {
                steps {
                    bat '''
                        echo JAVA_HOME = %JAVA_HOME%
                        "%JAVA_HOME%\\bin\\java.exe" -version
                    '''
                }
            }

            // Step 3: Run tests — already proven to work! ✅
            stage('Run Tests') {
                steps {
                    bat 'mvn.cmd clean test'
                }
            }

             // Step 1: Install Allure globally via NPM
                  stage('Install Allure') {
                      steps {
                          bat 'npm install -g allure-commandline'
                      }
                  }

                  // Step 2: Find Allure dynamically & Generate Report
                  stage('Generate Report') {
                      steps {
                          bat '''
                              @echo off
                              for /f "delims=" %%P in ('npm config get prefix') do set PREFIX=%%P

                              REM Try multiple possible Allure paths
                              if exist "%PREFIX%\\allure-commandline\\bin\\allure.cmd" (
                                  set ALLURE="%PREFIX%\\allure-commandline\\bin\\allure.cmd"
                              ) else if exist "%PREFIX%\\node_modules\\allure-commandline\\bin\\allure.cmd" (
                                  set ALLURE="%PREFIX%\\node_modules\\allure-commandline\\bin\\allure.cmd"
                              ) else (
                                  REM Fallback: user profile path
                                  set ALLURE="%USERPROFILE%\\AppData\\Roaming\\npm\\node_modules\\allure-commandline\\bin\\allure.cmd"
                              )

                              echo Using Allure: %ALLURE%
                              %ALLURE% generate target\\allure-results -o allure-report --clean
                          '''
                      }
                  }

                  // Step 3: Deploy report to GitHub Pages
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