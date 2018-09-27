package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.ObservableArrayList
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.github.nitrico.lastadapter.LastAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.item_screening.view.*
import org.unesco.mgiep.dali.Activity.NewScreeningActivity
import org.unesco.mgiep.dali.BR
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.FirebaseRepository
import org.unesco.mgiep.dali.Utility.hide
import org.unesco.mgiep.dali.Utility.show
import org.unesco.mgiep.dali.Utility.showFragment
import android.view.MenuInflater
import kotlinx.android.synthetic.main.drawer_layout.*
import org.unesco.mgiep.dali.Data.*
import org.unesco.mgiep.dali.Data.Screening
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningParticipantViewModel
import org.unesco.mgiep.dali.Repositories.MainReposirtory
import org.unesco.mgiep.dali.Utility.showAsToast
import org.unesco.mgiep.dali.databinding.ItemScreeningparticipantBinding


class Dashboard : Fragment() {

    private val lastAdapter: LastAdapter by lazy { initLastAdapter() }
    private val screeningsContainer = ObservableArrayList<Screening>()
    private val screenings = ObservableArrayList<Screening>()
    private val particpants = ObservableArrayList<Participant>()
    private val screeningParticipants = ObservableArrayList<ScreeningParticipant>()
    private lateinit var firebaseRepository: FirebaseRepository
    private lateinit var mainRepository: MainReposirtory
    private lateinit var mAuth: FirebaseAuth
    private lateinit var screeningViewModel: ScreeningViewModel
    private lateinit var participantViewModel: ScreeningParticipantViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        setHasOptionsMenu(true)
        firebaseRepository = FirebaseRepository()
        mainRepository = MainReposirtory()
        mAuth = FirebaseAuth.getInstance()
        screeningViewModel = ViewModelProviders.of(activity!!).get(ScreeningViewModel::class.java)
        participantViewModel = ViewModelProviders.of(activity!!).get(ScreeningParticipantViewModel::class.java)
    }

    fun initLastAdapter(): LastAdapter {
        return LastAdapter(screeningParticipants, BR.item)
                .map<ScreeningParticipant, ItemScreeningparticipantBinding>(R.layout.item_screeningparticipant) {
                    onBind {

                        if (it.binding.item!!.screening.type == Type.JST.toString()) {
                            if (it.binding.item!!.screening.totalScore < 16) {
                                it.itemView.item_layout.background = resources.getDrawable(R.drawable.rectangle_green)
                            } else {
                                it.itemView.item_layout.background = resources.getDrawable(R.drawable.rectangle_red)
                            }
                        } else {
                            if (it.binding.item!!.screening.totalScore < 19) {
                                it.itemView.item_layout.background = resources.getDrawable(R.drawable.rectangle_green)
                            } else {
                                it.itemView.item_layout.background = resources.getDrawable(R.drawable.rectangle_red)
                            }
                        }


                        if(it.binding.item!!.participant.gender == Gender.MALE.toString()){
                            it.itemView.item_screening_male.visibility = View.VISIBLE
                            it.itemView.item_screening_female.visibility = View.GONE
                        }else{
                            it.itemView.item_screening_male.visibility = View.GONE
                            it.itemView.item_screening_female.visibility = View.VISIBLE
                        }


                        if(it.binding.item!!.screening.completed){
                            it.itemView.item_screening_done.visibility =View.VISIBLE
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
                .into(screening_recycler)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_dashboard, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.nav_view.menu.getItem(1).isChecked = true

        screening_recycler.adapter = lastAdapter
        screening_recycler.layoutManager = LinearLayoutManager(activity)


        fetchScreenings()


        dashboard_refresh_layout.setOnRefreshListener {
             fetchScreenings()
         }

    }

    private fun fetchScreenings() {
        if(dashboard_refresh_layout.isRefreshing) dashboard_progressBar?.hide() else dashboard_progressBar?.show()
        firebaseRepository.fetchUserScreenings(mAuth.currentUser!!.uid)
                .addOnSuccessListener {
                    if (!it.isEmpty) {
                        screenings.clear()
                        it.documents.forEach {
                            screeningsContainer.add(it.toObject(Screening::class.java))
                            screenings.add(it.toObject(Screening::class.java))
                        }
                        fetchParticpants()
                    } else {
                        dashboard_refresh_layout?.isRefreshing = false
                        dashboard_progressBar?.hide()
                    }
                }
                .addOnCanceledListener {
                    dashboard_progressBar?.hide()
                    dashboard_refresh_layout?.isRefreshing = false
                    getString(R.string.network_error).showAsToast(activity!!)
                }
    }

    private fun fetchParticpants(){
        //AsyncTask.execute {
            firebaseRepository.fetchParticipants(mAuth.currentUser!!.uid)
                    .addOnSuccessListener {
                        if(!it.isEmpty){
                            particpants.clear()
                            it.documents.forEach {
                                particpants.add(it.toObject(Participant::class.java))
                                screeningParticipants.clear()
                                screenings.forEach {s->
                                    particpants.forEach {p->
                                        if(s.participantId == p.id){
                                            screeningParticipants.add(ScreeningParticipant(s,p))
                                        }
                                    }
                                }
                            }

                                lastAdapter.notifyDataSetChanged()
                                dashboard_refresh_layout?.isRefreshing = false
                                dashboard_progressBar?.hide()
                        }else{
                                dashboard_refresh_layout?.isRefreshing = false
                                dashboard_progressBar?.hide()
                            }
                    }
                    .addOnCanceledListener {
                            dashboard_refresh_layout?.isRefreshing = false
                            dashboard_progressBar?.hide()
                            getString(R.string.network_error).showAsToast(activity!!)
                    }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        activity!!.menuInflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.action_add -> {
                startActivity(Intent(activity, NewScreeningActivity::class.java))
                true
            }
            R.id.action_sort -> {
                true
            }
            R.id.sort_only_jst -> {
                screenings.clear()
                screeningsContainer.filter { it.type == Type.JST.toString() }
                        .map {
                            screenings.add(it)
                            lastAdapter.notifyDataSetChanged()
                        }
                true
            }
            R.id.sort_only_mst -> {
                screenings.clear()
                screeningsContainer.filter { it.type == Type.MST.toString() }
                        .map {
                            screenings.add(it)
                            lastAdapter.notifyDataSetChanged()
                        }

                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }

    override fun onResume() {
        super.onResume()
        activity!!.title = getString(R.string.screenings)
        Log.d("ParticipantList", "Resumed")
    }
}