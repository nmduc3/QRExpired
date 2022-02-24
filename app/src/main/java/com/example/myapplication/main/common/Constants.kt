package com.example.myapplication.main.common

class Constants {
    object TagName {
        const val HOME = "HOME_TAG"
    }

    object DefaultValue {
        const val TIME_DELAY_DEFAULT = 1500L
        const val DB_NAME = "QR_FOOD_DB"
        const val TIME_DELAY = 3L // day
        const val KEY_NAME = "key_name"
        const val CHANNEL_ID = "QRFoodChannelId"
        val DEFAULT_FOLDER = hashMapOf(
            "YQ" to "apple",
            "Yg" to "pineapple",
            "Yw" to "tomato",
            "ZA" to "orange",
            "ZQ" to "banana",
            "Zg" to "grapes",
            "Zw" to "pear",
            "aA" to "kiwi",
            "aQ" to "asparagus",
            "ag" to "avocado",
            "aw" to "beetroot",
            "bA" to "broccoli",
            "bQ" to "brussels sprout",
            "bg" to "cabbage",
            "bw" to "carrot",
            "cA" to "potato"
        )
    }
}
