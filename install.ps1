$ErrorActionPreference = "Stop"

$Repo = "Open-Spring-Labs/spring-artisan"
$InstallDir = "$env:USERPROFILE\.spring-artisan"

Write-Host "Installing Spring Artisan..."

# Get latest release version
$Release = Invoke-RestMethod "https://api.github.com/repos/$Repo/releases/latest"
$Version = $Release.tag_name -replace '^v', ''

if (-not $Version) {
    Write-Error "Could not determine latest version."
    exit 1
}

Write-Host "Latest version: v$Version"

# Download JAR
New-Item -ItemType Directory -Force -Path $InstallDir | Out-Null
$JarUrl = "https://github.com/$Repo/releases/download/v$Version/spring-artisan.jar"
Invoke-WebRequest -Uri $JarUrl -OutFile "$InstallDir\spring-artisan.jar"

# Create wrapper batch file
@"
@echo off
java -jar "%USERPROFILE%\.spring-artisan\spring-artisan.jar" %*
"@ | Set-Content "$InstallDir\spring-artisan.bat"

# Add to user PATH if not already there
$CurrentPath = [Environment]::GetEnvironmentVariable("PATH", "User")
if ($CurrentPath -notlike "*$InstallDir*") {
    [Environment]::SetEnvironmentVariable("PATH", "$CurrentPath;$InstallDir", "User")
    Write-Host "Added $InstallDir to PATH."
    Write-Host "Restart your terminal for the PATH change to take effect."
}

Write-Host ""
Write-Host "Spring Artisan v$Version installed successfully!"
Write-Host "Run: spring-artisan make model User"
