pipeline {
    agent any

    tools {
        maven 'Maven 3.8'
        jdk 'JDK-25'
    }

    stages {
        stage('Run Automated Tests') {
            steps {
                script {
                    // Προσθήκη του goal surefire-report:report-only στο τέλος
                    def cmd = "mvn clean test surefire-report:report-only"

                    def selectedTag = params.TAGS
                    if (selectedTag != null && !selectedTag.contains("All tests") && selectedTag.trim() != "") {
                        cmd += " -Dgroups=\"${selectedTag}\""
                    }

                    bat cmd
                }
            }
        }
    }

    post {
        always {
            publishHTML(target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/site',
                reportFiles: 'surefire-report.html',
                reportName: 'Surefire Test Report'
            ])

            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
        }
        success {
            echo 'All test scenarios passed successfully!'
        }
        failure {
            echo 'Test execution completed with failures. Inspect reports for root causes.'
        }
    }
}