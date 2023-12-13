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
    private var logList = mutableListOf<String>()

    private var numAsk = 0
    private var cardsError = mutableMapOf<String,Int>()

    fun start() {
        while (promptChoice != "exit") {
            promptUser()
        }
    }

    private fun promptUser() {
        val pmp = "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):"
        println(pmp)
        logList.add(pmp)

        promptChoice = readln()
        logList.add(promptChoice)

        when(promptChoice){
            "add"           -> { addCardToCardsList() }
            "remove"        -> { removeCard() }
            "export"        -> { exportFile() }
            "import"        -> { importFile() }
            "ask"           -> { askCard() }
            "log"           -> { logToFile() }
            "hardest card"  -> { hardestCard() }
            "reset stats"   -> { resetStats() }
            "exit"          -> { println("Bye bye!") }
        }
    }

    private fun resetStats() {
        cardsError.keys.forEach {
            cardsError[it] = 0
        }

        val msg = "Card statistics have been reset."
        println(msg)
        logList.add(msg)
    }

    private fun hardestCard() {
        val maxError = cardsError.maxByOrNull { it.value }
        if (maxError != null && maxError.value > 0){
            val keyFilter = cardsError.filter { (_,value) -> value == maxError.value }

            val msg: String
            (if (keyFilter.keys.size > 1) {
                "The hardest cards are \"${keyFilter.keys.joinToString("\", \"")}\". You have ${maxError.value} errors answering them."
            }else{
                "The hardest card is \"${maxError.key}\". You have ${maxError.value} errors answering it."
            }).also { msg = it }
            println(msg)
            logList.add(msg)
        }else{
            val msg = "There are no cards with errors."
            println(msg)
            logList.add(msg)
        }
    }

    private fun logToFile() {
        println("File name:")
        logList.add("File name:")

        val logFileName = readln()
        logList.add(logFileName)

        try {
            val filePathLog = "${folderPath}${logFileName}"
            val file = File(filePathLog)

            if (!file.exists()){
                file.createNewFile()
            }

            val msg = "The log has been saved."
            println(msg)
            logList.add(msg)

            val fileWriter = FileWriter(file)
            if (file.length() == 0L) {
                logList.forEach {
                    fileWriter.write(it)
                    fileWriter.write("\n")
                }
            }else{
                logList.forEach {
                    fileWriter.append(it)
                    fileWriter.write("\n")
                }
            }
            fileWriter.close()
        }catch (e:Exception){
            val message = "An error occurred: ${e.message}"
            println(message)
            logList.add(message)
        }
    }

    private fun askCard() {
        println("How many times to ask?")
        logList.add("How many times to ask?")

        numAsk = readln().toInt()
        logList.add(numAsk.toString())

        if (cardsList.isNotEmpty()){
            val entriesList = cardsList.entries.toList()
            repeat(numAsk) {
                val randomIndex: Int = if(cardsList.size == 1){
                    0
                }else{
                    Random.nextInt(0, cardsList.size - 1)
                }

                val chosenCard = entriesList[randomIndex]

                val message = "Print the definition of \"${chosenCard.key}\":"
                println(message)
                logList.add(message)

                val answer = readln()
                logList.add(answer)

                if (answer == chosenCard.value) {
                    println("Correct!")
                    logList.add("Correct!")
                } else {
                    cardsError[chosenCard.key]?.let { cardsError[chosenCard.key] = it + 1 }

                    if(cardsList.containsValue(chosenCard.value) && (cardsList.getKeyByValue(answer)!=null)){
                        val msg = "Wrong. The right answer is \"${chosenCard.value}\", but your definition is correct for \"${cardsList.getKeyByValue(answer)}\"."
                        println(msg)
                        logList.add(msg)
                    }else {
                        val msg = "Wrong. The right answer is \"${chosenCard.value}\"."
                        println(msg)
                        logList.add(msg)
                    }
                }
            }
        }else{
            val msg = "Nothing to ask, no cards available."
            println(msg)
            logList.add(msg)
        }
    }

    private fun exportFile() {
        println("File name:")
        logList.add("File name:")

        val exportFileName = readln()
        logList.add(exportFileName)

        try {
            val filePathExport = "${folderPath}${exportFileName}"
            val file = File(filePathExport)
            if (!file.exists()){
                file.createNewFile()
            }

            val fileWriter = FileWriter(file)

            if (cardsList.isNotEmpty()) {
                cardsList.forEach {
                    val mistakes = cardsError[it.key]?:0
                    val textToWrite = "${it.key}:${it.value}:${mistakes}\n"
                    fileWriter.write(textToWrite)
                }
                fileWriter.close()
                val message = "${cardsList.size} cards have been saved."
                println(message)
                logList.add(message)
            }
        }catch (e:Exception){
            val message = "An error occurred: ${e.message}"
            println(message)
            logList.add(message)
        }
    }

    private fun importFile(){
        println("File name:")
        logList.add("File name:")

        val importFileName = readln()
        logList.add(importFileName)

        try {
            val filePathImport = "${folderPath}${importFileName}"
            val file = File(filePathImport)
            if (file.exists()) {
                val lines = file.readLines()
                lines.forEach {
                    val (key, value, mistakes) = it.split(":")
                    importedCards += Pair(key, value)
                    cardsError[key] = mistakes.toInt()
                }
                val message = "${importedCards.size} cards have been loaded"
                println(message)
                logList.add(message)

                updateCardsList()
                importedCards.clear()
            } else {
                println("File not found.")
                logList.add("File not found.")
            }
        }catch (e:Exception){
            val message = "An error occurred: ${e.message}"
            println(message)
            logList.add(message)
        }
    }

    private fun updateCardsList() {
        importedCards.forEach { (key,value) ->
            cardsList[key] = value
        }
    }

    private fun removeCard() {
        println("Which card?")
        logList.add("Which card?")

        removeCardKey = readln()
        logList.add(removeCardKey)

        if (isCardExist(removeCardKey)) {
            cardsList.remove(removeCardKey)
            val message = "The card has been removed"
            println(message)
            logList.add(message)
        }else{
            val message = "Can't remove \"$removeCardKey\": there is no such card."
            println(message)
            logList.add(message)
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
        logList.add("The card:")

        val cardKey = readln()
        logList.add(cardKey)

        if (!isCardExist(cardKey)) {
            cardsError[cardKey] = 0

            println("The definition of the card:")
            logList.add("The definition of the card:")

            val cardDef = readln()
            logList.add(cardDef)

            if (!isDefinitionExist(cardDef)) {
                val message = "The pair (\"${cardKey}\":\"${cardDef}\") has been added."
                cardsList += Pair(cardKey,cardDef)

                println(message)
                logList.add(message)
            }else{
                val message = "The definition \"$cardDef\" already exists."
                println(message)
                logList.add(message)
            }
        }else{
            val message = "The card \"$cardKey\" already exists."

            println(message)
            logList.add(message)
        }
    }

    private fun <K, V> MutableMap<K, V>.getKeyByValue(value: V): K? {
        return this.entries.find { it.value == value }?.key
    }
}
