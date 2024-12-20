import jetbrains.buildServer.configs.kotlin.BuildSteps
import jetbrains.buildServer.configs.kotlin.buildSteps.script

fun BuildSteps.pullRequestInspection() {
    script {
        name = "HELLO WORLD"
        scriptContent = """
            echo "Hello world!"
            echo "Environment: ${'$'}{AWS_ENVIRONMENT}"
        """.trimIndent()
    }
    script {
        name = "Install dependencies"
        scriptContent = """
            echo "Installs poetry dependencies"
            # poetry config virtualenvs.in-project true
            # poetry install
        """.trimIndent()
    }
    script {
        name = "CDK synth"
        scriptContent = """
            echo "For AWS CDK deployments"
            # poetry run deployment synth \
            #     -l python \
            #     -r "arn:aws:iam::${'$'}{AWS_ACCOUNT_ID}:role/teamcity-deployment" \
            #    -s "teamcity-deployment"
        """.trimIndent()
    }
}

fun BuildSteps.deploymentPipeline() {
    script {
        name = "HELLO WORLD"
        scriptContent = """
            echo "Hello world!"
            echo "Deploy to ${'$'}{AWS_ENVIRONMENT}"
        """.trimIndent()
    }
    script {
        name = "Install dependencies"
        scriptContent = """
            echo "Installs poetry dependencies"
            # poetry config virtualenvs.in-project true
            # poetry install
        """.trimIndent()
    }
    script {
        name = "CDK Deploy"
        scriptContent = """
            echo "For AWS CDK deployments"
            # poetry run deployment deploy \
            #     -l python \
            #     -r "arn:aws:iam::${'$'}{AWS_ACCOUNT_ID}:role/teamcity-deployment" \
            #     -s "teamcity-deployment"
        """.trimIndent()
    }
}
