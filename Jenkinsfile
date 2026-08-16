# ==================================================
#  Build & Deploy Allure Test Report to GitHub Pages
# ==================================================
# Triggers:
#   - Push commits to main / develop / master
#   - Pull requests targeting those branches
#   - Manual run from Actions tab (workflow_dispatch)
# Output:
#   - Allure HTML Report published to GitHub Pages
# ==================================================

name: Build & Deploy Allure Report

# Define when this workflow should run
on:
  # Auto-run when code is pushed to these branches
  push:
    branches: [ "main", "develop", "master" ]
  
  # Auto-run on pull requests to these branches
  pull_request:
    branches: [ "main", "develop", "master" ]
  
  # Allow manual trigger via Actions tab (Run workflow button)
  workflow_dispatch:

jobs:
  # Single job: Build → Test → Generate Report → Deploy
  build-and-deploy:
    # Use Ubuntu latest runner (fastest & most compatible)
    runs-on: ubuntu-latest
    
    # ==================================================
    # CRITICAL PERMISSION: Give GITHUB_TOKEN write access
    # Without this → "not found deploy key or tokens" error
    # ==================================================
    permissions:
      contents: write

    steps:
      # ==================================================
      # Step 1: Download source code from repository
      # ==================================================
      - name: Checkout Code
        uses: actions/checkout@v4

      # ==================================================
      # Step 2: Install & configure Java 21 (Eclipse Temurin)
      # Matches your project's maven.compiler.release setting
      # ==================================================
      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'

      # ==================================================
      # Step 3: Run tests with Maven
      # Generates raw test result files in: target/allure-results/
      # ==================================================
      - name: Run Tests
        run: mvn clean test

      # ==================================================
      # Step 4: Install Allure Commandline
      # ✅ DIRECT DOWNLOAD from GitHub (FAST ~15 seconds)
      # ❌ Replaced slow NPM install (10+ minutes / hangs)
      # ==================================================
      - name: Install Allure CLI
        run: |
          ALLURE_VERSION=2.29.0
          echo "Downloading Allure v${ALLURE_VERSION}..."
          curl -fsSL https://github.com/allure-framework/allure2/releases/download/${ALLURE_VERSION}/allure-${ALLURE_VERSION}.tgz -o allure.tgz
          tar -xzf allure.tgz
          sudo mv allure-${ALLURE_VERSION} /opt/allure
          sudo ln -sf /opt/allure/bin/allure /usr/local/bin/allure
          echo "✅ Allure installed successfully!"
          allure --version

      # ==================================================
      # Step 5: Generate HTML Report
      # Reads raw test results → Creates styled report in: allure-report/
      # ==================================================
      - name: Generate Allure Report
        run: allure generate target/allure-results -o allure-report --clean

      # ==================================================
      # Step 6: Deploy Report to GitHub Pages
      # Pushes allure-report/ folder → gh-pages branch
      # enable_jekyll: false = Auto-adds .nojekyll file
      # → CRITICAL: Ensures Allure CSS/JS/sidebar works correctly!
      # ==================================================
      - name: Deploy to GitHub Pages
        uses: peaceiris/actions-gh-pages@v4
        with:
          github_token: ${{ secrets.GITHUB_TOKEN }}  # Auto-provided by GitHub — NO manual token needed!
          publish_dir: ./allure-report                  # Folder containing HTML report files
          enable_jekyll: false                           # Disable Jekyll → Allure sidebar WORKS ✅