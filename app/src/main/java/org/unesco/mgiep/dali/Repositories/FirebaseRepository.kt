package org.unesco.mgiep.dali.Repositories

import android.database.Observable
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import org.unesco.mgiep.dali.Data.*
import java.util.*


class FirebaseRepository {
    private val mDatabase = FirebaseFirestore.getInstance();

    fun writeNewUser(userId: String, user: User) {

        mDatabase.collection("user").document(userId).set(user)
    }

    fun writeNewScreening(id:String, screening: Screening ){

        mDatabase.collection("screening").document(id).set(screening)
                .addOnSuccessListener {
                    Log.d("save-screening","success")
                }
                .addOnFailureListener {
                    Log.d("save-screening","failed")
                }
    }

    fun writeNewParticipant(id: String, participant: Participant){
        mDatabase.collection("participant").document(id).set(participant)
    }

    fun writeResult(id: String, result: Result){
        mDatabase.collection("result").document(id).set(result)
    }

}