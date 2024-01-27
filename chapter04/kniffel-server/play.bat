@echo off
setlocal enabledelayedexpansion

for /f %%i in ('curl http://localhost:8080/api/v1/game/ -d "{\"playerNames\": [\"oli\",\"mike\"]}" -H "Content-Type: application/json"') do set GAME_CREATE=%%i

echo %GAME_CREATE%

for /f %%i in ('"echo %GAME_CREATE%" ^| jq -r .gameId') do set "GAME_ID=%%i"

set RUNNING=true

:while_loop
if "!RUNNING!"=="true" (
    set ROLL_ROUND=0
    :roll_round
    if !ROLL_ROUND! neq 3 (
        curl http://localhost:8080/api/v1/game/%GAME_ID% -s | jq

        set /p DATA="Enter dice to keep: (comma separated)"

        for /f %%i in ('curl http://localhost:8080/api/v1/game/%GAME_ID%/roll -X POST -d "{\"diceToKeep\": [!DATA!]}" -H "Content-Type: application/json" -s ^| jq -r ".rollRound"') do set ROLL_ROUND=%%i

        goto :roll_round
    )

    curl http://localhost:8080/api/v1/game/%GAME_ID% -s | jq

    set /p DATA="Enter booking type:"

    curl http://localhost:8080/api/v1/game/%GAME_ID%/book -X POST -d "{\"bookingType\": \"!DATA!\"}" -H "Content-Type: application/json" -s | jq

    goto :while_loop
)
