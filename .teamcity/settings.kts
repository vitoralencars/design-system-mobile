import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.buildFeatures.provideAwsCredentials
import jetbrains.buildServer.configs.kotlin.triggers.finishBuildTrigger
import jetbrains.buildServer.configs.kotlin.BuildSteps
import jetbrains.buildServer.configs.kotlin.buildSteps.script

project {

    /*Pull Request Subproject*/
    val pullRequestProject = subProject {
        id("PullRequest")
        name = "Pull Request"

        buildType(PullRequestInspection)
    }

    /*Deployment Subproject*/
    val deploymentProject = subProject {
        id("Deployment")
        name = "Deployment"

        /*Defines dependencies*/
        val buildChain = sequential {
            buildType(DeployToTesting)
            buildType(DeployToAcceptance)
            buildType(DeployToProduction)
        }.buildTypes()

        buildChain.forEach { buildType(it) }

        buildTypesOrder = buildChain
    }

    subProjectsOrder = arrayListOf(pullRequestProject, deploymentProject)
}
/***************************************************************************************************/
/***************************************************************************************************/
/***************************************************************************************************/
/*** Pull Request Inspection Pipeline ***/
object PullRequestInspection : BuildType({
    name = "Pull request inspection"

    vcs {
        root(DslContext.settingsRoot)
    }

    features {
        commitStatusPublisher {
            vcsRootExtId = "${DslContext.settingsRoot.id}"
            publisher = github {
                githubUrl = "https://api.github.com"
                authType = personalToken {
                    token = "credentialsJSON:5b8d9044-9abd-4183-bfd0-f8d9fbb2c179"
                }
            }
        }
        provideAwsCredentials {
            awsConnectionId = "TestingTeamcityDeployment"
        }
    }

    params {
        param("env.AWS_ACCOUNT_ID", "TO_COMPLETE")
        param("env.AWS_ENVIRONMENT", "testing")
    }

    steps {
        pullRequestInspection()
    }

    requirements {
        contains("teamcity.agent.name", "testing-linux-amzn2023-python")
    }
})
/***************************************************************************************************/
/***************************************************************************************************/
/***************************************************************************************************/
/*** Deploy to testing Pipeline ***/
object DeployToTesting : BuildType({
    name = "Deploy to Testing"

    vcs {
        root(DslContext.settingsRoot)
    }

    features {
        provideAwsCredentials {
            awsConnectionId = "TestingTeamcityDeployment"
        }
    }

    triggers {
        finishBuildTrigger {
            buildType = "${PullRequestInspection.id}"
            successfulOnly = true
        }
    }

    params {
        param("env.AWS_ACCOUNT_ID", "TO_COMPLETE")
        param("env.AWS_ENVIRONMENT", "testing")
    }

    steps {
        deploymentPipeline()
    }

    requirements {
        contains("teamcity.agent.name", "testing-linux-amzn2023-python")
    }
})
/***************************************************************************************************/
/***************************************************************************************************/
/***************************************************************************************************/
/*** Deploy to acceptance Pipeline ***/
object DeployToAcceptance : BuildType({
    name = "Deploy to Acceptance"

    vcs {
        root(DslContext.settingsRoot)
    }

    features {
        provideAwsCredentials {
            awsConnectionId = "AcceptanceTeamcityDeployment"
        }
    }

    triggers {
        finishBuildTrigger {
            buildType = "${DeployToTesting.id}"
            successfulOnly = true
        }
    }

    params {
        param("env.AWS_ACCOUNT_ID", "TO_COMPLETE")
        param("env.AWS_ENVIRONMENT", "acceptance")
    }

    steps {
        script {
            name = "Testing step"
            scriptContent = """
                echo 'Hello world!'
                echo "Deploy to ${'$'}{AWS_ENVIRONMENT}"
            """.trimIndent()
        }
    }

    requirements {
        contains("teamcity.agent.name", "acceptance-linux-amzn2023-python")
    }
})
/***************************************************************************************************/
/***************************************************************************************************/
/***************************************************************************************************/
/*** Deploy to production Pipeline ***/
object DeployToProduction : BuildType({
    name = "Deploy to Production"

    vcs {
        root(DslContext.settingsRoot)
    }

    features {
        provideAwsCredentials {
            awsConnectionId = "ProductionTeamcityDeployment"
        }
    }

    triggers {
        finishBuildTrigger {
            buildType = "${DeployToAcceptance.id}"
            successfulOnly = true
        }
    }

    params {
        param("env.AWS_ENVIRONMENT", "production")
    }

    steps {
        script {
            name = "Testing step"
            scriptContent = """
                echo 'Hello world!'
                echo "Deploy to ${'$'}{AWS_ENVIRONMENT}"
            """.trimIndent()
        }
    }

    requirements {
        contains("teamcity.agent.name", "production-linux-amzn2023-python")
    }
})
