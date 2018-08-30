package org.unesco.mgiep.dali.Repositories

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import org.unesco.mgiep.dali.Data.*


class FirebaseRepository {
    private val mDatabase = FirebaseFirestore.getInstance()
    private val usersRef = mDatabase.collection("users")
    private val screeningsRef = mDatabase.collection("screenings")
    private val participantsRef = mDatabase.collection("participants")
    private val resultsRef = mDatabase.collection("results")

    fun writeNewUser(userId: String, user: User) {

        usersRef.document(userId).set(user)
    }

    fun writeNewScreening(id:String, screening: FirebaseScreening ){

        screeningsRef.document(id).set(screening)
                .addOnSuccessListener {
                    Log.d("save-screening","success")
                }
                .addOnFailureListener {
                    Log.d("save-screening","failed")
                }
    }

    fun writeNewParticipant(id: String, participant: Participant){
        participantsRef.document(id).set(participant)
    }

    fun writeResult(id: String, result: Result){
        resultsRef.document(id).set(result)
    }

    fun fetchUserDetails(userId: String){
        usersRef.document(userId).get().addOnCompleteListener {
            task ->
            if(task.isSuccessful){
                if(task.result.exists()){
                    val user = task.result.toObject(User::class.java)
                    Log.d("fetch-userDetails","success")
                }else{
                    Log.d("fetch-userDetails","failed!!No Such Document")
                }
            }else{
                Log.d("fetch-userDetails","failed",task.exception)
            }
        }
    }

    fun fetchScreening(screeningId: String){
        screeningsRef.document(screeningId).get().addOnCompleteListener {
            task ->
            if(task.isSuccessful){
                if(task.result.exists()){
                    val screening = task.result.toObject(FirebaseScreening::class.java)
                    Log.d("fetch-screening","success")
                }else{
                    Log.d("fetch-screening","failed!!No Such Document")
                }
            }else{
                Log.d("fetch-screening","failed",task.exception)
            }
        }
    }

    fun fetchUserScreenings(userId: String){
        screeningsRef
                .whereEqualTo("userId",userId)
                .get()
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                         if(!task.result.isEmpty){
                                Log.d("fetch-screenings","success")
                         }else{
                                Log.d("fetch-screenings","failed!!No Such Document")
                         }
                    }else{
                        Log.d("fetch-screening","failed",task.exception)
                    }
                }
    }

    fun fetchParticipantScreenings(participantId: String){
        screeningsRef
                .whereEqualTo("participantId",participantId)
                .get()
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        if(!task.result.isEmpty){
                            Log.d("fetch-PScreenings","success")
                        }else{
                            Log.d("fetch-PScreenings","failed!!No Such Document")
                        }
                    }else{
                        Log.d("fetch-screening","failed",task.exception)
                    }
                }
    }

    fun fetchParticipant(participantId: String){
        participantsRef.document(participantId)
                .get()
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        if(task.result.exists()){
                            val participant = task.result.toObject(Participant::class.java)
                            Log.d("fetch-Participant","success")
                        }else{
                            Log.d("fetch-Participant","failed!!No Such Document")
                        }
                    }else{
                        Log.d("fetch-Participant","failed",task.exception)
                    }
                }
    }

    fun fetchParticipants(userId: String){
        participantsRef
                .whereEqualTo("userId",userId)
                .get()
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        if(!task.result.isEmpty){
                            Log.d("fetch-Participants","success")
                        }else{
                            Log.d("fetch-Participants","failed!!No Such Document")
                        }
                    }else{
                        Log.d("fetch-Participants","failed",task.exception)
                    }
                }
    }

    fun fetchResult(resultId: String){
        resultsRef
                .document(resultId)
                .get()
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        if(!task.result.exists()){
                            Log.d("fetch-Result","success")
                        }else{
                            Log.d("fetch-Result","failed!!No Such Document")
                        }
                    }else{
                        Log.d("fetch-Result","failed",task.exception)
                    }
                }
    }

    fun fetchParticipantResults(participantId: String){
        resultsRef
                .whereEqualTo("participantId",participantId)
                .get()
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        if(!task.result.isEmpty){
                            Log.d("fetch-PResults","success")
                        }else{
                            Log.d("fetch-PResults","failed!!No Such Document")
                        }
                    }else{
                        Log.d("fetch-PResults","failed",task.exception)
                    }
                }
    }

    fun fetchScreeningResult(screeningId: String){
        resultsRef
                .whereEqualTo("screeningId",screeningId)
                .get()
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        if(!task.result.isEmpty){
                            Log.d("fetch-SResults","success")
                        }else{
                            Log.d("fetch-SResults","failed!!No Such Document")
                        }
                    }else{
                        Log.d("fetch-SResults","failed",task.exception)
                    }
                }
    }

}