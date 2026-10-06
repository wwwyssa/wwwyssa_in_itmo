$ErrorActionPreference = 'Stop'

$projectRoot = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path
$env:GRADLE_USER_HOME = Join-Path $projectRoot '.gradle-user-home'

Push-Location $projectRoot
try {
    & .\gradlew.bat war
    if ($LASTEXITCODE -ne 0) { throw 'Gradle build failed.' }

    $war = Join-Path $projectRoot 'build\libs\laba1-1.0-SNAPSHOT.war'
    $deployment = Join-Path $projectRoot 'wildfly\standalone\deployments\laba1.war'
    if (-not (Test-Path -LiteralPath (Split-Path $deployment))) {
        throw 'WildFly is missing from the project wildfly directory.'
    }
    Copy-Item -LiteralPath $war -Destination $deployment -Force
    Write-Host "Deployed $deployment"
} finally {
    Pop-Location
}
