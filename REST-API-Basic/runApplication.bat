call gradlew.bat clean
call gradlew.bat build
copy build\libs %CATALINA_HOME%\webapps
cd %CATALINA_HOME%\bin
startup