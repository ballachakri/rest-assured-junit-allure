pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    environment {
        ALLURE_VERSION = '2.29.1'
        GITHUB_TOKEN = credentials('GITHUB_TOKEN')
    }

    stages {
        // Step 1: Check out code
        stage('Checkout') {
            steps { checkout scm }
        }

        // Step 2: Run tests → generates target/allure-results
        stage('Run Tests') {
            steps { sh 'mvn clean test' }
        }

        // Step 3: Install Allure CLI
        stage('Install Allure') {
            steps {
                sh '''
                    curl -sL https://github.com/allure-framework/allure2/releases/download/${ALLURE_VERSION}/allure-${ALLURE_VERSION}.tgz \
                    | tar -xz -C ${HOME}
                    export PATH=${HOME}/allure-${ALLURE_VERSION}/bin:$PATH
                '''
            }
        }

        // Step 4: Generate HTML report
        stage('Generate Report') {
            steps {
                sh '''
                    export PATH=${HOME}/allure-${ALLURE_VERSION}/bin:$PATH
                    allure generate target/allure-results -o allure-report --clean
                '''
            }
        }

        // Step 5: Deploy report to GitHub Pages
        stage('Deploy Report') {
            steps {
                sh '''
                    git config --global user.name "jenkins-bot"
                    git config --global user.email "jenkins@example.com"

                    git clone --depth=1 --branch=gh-pages https://${GITHUB_TOKEN}@github.com/ballachakri/rest-assured-junit-allure.git gh-pages-repo || mkdir -p gh-pages-repo
                    cd gh-pages-repo
                    rm -rf *
                    cp -r ../allure-report/* .
                    touch .nojekyll
                    git add .
                    git commit -m "Allure Report: Build #${BUILD_NUMBER}" || exit 0
                    git push https://${GITHUB_TOKEN}@github.com/ballachakri/rest-assured-junit-allure.git gh-pages
                '''
            }
        }
    }
}