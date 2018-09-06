package org.unesco.mgiep.dali.Fragments

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.nitrico.lastadapter.BR
import com.github.nitrico.lastadapter.LastAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_participantlist.*
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.Participant
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.FirebaseRepository
import org.unesco.mgiep.dali.Utility.showFragment
import org.unesco.mgiep.dali.databinding.ItemParticipantBinding

class ParticipantList : Fragment(){

    private val lastAdapter: LastAdapter by lazy { initLastAdapter() }
    private val participants = ObservableArrayList<Participant>()
    private lateinit var firebaseRepository: FirebaseRepository
    private lateinit var mAuth: FirebaseAuth

    fun initLastAdapter():LastAdapter{
        return LastAdapter(participants, BR.item)
                .map<Participant, ItemParticipantBinding>(R.layout.item_participant){
                    onBind {

                    }
                    onClick {
                        showFragment(
                                Fragment.instantiate(
                                        activity,
                                        org.unesco.mgiep.dali.Fragments.Participant::class.java.name
                                ),
                                true

                        )
                    }
                }
                .into(recycler_particpants)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_forgotpassword, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_particpants.adapter = lastAdapter
        recycler_particpants.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        fetchParticpants()

        screening_swipe_layout.setOnRefreshListener {
            fetchParticpants()
        }
    }

    private fun fetchParticpants(){
        firebaseRepository.fetchParticipants(mAuth.currentUser!!.uid)
                .addOnCompleteListener {
                    if(!it.result.isEmpty){
                        participants.clear()
                        it.result.documents.forEach {
                            participants.add(it.toObject(Participant::class.java))
                            lastAdapter.notifyDataSetChanged()
                            screening_swipe_layout.isRefreshing = false
                        }
                    }else{
                        screening_swipe_layout.isRefreshing = false
                    }
                }
                .addOnFailureListener {
                    screening_swipe_layout.isRefreshing = false
                }

    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}