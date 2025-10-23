@echo off
REM ==== 설정 ====
set "MYSQL=C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
REM PATH에 mysql이 있으면 위 줄을 set "MYSQL=mysql" 로 바꿔도 됩니다.
set HOST=localhost
set USER=root
set PASS=1234
set DB=project_smartfarm

REM ==== bat 파일 위치 기준으로 이동 ====
cd /d "%~dp0"

REM ==== SQL 경로 ====
set SQL_DIR=src\sql

REM 1) DB 생성 (DB 미지정 실행)
echo [1/4] create_database
"%MYSQL%" -h %HOST% -u %USER% -p%PASS% < "%SQL_DIR%\project_smartfarm_create_database.sql" || goto :err

REM 2) DB 존재/접속 확인
"%MYSQL%" -h %HOST% -u %USER% -p%PASS% -e "USE %DB%;" || goto :no_db

REM 3) 테이블 생성
echo [2/4] create_table
"%MYSQL%" -h %HOST% -u %USER% -p%PASS% < "%SQL_DIR%\project_smartfarm_create_table.sql" || goto :err

REM 4) 사양 데이터
echo [3/4] insert_device_specs
"%MYSQL%" -h %HOST% -u %USER% -p%PASS% < "%SQL_DIR%\project_smartfarm_insert_device_specs.sql" || goto :err

REM 5) 디바이스 데이터
echo [4/4] insert_devices
"%MYSQL%" -h %HOST% -u %USER% -p%PASS% < "%SQL_DIR%\project_smartfarm_insert_devices.sql" || goto :err

echo 완료
goto :end

:no_db
echo 지정한 DB(%DB%)에 USE 실패. DB명 확인 필요.
goto :end

:err
echo 오류 발생. 직전 단계 로그 확인.
:end
pause
