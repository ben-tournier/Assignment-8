@echo off
echo ===============================================
echo    LIBRARY MANAGEMENT SYSTEM - RUN TESTS
echo ===============================================
echo.

if not exist "build" mkdir build

javac -d build src/*.java

if %ERRORLEVEL% EQU 0 (
    java -cp build FineCalculatorTest
) else (
    echo Compilation failed! Please check the error messages above.
)

echo.
echo Press any key to exit...
pause >nul
