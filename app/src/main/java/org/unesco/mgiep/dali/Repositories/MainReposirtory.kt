package org.unesco.mgiep.dali.Repositories

import android.content.res.Resources
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Maybe
import org.unesco.mgiep.dali.Data.*
import org.unesco.mgiep.dali.R

class MainReposirtory {

    private val firebaseRepository = FirebaseRepository()

    private val mDatabase = FirebaseFirestore.getInstance()
    private val usersRef = mDatabase.collection("users")
    private val screeningsRef = mDatabase.collection("screenings")
    private val participantsRef = mDatabase.collection("participants")
    private val resultsRef = mDatabase.collection("results")

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

    fun saveResult(id: String, result: Result): Task<*> {
        Log.d("save","result")
        return firebaseRepository.writeDocument(resultsRef.document(id),result,"result")
    }


    //get data
    fun getUser(id: String): Maybe<DocumentSnapshot> {
        return firebaseRepository.fetchDocument(usersRef.document(id),"user")
    }

    fun getScreening(id: String): Maybe<DocumentSnapshot> {
        return firebaseRepository.fetchDocument(screeningsRef.document(id),"screening")
    }

    fun getParticipant(id:String): Maybe<DocumentSnapshot> {
        return firebaseRepository.fetchDocument(participantsRef.document(id),"participant")
    }

    fun getResult(id: String): Maybe<DocumentSnapshot> {
        return firebaseRepository.fetchDocument(resultsRef.document(id),"result")
    }



}