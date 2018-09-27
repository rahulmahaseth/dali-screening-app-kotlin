package org.unesco.mgiep.dali.Data

data class ScreeningParticipant(
        var screening: Screening = Screening(),
        var participant: Participant = Participant()
)