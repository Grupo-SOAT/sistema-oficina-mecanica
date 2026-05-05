@echo off
cd /d "C:\Users\Gustavo Vieira\Documents\Github\sistema-oficina-mecanica"
call mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local > app.log 2>&1
