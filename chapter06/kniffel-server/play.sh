#!/bin/sh

GAME_CREATE=$(curl "http://localhost:8080/api/v1/game/" -d '{"playerNames": ["oli","mike"]}' -H "Content-Type: application/json" -s | jq)

echo "$GAME_CREATE"

GAME_ID=$(echo "$GAME_CREATE" | jq -r '.gameId')

RUNNING=true

while "$RUNNING" = "true"; do
    ROLL_ROUND=0
    while [ $ROLL_ROUND -ne 3 ]; do
      curl "http://localhost:8080/api/v1/game/$GAME_ID" -s | jq

      echo "Enter dice to keep: (comma separated) "
      read -r data

      ROLL_RESPONSE=$(curl "http://localhost:8080/api/v1/game/$GAME_ID/roll" -X POST -d "{\"diceToKeep\": [$data]}" -H "Content-Type: application/json" -s)
      ROLL_ROUND=$(echo "$ROLL_RESPONSE" | jq -r '.rollRound')
    done

    curl "http://localhost:8080/api/v1/game/$GAME_ID" -s | jq

    echo "Enter booking type: "
    read -r data

    curl "http://localhost:8080/api/v1/game/$GAME_ID/book" -X POST -d "{\"bookingType\": \"$data\"}" -H "Content-Type: application/json" -s | jq

done
