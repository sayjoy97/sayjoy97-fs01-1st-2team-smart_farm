<<<<<<< HEAD
@echo off 
REM ==== ¼³Á¤ ====
set "MYSQL=C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
REM PATH¿¡ mysqlÀÌ ÀÖÀ¸¸é À§ ÁÙÀ» set "MYSQL=mysql" ·Î ¹Ù²ãµµ µË´Ï´Ù.
set HOST=localhost
set USER=root
set PASS=1234
set DB=project_smartfarm

REM ==== bat ÆÄÀÏ À§Ä¡ ±âÁØÀ¸·Î ÀÌµ¿ ====
cd /d "%~dp0"

REM ==== SQL °æ·Î ====
set SQL_DIR=src\sql

REM 1) DB »ı¼º (DB ¹ÌÁöÁ¤ ½ÇÇà)
echo [1/5] create_database
"%MYSQL%" -h %HOST% -u %USER% -p%PASS% < "%SQL_DIR%\project_smartfarm_create_database.sql" || goto :err

REM 2) DB Á¸Àç/Á¢¼Ó È®ÀÎ
"%MYSQL%" -h %HOST% -u %USER% -p%PASS% -e "USE %DB%;" || goto :no_db

REM 3) Å×ÀÌºí »ı¼º
echo [2/5] create_table
"%MYSQL%" -h %HOST% -u %USER% -p%PASS% < "%SQL_DIR%\project_smartfarm_create_table.sql" || goto :err

REM 4) »ç¾ç µ¥ÀÌÅÍ
echo [3/5] insert_device_specs
"%MYSQL%" -h %HOST% -u %USER% -p%PASS% < "%SQL_DIR%\project_smartfarm_insert_device_specs.sql" || goto :err

REM 5) µğ¹ÙÀÌ½º µ¥ÀÌÅÍ
echo [4/5] insert_devices
"%MYSQL%" -h %HOST% -u %USER% -p%PASS% < "%SQL_DIR%\project_smartfarm_insert_devices.sql" || goto :err

REM 6) ´õ¹Ì Å×½ºÆ® µ¥ÀÌÅÍ (»ç¿ëÀÚ, ÀÛ¹°, ³óÀå, ·Î±×)
echo [5/5] insert_dummy_data
"%MYSQL%" -h %HOST% -u %USER% -p%PASS% < "%SQL_DIR%\project_smartfarm_insert_dummy_data.sql" || goto :err

echo === ÀüÃ¼ ¿Ï·á ===
goto :end

:no_db
echo ÁöÁ¤ÇÑ DB(%DB%)¿¡ USE ½ÇÆĞ. DB¸í È®ÀÎ ÇÊ¿ä.
goto :end

:err
echo ¿À·ù ¹ß»ı. Á÷Àü ´Ü°è ·Î±× È®ÀÎ.
=======
@echo off
REM ==== ì„¤ì • ====
set "MYSQL=C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
REM PATHì— mysqlì´ ìˆìœ¼ë©´ ìœ„ ì¤„ì„ set "MYSQL=mysql" ë¡œ ë°”ê¿”ë„ ë©ë‹ˆë‹¤.
set HOST=localhost
set USER=root
set PASS=1234
set DB=project_smartfarm

REM ==== bat íŒŒì¼ ìœ„ì¹˜ ê¸°ì¤€ìœ¼ë¡œ ì´ë™ ====
cd /d "%~dp0"

REM ==== SQL ê²½ë¡œ ====
set SQL_DIR=src\sql

REM 1) DB ìƒì„± (DB ë¯¸ì§€ì • ì‹¤í–‰)
echo [1/4] create_database
"%MYSQL%" -h %HOST% -u %USER% -p%PASS% < "%SQL_DIR%\project_smartfarm_create_database.sql" || goto :err

REM 2) DB ì¡´ì¬/ì ‘ì† í™•ì¸
"%MYSQL%" -h %HOST% -u %USER% -p%PASS% -e "USE %DB%;" || goto :no_db

REM 3) í…Œì´ë¸” ìƒì„±
echo [2/4] create_table
"%MYSQL%" -h %HOST% -u %USER% -p%PASS% < "%SQL_DIR%\project_smartfarm_create_table.sql" || goto :err

REM 4) ì‚¬ì–‘ ë°ì´í„°
echo [3/4] insert_device_specs
"%MYSQL%" -h %HOST% -u %USER% -p%PASS% < "%SQL_DIR%\project_smartfarm_insert_device_specs.sql" || goto :err

REM 5) ë””ë°”ì´ìŠ¤ ë°ì´í„°
echo [4/4] insert_devices
"%MYSQL%" -h %HOST% -u %USER% -p%PASS% < "%SQL_DIR%\project_smartfarm_insert_devices.sql" || goto :err

echo ì™„ë£Œ
goto :end

:no_db
echo ì§€ì •í•œ DB(%DB%)ì— USE ì‹¤íŒ¨. DBëª… í™•ì¸ í•„ìš”.
goto :end

:err
echo ì˜¤ë¥˜ ë°œìƒ. ì§ì „ ë‹¨ê³„ ë¡œê·¸ í™•ì¸.
>>>>>>> refs/remotes/downstream/main
:end
pause
