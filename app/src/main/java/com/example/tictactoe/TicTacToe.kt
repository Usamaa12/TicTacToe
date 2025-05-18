package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class TicTacToe : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeGame()
        }
    }
}

enum class Player { X, O }

@Composable
fun TicTacToeGame() {
    var board by remember { mutableStateOf(List(3) { MutableList(3) { "" } }) }
    var currentPlayer by remember { mutableStateOf(Player.X) }
    var result by remember { mutableStateOf<Pair<String, String>?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Tic-Tac-Toe", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Board(board = board) { row, col ->
            if (board[row][col].isEmpty() && result == null) {
                board = board.mapIndexed { r, rowList ->
                    rowList.mapIndexed { c, cell ->
                        if (r == row && c == col) currentPlayer.name else cell
                    }.toMutableList()
                }
                if (checkWin(board, currentPlayer.name)) {
                    result = if (currentPlayer == Player.X) {
                        "Player X wins!" to "Player O loses!"
                    } else {
                        "Player O wins!" to "Player X loses!"
                    }
                } else if (board.flatten().none { it.isEmpty() }) {
                    result = "It's a draw!" to "No win & No loses"
                } else {
                    currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (result == null) {
            Text(
                text = "Current Player: ${currentPlayer.name}",
                fontSize = 24.sp,
                color = if (currentPlayer == Player.X) Color.Red else Color.Blue
            )
        } else {
            Text(
                text = result!!.first,
                fontSize = 24.sp,
                color = if (result!!.first.contains("X")) Color.Red else Color.Blue
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = result!!.second,
                fontSize = 24.sp,
                color = if (result!!.second.contains("X")) Color.Red else Color.Blue
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                board = List(3) { MutableList(3) { "" } }
                currentPlayer = Player.X
                result = null
            }) {
                Text("Restart Game")
            }
        }
    }
}

@Composable
fun Board(board: List<List<String>>, onCellClick: (Int, Int) -> Unit) {
    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .border(2.dp, Color.Black)
    ) {
        for (i in board.indices) {
            Row {
                for (j in board[i].indices) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .border(1.dp, Color.Black)
                            .clickable { onCellClick(i, j) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = board[i][j],
                            fontSize = 32.sp,
                            color = when (board[i][j]) {
                                "X" -> Color.Red
                                "O" -> Color.Blue
                                else -> Color.Black
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

fun checkWin(board: List<List<String>>, player: String): Boolean {
    // Check rows
    if (board.any { row -> row.all { cell -> cell == player } }) return true
    // Check columns
    if ((0..2).any { col -> board.all { row -> row[col] == player } }) return true
    // Check diagonals
    if ((0..2).all { i -> board[i][i] == player }) return true
    if ((0..2).all { i -> board[i][2 - i] == player }) return true
    return false
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TicTacToeGame()
}

