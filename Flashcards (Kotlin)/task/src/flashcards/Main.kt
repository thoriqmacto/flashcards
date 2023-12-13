package flashcards

import java.io.File
import java.io.FileWriter
import kotlin.random.Random

fun main() {
    val game = Flashcards()
    game.start()
}

class Flashcards {
    private val cardsList = mutableMapOf<String,String>()
    private var promptChoice = ""
    private var removeCardKey = ""

    private val workingDir = System.getProperty("user.dir")
    private val separator = File.separator
    private val folderPath = "${workingDir}${separator}"

    private var importedCards = mutableMapOf<String,String>()



    private var numAsk = 0

    fun start() {
        while (promptChoice != "exit") {
            promptUser()
        }
    }

    private fun promptUser() {
        println("Input the action (add, remove, import, export, ask, exit):")
        promptChoice = readln()

        when(promptChoice){
            "add" -> { addCardToCardsList() }
            "remove" -> { removeCard() }
            "export" -> { exportFile() }
            "import" -> { importFile() }
            "ask" -> { askCard() }
            "exit" -> { println("Bye bye!") }
        }
    }

    private fun askCard() {
        println("How many times to ask?")
        numAsk = readln().toInt()

        if (cardsList.isNotEmpty()){
            val entriesList = cardsList.entries.toList()
            repeat(numAsk) {
                val randomIndex: Int = if(cardsList.size == 1){
                    0
                }else{
                    Random.nextInt(0, cardsList.size - 1)
                }

                val chosenCard = entriesList[randomIndex]
                println("Print the definition of \"${chosenCard.key}\":")
                val answer = readln()

                if (answer == chosenCard.value) {
                    println("Correct!")
                } else {
                    if(cardsList.containsValue(chosenCard.value) && (cardsList.getKeyByValue(answer)!=null)){
                        println("Wrong. The right answer is \"${chosenCard.value}\", but your definition is correct for \"${cardsList.getKeyByValue(answer)}\".")
                    }else {
                        println("Wrong. The right answer is \"${chosenCard.value}\".")
                    }
                }
            }
        }else{
            println("Nothing to ask, no cards available.")
        }
    }

    private fun exportFile() {
        println("File name:")
        val exportFileName = readln()

        try {
            val filePathExport = "${folderPath}${exportFileName}"
            val file = File(filePathExport)
            if (!file.exists()){
                file.createNewFile()
            }

            val fileWriter = FileWriter(file)

            if (cardsList.isNotEmpty()) {
                cardsList.forEach {
                    val textToWrite = "${it.key}:${it.value}\n"
                    fileWriter.write(textToWrite)
                }
                fileWriter.close()
                println("${cardsList.size} cards have been saved.")
            }
        }catch (e:Exception){
            println("An error occurred: ${e.message}")
        }
    }



    private fun importFile(){
        println("File name:")
        val importFileName = readln()

        try {
            val filePathImport = "${folderPath}${importFileName}"
            val file = File(filePathImport)
            if (file.exists()) {
                val lines = file.readLines()
                lines.forEach {
                    val (key, value) = it.split(":")
                    importedCards += Pair(key, value)
                }

                println("${importedCards.size} cards have been loaded")
                updateCardsList()
                importedCards.clear()
            } else {
                println("File not found.")
            }
        }catch (e:Exception){
            println("An error occurred: ${e.message}")
        }
    }

    private fun updateCardsList() {
        importedCards.forEach { (key,value) ->
            cardsList[key] = value
        }
    }

    private fun removeCard() {
        println("Which card?")
        removeCardKey = readln()
        if (isCardExist(removeCardKey)) {
            cardsList.remove(removeCardKey)
            println("The card has been removed")
        }else{
            println("Can't remove \"$removeCardKey\": there is no such card.")
        }
    }

    private fun isCardExist(cardKey:String):Boolean{
        return cardsList.containsKey(cardKey)
    }

    private fun isDefinitionExist(cardValue:String):Boolean{
        return cardsList.containsValue(cardValue)
    }

    private fun addCardToCardsList() {
        println("The card:")
        val cardKey = readln()
        if (!isCardExist(cardKey)) {
            println("The definition of the card:")
            val cardDef = readln()
            if (!isDefinitionExist(cardDef)) {
                cardsList += Pair(cardKey,cardDef)
                println("The pair (\"${cardKey}\":\"${cardDef}\") has been added.")
            }else{
                println("The definition \"$cardDef\" already exists.")
            }
        }else{
            println("The card \"$cardKey\" already exists.")
        }
    }

    private fun <K, V> MutableMap<K, V>.getKeyByValue(value: V): K? {
        return this.entries.find { it.value == value }?.key
    }
}
