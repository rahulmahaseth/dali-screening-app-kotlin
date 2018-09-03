package org.unesco.mgiep.dali.Repositories

import android.content.res.Resources
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
    fun saveUser(id: String, user: User): Completable {
        return firebaseRepository.writeDocument(usersRef.document(id),user,"user")
    }

    fun saveScreening(id: String, screening: Screening): Completable{
        return firebaseRepository.writeDocument(screeningsRef.document(id), screening,"screening")
    }

    fun saveParticipant(id: String, participant: Participant): Completable{
        return firebaseRepository.writeDocument(participantsRef.document(id),participant,"participant")
    }

    fun saveResult(id: String, result: Result): Completable {
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