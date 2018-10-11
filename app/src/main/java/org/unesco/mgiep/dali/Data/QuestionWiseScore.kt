package org.unesco.mgiep.dali.Data

data class QuestionWiseScore (
        var id: String = "",
        var screeningId: String = "",
        var questionWiseScore: HashMap<String, Int> = hashMapOf()
)