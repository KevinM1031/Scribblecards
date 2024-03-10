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

import com.example.flashcards.R

object DataSource {
    val cards = listOf(
        Deck(
            name = "Addition and Subtraction",
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
        Bundle(
            name = "Animals",
            decks = listOf(
                Deck(
                    name = "Desert",
                    cards = listOf(
                        Card(questionText = "Rattlesnake", answerText = "Reptile"),
                        Card(questionText = "Shrew", answerText = "Mammal"),
                        Card(questionText = "Vulture", answerText = "Reptile"),
                        Card(questionText = "Gecko", answerText = "Reptile"),
                        Card(questionText = "Desert Toad", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    name = "Ocean",
                    cards = listOf(
                        Card(questionText = "Eel", answerText = "Fish"),
                        Card(questionText = "Sardine", answerText = "Fish"),
                        Card(questionText = "Mackerel", answerText = "Fish"),
                        Card(questionText = "Oyster", answerText = "Mollusc"),
                        Card(questionText = "Mussel", answerText = "Mollusc"),
                    )
                ),
                Deck(
                    name = "Forest",
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    name = "Forest",
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    name = "Forest",
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    name = "Forest",
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    name = "Forest",
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    name = "Forest",
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    name = "Forest",
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    name = "Forest",
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    name = "Forest",
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    name = "Forest",
                    cards = listOf(
                        Card(questionText = "Frog", answerText = "Amphibian"),
                        Card(questionText = "Turtle", answerText = "Reptile"),
                        Card(questionText = "Parrot", answerText = "Reptile"),
                        Card(questionText = "Monkey", answerText = "Mammal"),
                        Card(questionText = "Salamander", answerText = "Amphibian"),
                    )
                ),
                Deck(
                    name = "Forest",
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
        Deck(
            name = "Math 2",
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
            name = "Math 3",
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
            name = "Math 4",
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
            name = "Math 5",
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
            name = "Math 6",
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