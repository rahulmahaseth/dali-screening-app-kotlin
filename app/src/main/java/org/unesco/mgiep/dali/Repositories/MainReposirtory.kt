package org.unesco.mgiep.dali.Repositories

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import org.unesco.mgiep.dali.Data.*

class MainReposirtory {

    private val firebaseRepository = FirebaseRepository()

    private val mDatabase = FirebaseFirestore.getInstance()
    private val usersRef = mDatabase.collection("users")
    private val screeningsRef = mDatabase.collection("screenings")
    private val participantsRef = mDatabase.collection("participants")
    private val scoresRef = mDatabase.collection("scores")

    //save data
    fun saveUser(id: String, user: User): Task<*> {
        Log.d("save","user")
        return firebaseRepository.writeDocument(usersRef.document(id),user,"user")
    }

    fun saveScreening(id: String, screening: Screening): Task<*> {
        Log.d("save","screening")
        return firebaseRepository.writeDocument(screeningsRef.document(id), screening,"screening")
    }

    fun saveParticipant(id: String, participant: Participant): Task<*> {
        Log.d("save","participant")
        return firebaseRepository.writeDocument(participantsRef.document(id),participant,"participant")
    }

    fun saveScore(id: String, result: QuestionWiseScore): Task<*> {
        return firebaseRepository.writeDocument(scoresRef.document(id), result, "result")
    }

    //get data
    fun getUser(id: String): Task<DocumentSnapshot> {
        return firebaseRepository.fetchDocument(usersRef.document(id),"user")
    }

    fun getScreening(id: String): Task<DocumentSnapshot> {
        return firebaseRepository.fetchDocument(screeningsRef.document(id),"screening")
    }

    fun getParticipant(id:String): Task<DocumentSnapshot> {
        return firebaseRepository.fetchDocument(participantsRef.document(id),"participant")
    }

}