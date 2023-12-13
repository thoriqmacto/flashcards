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
            // input Card
            println("Card #$i:")
            var card = readln()

            while (cardsList.containsKey(card)) {
                println("The term \"$card\" already exists. Try again:")
                card = readln()
            }

            // input definition
            println("The definition for card #$i:")
            var definition = readln()

            while (cardsList.containsValue(definition)){
                println("The definition \"$definition\" already exists. Try again:")
                definition = readln()
            }

            if (!cardsList.containsKey(card) && !cardsList.containsValue(definition)) {
                cardsList += card to definition
            }
        }

        // spit out the cardsList
        cardsList.forEach { (k,v) ->
            println("Print the definition of \"$k\":")
            val answer = readln()
            if (answer == v) {
                println("Correct!")
            } else {
                if(cardsList.containsValue(v)){
                    println("Wrong. The right answer is \"$v\", but your definition is correct for \"${cardsList.getKeyByValue(answer)}\".")
                }else {
                    println("Wrong. The right answer is \"$v\".")
                }
            }
        }
    }

    private fun <K, V> MutableMap<K, V>.getKeyByValue(value: V): K? {
        return this.entries.find { it.value == value }?.key
    }
}
