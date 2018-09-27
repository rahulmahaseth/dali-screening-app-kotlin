package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.github.nitrico.lastadapter.LastAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.fragment_pendingscreenings.*
import kotlinx.android.synthetic.main.item_screeningparticipant.view.*
import org.unesco.mgiep.dali.BR
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.*
import org.unesco.mgiep.dali.Data.Screening
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningParticipantViewModel
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.FirebaseRepository
import org.unesco.mgiep.dali.Repositories.MainReposirtory
import org.unesco.mgiep.dali.Utility.hide
import org.unesco.mgiep.dali.Utility.show
import org.unesco.mgiep.dali.Utility.showAsToast
import org.unesco.mgiep.dali.Utility.showFragment
import org.unesco.mgiep.dali.databinding.ItemScreeningparticipantBinding

class PendingScreenings: Fragment() {

    private val lastAdapter: LastAdapter by lazy { initLastAdapter() }
    private val screenings = ObservableArrayList<Screening>()
    private val particpants = ObservableArrayList<Participant>()
    private val screeningParticipants = ObservableArrayList<ScreeningParticipant>()
    private val screeningParticipantContainer = ObservableArrayList<ScreeningParticipant>()
    private lateinit var firebaseRepository: FirebaseRepository
    private lateinit var mainRepository: MainReposirtory
    private lateinit var mAuth: FirebaseAuth
    private lateinit var screeningViewModel: ScreeningViewModel
    private lateinit var participantViewModel: ScreeningParticipantViewModel

    fun initLastAdapter(): LastAdapter {
        return LastAdapter(screeningParticipants, BR.item)
                .map<ScreeningParticipant, ItemScreeningparticipantBinding>(R.layout.item_screeningparticipant) {
                    onBind {
                        if(it.binding.item!!.screening.type == Type.JST.toString() ){
                        }

                        if(it.binding.item!!.participant.gender == Gender.MALE.toString()){
                            it.itemView.item_screening_male.visibility = View.VISIBLE
                            it.itemView.item_screening_female.visibility = View.GONE
                        }else{
                            it.itemView.item_screening_male.visibility = View.GONE
                            it.itemView.item_screening_female.visibility = View.VISIBLE
                        }
                    }
                    onClick {
                        screeningViewModel.select(it.binding.item!!.screening)
                        participantViewModel.select(it.binding.item!!.participant)
                        showFragment(
                                Fragment.instantiate(
                                        activity,
                                        ScreeningDetails::class.java.name
                                ),
                                true
                        )
                    }
                }
                .into(pending_screening_recycler)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        setHasOptionsMenu(true)
        mAuth = FirebaseAuth.getInstance()
        firebaseRepository = FirebaseRepository()
        mainRepository = MainReposirtory()
        screeningViewModel = ViewModelProviders.of(activity!!).get(ScreeningViewModel::class.java)
        participantViewModel = ViewModelProviders.of(activity!!).get(ScreeningParticipantViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_pendingscreenings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.nav_view.menu.getItem(2).isChecked = true

        pending_screening_recycler.adapter = lastAdapter
        pending_screening_recycler.layoutManager = LinearLayoutManager(activity)

        fetchPendingScreenings()


        pending_swipe_refresh.setOnRefreshListener {
            fetchPendingScreenings()
        }


    }

    private fun fetchPendingScreenings(){
        if(pending_swipe_refresh.isRefreshing) pending_screening_progressBar?.hide() else pending_screening_progressBar?.show()
        firebaseRepository.fetchPendingUserScreenings(mAuth.currentUser!!.uid)
                .addOnCompleteListener {
                    if(!it.result.isEmpty){
                        screenings.clear()
                        it.result.documents.forEach {
                            screenings.add(it.toObject(Screening::class.java))
                        }
                        fetchParticpants()
                    }else{
                        pending_swipe_refresh?.isRefreshing = false
                        pending_screening_progressBar?.hide()

                    }
                }
                .addOnCanceledListener {
                    pending_swipe_refresh?.isRefreshing = false
                    pending_screening_progressBar?.hide()
                    getString(R.string.network_error).showAsToast(activity!!)
                }
    }

    private fun fetchParticpants(){
        firebaseRepository.fetchParticipants(mAuth.currentUser!!.uid)
                .addOnSuccessListener {
                    if(!it.isEmpty){
                        particpants.clear()
                        it.documents.forEach {
                            particpants.add(it.toObject(Participant::class.java))
                            screeningParticipants.clear()
                            screeningParticipantContainer.clear()
                            screenings.forEach {s->
                                particpants.forEach {p->
                                    if(s.participantId == p.id){
                                        screeningParticipantContainer.add(ScreeningParticipant(s,p))
                                        screeningParticipants.add(ScreeningParticipant(s,p))
                                    }
                                }
                            }
                        }
                        lastAdapter.notifyDataSetChanged()
                        pending_swipe_refresh?.isRefreshing = false
                        pending_screening_progressBar?.hide()
                    }else{
                        pending_swipe_refresh?.isRefreshing = false
                        pending_screening_progressBar?.hide()
                    }
                }
                .addOnCanceledListener {
                    pending_swipe_refresh?.isRefreshing = false
                    pending_screening_progressBar?.hide()
                    getString(R.string.network_error).showAsToast(activity!!)
                }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        activity!!.menuInflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item!!.itemId){
            R.id.action_add -> {
                showFragment(
                        Fragment.instantiate(
                                activity,
                                NewScreening::class.java.name
                        ),
                        true
                )
                true
            }
            R.id.sort_only_jst -> {
                screeningParticipants.clear()
                screeningParticipantContainer.filter { it.screening.type == Type.JST.toString() }
                        .forEach {
                            screeningParticipants.add(it)
                        }
                lastAdapter.notifyDataSetChanged()
                true
            }
            R.id.sort_only_mst -> {
                screeningParticipants.clear()
                screeningParticipantContainer.filter { it.screening.type == Type.MST.toString() }
                        .map {
                            screeningParticipants.add(it)
                        }
                lastAdapter.notifyDataSetChanged()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity!!.title = getString(R.string.scheduled)
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}