//package com.example.flashcards.data
//
//import java.util.Date
//
//object DataSource {
//    val d1 = System.currentTimeMillis()-86400000*5
//    val d2 = System.currentTimeMillis()-86400000*2
//    val d3 = System.currentTimeMillis()-500000*2
//
//    var bundles = listOf(
//        Bundle(
//            name = "Animals",
//            decks = listOf(
//                Deck(
//                    data = DeckData(name = "Desert", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//                    cards = listOf(
//                        Card(questionText = "Rattlesnake", answerText = "Reptile"),
//                        Card(questionText = "Shrew", answerText = "Mammal"),
//                        Card(questionText = "Vulture", answerText = "Reptile"),
//                        Card(questionText = "Gecko", answerText = "Reptile"),
//                        Card(questionText = "Desert Toad", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    data = DeckData(name = "Ocean", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//                    cards = listOf(
//                        Card(questionText = "Eel", answerText = "Fish"),
//                        Card(questionText = "Sardine", answerText = "Fish"),
//                        Card(questionText = "Mackerel", answerText = "Fish"),
//                        Card(questionText = "Oyster", answerText = "Mollusc"),
//                        Card(questionText = "Mussel", answerText = "Mollusc"),
//                    )
//                ),
//                Deck(
//                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                ),
//                Deck(
//                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//                    cards = listOf(
//                        Card(questionText = "Frog", answerText = "Amphibian"),
//                        Card(questionText = "Turtle", answerText = "Reptile"),
//                        Card(questionText = "Parrot", answerText = "Reptile"),
//                        Card(questionText = "Monkey", answerText = "Mammal"),
//                        Card(questionText = "Salamander", answerText = "Amphibian"),
//                    )
//                )
//            )
//        ),
//    )
//    var decks = listOf(
//        Deck(
//            data = DeckData(name = "Addition and Subtraction", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//            cards = listOf(
//                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
//                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
//                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
//                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
//                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
//                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
//            )
//        ),
//        Deck(
//            data = DeckData(name = "Math 2", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//            cards = listOf(
//                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
//                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
//                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
//                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
//                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
//                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
//            )
//        ),
//        Deck(
//            data = DeckData(name = "Math 3", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//            cards = listOf(
//                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
//                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
//                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
//                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
//                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
//                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
//            )
//        ),
//        Deck(
//            data = DeckData(name = "Math 4", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//            cards = listOf(
//                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
//                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
//                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
//                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
//                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
//                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
//            )
//        ),
//        Deck(
//            data = DeckData(name = "Math 5", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//            cards = listOf(
//                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
//                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
//                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
//                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
//                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
//                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
//            )
//        ),
//        Deck(
//            data = DeckData(name = "Math 6", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//            cards = listOf(
//                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
//                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
//                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
//                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
//                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
//                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
//            )
//        ),
//        Deck(
//            data = DeckData(name = "Math 7", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
//            cards = listOf(
//                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
//                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
//                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
//                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
//                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
//                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
//                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
//                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
//            )
//        ),
//    )
//}