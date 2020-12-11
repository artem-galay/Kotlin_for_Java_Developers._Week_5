package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
        GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {

    private val board: GameBoard<Int?> = createGameBoard(4)

    override fun canMove(): Boolean {
        return true
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }

    override fun initialize() {
        board.addValues(initializer)
    }

    override fun processMove(direction: Direction) {
        board.moveValuesFifteen(direction)
    }

    override fun hasWon(): Boolean {
        val allSet = board.getAllCells()
        var fg = 0
        for (i in 1 until 16) {
            if(board[allSet.elementAt(i-1)] != i)
                fg++
        }
        return (fg == 0)
    }

}

/**
 * Move values by the rules of the game of fifteen to the specified direction.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValuesFifteen(direction: Direction) {

    val emptyCells = this.getAllCells().filter { this[it] == null }

    when (direction) {
        Direction.LEFT -> {

            val rowToBeUpdated = this.getCell(emptyCells[0].i, emptyCells[0].j + 1)
            swapValues(rowToBeUpdated, emptyCells)
        }
        Direction.RIGHT -> {
            val rowToBeUpdated = this.getCell(emptyCells[0].i, emptyCells[0].j - 1)
            swapValues(rowToBeUpdated, emptyCells)
        }
        Direction.DOWN -> {
            val rowToBeUpdated = this.getCell(emptyCells[0].i - 1, emptyCells[0].j)
            swapValues(rowToBeUpdated, emptyCells)
        }
        Direction.UP -> {
            val rowToBeUpdated = this.getCell(emptyCells[0].i + 1, emptyCells[0].j)
            swapValues(rowToBeUpdated, emptyCells)
        }
    }
}

private fun GameBoard<Int?>.swapValues(rowToBeUpdated: Cell, emptyCells: List<Cell>) {
    val temp = this[rowToBeUpdated]
    this[rowToBeUpdated] = this[emptyCells[0]]
    this[emptyCells[0]] = temp
}


private fun GameBoard<Int?>.addValues(initializer: GameOfFifteenInitializer) {
    val data = initializer.initialPermutation
    var k = 0
    for (i in 1..width) {
        for (j in 1..width) {
            var value: Int?
            if (k < 15) {
                value = data[k]
                k++
            } else {
                value = null
            }

            this[this.getCell(i, j)] = value

        }
    }

}
