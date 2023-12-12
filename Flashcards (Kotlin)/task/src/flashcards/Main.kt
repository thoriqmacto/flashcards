package flashcards

fun main() {
    val game = Flashcards()
    game.start()
}

class Flashcards {
    private var promptCard = ""
    private var promptDefinition = ""

    fun start() {
        promptUser()
    }

    private fun promptUser() {
        println("Card:")
        promptCard = readln()
        println(promptCard)

        println("Definition:")
        promptDefinition = readln()
        println(promptDefinition)
    }
}
