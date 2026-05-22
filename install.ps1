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

# Save installed version
Set-Content "$InstallDir\version" $Version

# Create wrapper batch file with built-in update and version support
@'
@echo off
set INSTALL_DIR=%USERPROFILE%\.spring-artisan
set REPO=Open-Spring-Labs/spring-artisan

if "%1"=="update" (
    echo Checking for updates...
    for /f "delims=" %%i in ('powershell -Command "(Invoke-RestMethod https://api.github.com/repos/%REPO%/releases/latest).tag_name -replace '^v', ''"') do set LATEST=%%i
    set /p CURRENT=<%INSTALL_DIR%\version
    if "%LATEST%"=="%CURRENT%" (
        echo Already on the latest version ^(v%CURRENT%^).
    ) else (
        echo Updating from v%CURRENT% to v%LATEST%...
        powershell -Command "Invoke-WebRequest -Uri https://github.com/%REPO%/releases/download/v%LATEST%/spring-artisan.jar -OutFile '%INSTALL_DIR%\spring-artisan.jar'"
        echo %LATEST%>%INSTALL_DIR%\version
        echo Updated to v%LATEST% successfully!
    )
    goto :eof
)

if "%1"=="--version" goto :version
if "%1"=="-v" goto :version
goto :run

:version
set /p VER=<%INSTALL_DIR%\version
echo Spring Artisan v%VER%
goto :eof

:run
java -jar "%INSTALL_DIR%\spring-artisan.jar" %*
'@ | Set-Content "$InstallDir\spring-artisan.bat"

# Add to user PATH if not already there
$CurrentPath = [Environment]::GetEnvironmentVariable("PATH", "User")
if ($CurrentPath -notlike "*$InstallDir*") {
    [Environment]::SetEnvironmentVariable("PATH", "$CurrentPath;$InstallDir", "User")
    Write-Host "Added $InstallDir to PATH."
    Write-Host "Restart your terminal for the PATH change to take effect."
}

Write-Host ""
Write-Host "Spring Artisan v$Version installed successfully!"
Write-Host ""
Write-Host "Commands:"
Write-Host "  spring-artisan make model User     generate code"
Write-Host "  spring-artisan --version           show current version"
Write-Host "  spring-artisan update              update to latest version"
