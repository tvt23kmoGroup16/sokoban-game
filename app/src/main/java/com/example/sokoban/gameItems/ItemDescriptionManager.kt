package com.example.sokoban.gameItems

class ItemDescriptionManager {

    //Mapping
    private val itemDescriptions = mapOf(
        'I' to "Item", // Generic item placeholder
        'M' to "Magic Wand",
        'S' to "Speed Boots",
        'R' to "Ray Gun",
        'C' to "Magic Club"
    )

    val itemDetailedDescriptions = mapOf(
        'M' to "A magical wand that can prevent boulders from falling into holes.",
        'S' to "Enhanced pair of hiking boots that contain exceptional powers.",
        'R' to "Strange looking weapon made of metal out of the world of ordinary humans.",
        'C' to "Sturdy little wooden club, reinforced with nails. It pulses with strange magic."

    )

}