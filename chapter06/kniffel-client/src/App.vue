<script setup lang="ts">
import { ref } from 'vue'
import type { Ref } from 'vue'
import createClient from 'openapi-fetch';
import type { components, paths } from '@/api/v1';

const client = createClient<paths>({ baseUrl: `${__API_URL__}` });

interface PlayerInformation {
    index: number;
    name: string;
}

const names : Ref<PlayerInformation[]> = ref([{index: 0, name: ''}, {index: 1, name: ''}]);
const gameData : Ref<components["schemas"]["GameResponse"]|undefined> = ref();
const rerollSelection = ref([false, false, false, false, false]);
// the booking type selected in the dropdown box
const selectedBookingType : Ref<components["schemas"]["GameResponse"]["usedBookingTypes"]|undefined> = ref();

async function createGame() {
    const { data } = await client.POST("/api/v1/game/", {
        body: {
            playerNames: names.value.map(n => n.name)
        }
    });
    gameData.value = data;
}

async function reroll() {
    const diceToKeep : number[] = [];
    if (gameData.value) {
        for (let i = 0; i < gameData.value.diceRolls.length; i++) {
            if (rerollSelection.value[i]) {
                diceToKeep.push(gameData.value.diceRolls[i]);
            }
        }
        const { data } = await client.POST("/api/v1/game/{gameId}/roll", {
            params: {
                path: {
                    gameId: gameData.value.gameId
                }
            },
            body: {
                diceToKeep
            }
        });
        gameData.value = data;
        selectedBookingType.value = undefined;
        if (gameData.value) {
            for (let i = 0; i < gameData.value.diceRolls.length; i++) {
                const idxToKeep = diceToKeep.indexOf(gameData.value.diceRolls[i]);
                if (idxToKeep === -1) {
                    rerollSelection.value[i] = false;
                } else {
                    rerollSelection.value[i] = true;
                    diceToKeep.splice(idxToKeep, 1);
                }
            }
        }
    }
}

// simple REST API call to send the booking type
async function book() {
  if (gameData.value) {
    const { data } = await client.POST("/api/v1/game/{gameId}/book", {
      params: {
        path: {
          gameId: gameData.value.gameId
        }
      },
      body: {
        bookingType: selectedBookingType.value
      }
    });
    gameData.value = data;
    rerollSelection.value = [false, false, false, false, false];
  }
}

</script>

<template>
    <div v-if="!gameData?.gameId">
        <h1>Player Names</h1>
        <form>
            <ul>
                <li v-for="ply in names" :key="ply.index">
                    Player {{ ply.index+1 }}'s name: <input type="text" v-model="ply.name" />
                </li>
            </ul>
        </form>
        <button @click="names.push({index: names.length, name: ''})">Add Name</button> &nbsp;
        <button @click="createGame">Create Game</button>
    </div>
    <div v-if="gameData?.gameId">
        <h1>Game Scores</h1>
        <ul>
            <li v-for="ply in gameData?.playerData" :key="ply.name">
                Player {{ ply.name }} - Score: {{ ply.score }}
            </li>
        </ul>
        <h1 class="mt-20">
            Current player: {{ gameData.currentPlayerName }}
        </h1>
    </div>
    <div v-if="gameData?.state === 'ROLL'">
        <h3>Roll round: {{ gameData?.rollRound }}</h3>
        <div> These types are still available:
            {{ gameData?.availableBookingTypes }}
        </div>
        <h3 style="margin-top: 30px;">Select the dice to keep:</h3>
          <ul>
              <li v-for="(die, idx) in gameData.diceRolls" :key="idx">
                  {{ die }} <input type="checkbox" v-model="rerollSelection[idx]" />
              </li>
          </ul>
        <button @click="reroll">Roll</button>
    </div>
    <div v-if="gameData?.state === 'BOOK'">
        <h1>Final dice rolls: {{  gameData.diceRolls }}</h1>
        <div class="mt-20">
          Select the booking type:
        </div>
        <select v-model="selectedBookingType">
            <option v-for="cat in gameData.availableBookingTypes" :key="cat" :value="cat">{{ cat }}</option>
        </select>
        <button @click="book">Book</button>
    </div>
</template>

<style scoped>
button,input {
    margin: 10px;
}
.mt-20 {
    margin-top: 20px;
}
</style>