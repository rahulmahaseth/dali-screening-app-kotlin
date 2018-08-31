package org.unesco.mgiep.dali.Repositories

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import durdinapps.rxfirebase2.RxFirestore
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers


class FirebaseRepository {
    private val mDatabase = FirebaseFirestore.getInstance()
    private val usersRef = mDatabase.collection("users")
    private val screeningsRef = mDatabase.collection("screenings")
    private val participantsRef = mDatabase.collection("participants")
    private val resultsRef = mDatabase.collection("results")

    //Functions to Write Data to FireStore
    fun writeDocument(documentReference: DocumentReference, oobject:Any, type: String): Completable{
        return RxFirestore.setDocument(documentReference,oobject).subscribeOn(Schedulers.newThread())
                .doOnComplete {
                    Log.d("writeDocument","$type saved")
                }
    }

    //functions to fetch data from Firestore
    fun fetchDocument(documentReference: DocumentReference, type:String): Maybe<DocumentSnapshot>{

        return RxFirestore.getDocument(documentReference).subscribeOn(Schedulers.newThread())
                .doOnSuccess {
                    if(it.exists()){
                        it
                        Log.d("fetch$type","Success")
                    }else{
                        Log.d("fetchUserDetails","Document does not exist")
                    }
                }
                .doOnError {
                    Log.d("fetchUserDetails","Failed to fetch",it)
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