package flashcards

fun main() {
    val game = Flashcards()
    game.start()
}

class Flashcards {
    private val cardsList = mutableMapOf<String, String>()
    private var numCards = 0

    init {
        println("Input the number of cards:")
        numCards = readln().toInt()
    }

    fun start() {
        promptUser()
    }

    private fun promptUser() {
        // fill in the cardsList
        for (i in 1..numCards){
            println("Card #$i:")
            val card = readln()
            println("The definition for card #$i:")
            val definition = readln()
            cardsList += card to definition
        }

        // spit out the cardsList
        cardsList.forEach { (k,v) ->
            println("Print the definition of \"$k\":")
            val answer = readln()
            if (answer == v) {
                println("Correct!")
            } else {
                println("Wrong. The right answer is \"$v\".")
            }
        }
    }
}
