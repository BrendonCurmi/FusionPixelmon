@echo off
REM Compiles the jars

REM Clear build
rmdir /q /s builds

REM Create shadow jars
call gradlew clean shadowJar

REM Create builds folders and rename modules
mkdir builds
REM mkdir builds\core
REM mkdir builds\data
REM mkdir builds\sponge
REM mkdir builds\spigot
REM mkdir builds\forge

rename "fusionpixelmon-core\build" "core"
rename "fusionpixelmon-data\build" "data"
rename "fusionpixelmon-sponge\build" "sponge"
rename "fusionpixelmon-spigot\build" "spigot"

REM Move build to builds folder
move fusionpixelmon-core\core builds
move fusionpixelmon-data\data builds
move fusionpixelmon-sponge\sponge builds
move fusionpixelmon-spigot\spigot builds

REM Create build jars
call gradlew clean build

rename "fusionpixelmon-forge\build" "forge"

REM Get jar name from Spigot jar and rename Forge jar
setlocal EnableExtensions EnableDelayedExpansion
for /r builds\spigot\libs %%F in (*.jar) do (
set "str=%%~nxF"
set "name=!str:spigot=forge!"
cd fusionpixelmon-forge\forge\reobfShadowJar
rename "output.jar" "!name!"
cd ..\..\../
)
endlocal

move fusionpixelmon-forge\forge builds

call gradlew clean

pause
