<script setup lang="ts">
import { ref } from 'vue';

interface PlayerInformation {
    index: number;
    name: string;
}

interface PlayerData {
    name: string;
    score: number;
}

interface GameData {
    gameId: string;
    currentPlayerName: string;
    playerData: PlayerData[];
    state: string;
    rollRound: number;
    diceRolls: number[];
    availableBookingTypes: string[];
    usedBookingTypes: string[];
}

const names = ref<PlayerInformation[]>([{index: 0, name: ''}, {index: 1, name: ''}]);
const gameData = ref<GameData>({ gameId: '', currentPlayerName: '', playerData: [], state: '', rollRound: 0, diceRolls: [], availableBookingTypes: [], usedBookingTypes: []});
const rerollSelection = ref([false, false, false, false, false]);
// the booking type selected in the dropdown box
const selectedBookingType = ref('');

async function createGame() {
    try {
        const response = await fetch('http://localhost:8080/api/v1/game/', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                playerNames: names.value.map(n => n.name)
            })
        });
        if (response.status > 299) {
            alert('Error creating game');
            return;
        }
        gameData.value = await response.json();
    } catch (e) {
        alert('Error creating game : ' + JSON.stringify(e));
    }
}

async function reroll() {
    const diceToKeep : number[] = [];
    for (let i = 0; i < gameData.value.diceRolls.length; i++) {
        if (rerollSelection.value[i]) {
            diceToKeep.push(gameData.value.diceRolls[i]);
        }
    }
    try {
        const response = await fetch(`http://localhost:8080/api/v1/game/${gameData.value.gameId}/roll`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({diceToKeep})
        });
        if (response.status !== 200) {
            alert('Error creating game');
            return;
        }
        gameData.value = await response.json();
        selectedBookingType.value = '';
        for (let i = 0; i < gameData.value.diceRolls.length; i++) {
            const idxToKeep = diceToKeep.indexOf(gameData.value.diceRolls[i]);
            if (idxToKeep === -1) {
                rerollSelection.value[i] = false;
            } else {
                rerollSelection.value[i] = true;
                diceToKeep.splice(idxToKeep, 1);
            }
        }
    } catch (e) {
        alert('Error rerolling dice : ' + JSON.stringify(e));
    }
}

// simple REST API call to send the booking type
async function book() {
    try {
        const response = await fetch(`http://localhost:8080/api/v1/game/${gameData.value.gameId}/book`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({bookingType: selectedBookingType.value})
        });
        if (response.status !== 200) {
            alert('Error creating game');
            return;
        }
        gameData.value = await response.json();
        rerollSelection.value = [false, false, false, false, false];
    } catch (e) {
        alert('Error selecting booking category : ' + JSON.stringify(e));
    }
}

</script>

<template>
    <div v-if="!gameData.gameId">
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
    <div v-if="gameData.gameId">
        <h1>Game Scores</h1>
        <ul>
            <li v-for="ply in gameData.playerData" :key="ply.name">
                Player {{ ply.name }} - Score: {{ ply.score }}
            </li>
        </ul>
        <h1 class="mt-20">
            Current player: {{ gameData.currentPlayerName }}
        </h1>
    </div>
    <div v-if="gameData.state === 'ROLL'">
        <h3>Roll round: {{ gameData.rollRound }}</h3>
        <div> These types are still available:
            {{ gameData.availableBookingTypes }}
        </div>
        <h3 style="margin-top: 30px;">Select the dice to keep:</h3>
          <ul>
              <li v-for="(die, idx) in gameData.diceRolls" :key="idx">
                  {{ die }} <input type="checkbox" v-model="rerollSelection[idx]" />
              </li>
          </ul>
        <button @click="reroll">Roll</button>
    </div>
    <div v-if="gameData.state === 'BOOK'">
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