/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.flashcards.data

import java.util.Date

object DataSource {
    val d1: Date = Date(System.currentTimeMillis()-86400000*5)
    val d2: Date = Date(System.currentTimeMillis()-86400000*2)
    val d3: Date = Date(System.currentTimeMillis()-500000*2)

    var bundles = listOf(
        Bundle(
            name = "Animals",
            decks = listOf(
                Deck(
                    data = DeckData(name = "Desert", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
                    cards = listOf(
                        Card(questionText = "Rattlesnake", answerText = "Reptile"),
                        Card(questionText = "Shrew", answerText = "Mammal"),
                        Card(questionText = "Vulture", answerText = "Reptile"),
                        Card(questionText = "Gecko", answerText = "Reptile"),
                        Card(questionText = "Desert Toad", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    data = DeckData(name = "Ocean", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
                    cards = listOf(
                        Card(questionText = "Eel", answerText = "Fish"),
                        Card(questionText = "Sardine", answerText = "Fish"),
                        Card(questionText = "Mackerel", answerText = "Fish"),
                        Card(questionText = "Oyster", answerText = "Mollusc"),
                        Card(questionText = "Mussel", answerText = "Mollusc"),
                    )
                ),
                Deck(
                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    data = DeckData(name = "Forest", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                )
            )
        ),
    )
    var decks = listOf(
        Deck(
            data = DeckData(name = "Addition and Subtraction", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
            cards = listOf(
                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
            )
        ),
        Deck(
            data = DeckData(name = "Math 2", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
            cards = listOf(
                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
            )
        ),
        Deck(
            data = DeckData(name = "Math 3", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
            cards = listOf(
                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
            )
        ),
        Deck(
            data = DeckData(name = "Math 4", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
            cards = listOf(
                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
            )
        ),
        Deck(
            data = DeckData(name = "Math 5", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
            cards = listOf(
                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
            )
        ),
        Deck(
            data = DeckData(name = "Math 6", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
            cards = listOf(
                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
            )
        ),
        Deck(
            data = DeckData(name = "Math 7", dateCreated = d1, dateUpdated = d2, dateStudied = d3, masteryLevel = 0.73f),
            cards = listOf(
                Card(questionText = "1+2", answerText = "3", hintText = "three", exampleText = "example"),
                Card(questionText = "3-1", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "4-4", answerText = "0", hintText = "zero", exampleText = "example"),
                Card(questionText = "4+2", answerText = "6", hintText = "six", exampleText = "example"),
                Card(questionText = "3-2", answerText = "1", hintText = "one", exampleText = "example"),
                Card(questionText = "1+1", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "4+5", answerText = "9", hintText = "nine", exampleText = "example"),
                Card(questionText = "8-3", answerText = "5", hintText = "five", exampleText = "example"),
                Card(questionText = "0+2", answerText = "2", hintText = "two", exampleText = "example"),
                Card(questionText = "9-8", answerText = "1", hintText = "one", exampleText = "example"),
                Card(questionText = "4+1", answerText = "5", hintText = "five", exampleText = "example"),
            )
        ),
    )
}