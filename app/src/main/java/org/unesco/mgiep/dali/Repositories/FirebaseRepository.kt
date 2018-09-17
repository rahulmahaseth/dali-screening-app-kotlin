package org.unesco.mgiep.dali.Repositories

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import durdinapps.rxfirebase2.RxFirestore
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import org.unesco.mgiep.dali.Data.Screening
import org.unesco.mgiep.dali.Data.User


class FirebaseRepository {
    private val mDatabase = FirebaseFirestore.getInstance()
    private val screeningsRef = mDatabase.collection("screenings")
    private val participantsRef = mDatabase.collection("participants")



    //Functions to Write Data to FireStore
    fun writeDocument(documentReference: DocumentReference, oobject:Any, type: String): Task<*> {
        Log.d("firebase-save",type)
        return documentReference.set(oobject).addOnCompleteListener {
                    Log.d("firebase-save-$type","success")
                }
                .addOnFailureListener {
                    Log.d("firebase-save-$type","error",it)
                }
    }


    fun fetchDocument(documentReference: DocumentReference, type:String): Task<DocumentSnapshot> {
        return documentReference.get()
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        Log.d("fetch-$type","Success")
                    }else{
                        Log.d("fetch-$type","failed",it.exception)
                    }
                }
                .addOnCanceledListener {
                    Log.d("fetch-$type","cancelled")
                }
    }


    fun fetchUserScreenings(userId: String): Task<QuerySnapshot> {
        return screeningsRef
                .whereEqualTo("userId",userId)
                .whereEqualTo("completed",true)
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

    fun fetchPendingUserScreenings(userId: String): Task<QuerySnapshot> {
        return screeningsRef
                .whereEqualTo("userId",userId)
                .whereEqualTo("completed", false)
                .get()
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        if(!task.result.isEmpty){
                            Log.d("fetch-Pendingscreenings","success")
                        }else{
                            Log.d("fetch-Pendingscreenings","failed!!No Such Document")
                        }
                    }else{
                        Log.d("fetch-Pendingscreening","failed",task.exception)
                    }
                }
    }

    fun updateScreeningCompletion(id: String, screening: Screening): Task<Void> {
        return screeningsRef.document(id).set(screening)
    }

    fun fetchParticipantScreenings(participantId: String): Task<QuerySnapshot> {
        return screeningsRef
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

    fun fetchParticipants(userId: String): Task<QuerySnapshot> {
        return participantsRef
                .whereEqualTo("createdBy",userId)
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


    fun fetchParticipantDetails(userId: String, screeningId: String): Task<QuerySnapshot> {
        return participantsRef
                .whereEqualTo("screeningId",screeningId)
                .whereEqualTo("userId",userId)
                .get()
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        if(!task.result.isEmpty){
                            Log.d("fParticipantDetails","success")
                        }else{
                            Log.d("ParticipantDetails","failed!!No Such Document")
                        }
                    }else{
                        Log.d("ParticipantDetails","failed",task.exception)
                    }
                }
    }

}