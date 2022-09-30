package com.psr.quizapp.models

import com.psr.quizapp.models.Question

object Questions {

    fun getQuestions(): ArrayList<Question> {
        val questionsList = ArrayList<Question>()

        // 1
        val que1 = Question(
            1, "Which one of the following river flows between Vindhyan and Satpura ranges?",
            "Narmada", "Mahanadi",
            "Son", "Netravati", 1
        )

        questionsList.add(que1)

        // 2
        val que2 = Question(
            2, "The Central Rice Research Station is situated in?",
            "Chennai", "Quilon",
            "Bangalore", "Cuttack", 4
        )

        questionsList.add(que2)

        // 3
        val que3 = Question(
            3, "Who among the following wrote Sanskrit grammar?",
            "Aryabhatt", "Panini",
            "Charak", "Kalidasa", 2
        )

        questionsList.add(que3)
        return questionsList
    }
    // END
}
// END