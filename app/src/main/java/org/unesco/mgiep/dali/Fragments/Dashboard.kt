package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.Toast
import com.github.nitrico.lastadapter.LastAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.item_screening.view.*
import kotlinx.android.synthetic.main.layout_menu.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.unesco.mgiep.dali.BR
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.AppPref
import org.unesco.mgiep.dali.Data.Screening
import org.unesco.mgiep.dali.Data.Participant
import org.unesco.mgiep.dali.Data.Type
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.R.id.action_sort
import org.unesco.mgiep.dali.Repositories.FirebaseRepository
import org.unesco.mgiep.dali.Utility.showFragment
import org.unesco.mgiep.dali.databinding.ItemScreeningBinding

class Dashboard: Fragment() {

    private val lastAdapter: LastAdapter by lazy { initLastAdapter() }
    private val screeningsContainer = ObservableArrayList<Screening>()
    private val screenings = ObservableArrayList<Screening>()
    private val participants = ObservableArrayList<Participant>()
    private lateinit var firebaseRepository: FirebaseRepository
    private lateinit var mAuth: FirebaseAuth
    private lateinit var screeningViewModel: ScreeningViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        setHasOptionsMenu(true)
        firebaseRepository = FirebaseRepository()
        mAuth = FirebaseAuth.getInstance()
        screeningViewModel = ViewModelProviders.of(activity!!).get(ScreeningViewModel::class.java)
    }

    fun initLastAdapter(): LastAdapter {
        return LastAdapter(screenings, BR.item)
                .map<Screening, ItemScreeningBinding>(R.layout.item_screening) {
                    onBind {

                        if(it.binding.item!!.type == Type.JST.toString() ){
                            if(it.binding.item!!.totalScore < 16){
                                it.itemView.item_layout.background = resources.getDrawable(R.drawable.rectangle_green)
                            }else{
                                it.itemView.item_layout.background = resources.getDrawable(R.drawable.rectangle_red)
                            }
                        }else{
                            if(it.binding.item!!.totalScore < 19){
                                it.itemView.item_layout.background = resources.getDrawable(R.drawable.rectangle_green)
                            }else{
                                it.itemView.item_layout.background = resources.getDrawable(R.drawable.rectangle_red)
                            }
                        }
                    }
                    onClick {
                        screeningViewModel.select(it.binding.item!!)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_dashboard, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screening_recycler.adapter = lastAdapter
        screening_recycler.layoutManager = LinearLayoutManager(activity)


        fetchScreenings()


       /* screening_swipe_layout.setOnRefreshListener {
            fetchScreenings()
        }*/

    }

    private fun fetchScreenings(){
        firebaseRepository.fetchUserScreenings(mAuth.currentUser!!.uid)
                .addOnSuccessListener {
                    if(!it.isEmpty){
                        screenings.clear()
                        it.documents.forEach {
                            screeningsContainer.add(it.toObject(Screening::class.java))
                            screenings.add(it.toObject(Screening::class.java))
                            //screening_swipe_layout.isRefreshing = false
                            lastAdapter.notifyDataSetChanged()
                        }
                    }else{
                        //show empty screen
                        //screening_swipe_layout.isRefreshing = false
                    }
                }
                .addOnCanceledListener {
                    //screening_swipe_layout.isRefreshing = false
                    Toast.makeText(activity,"Error While Fetching Data! Check Network Connection.", Toast.LENGTH_SHORT).show()
                }
    }

    private fun fetchParticipants(){
        firebaseRepository.fetchParticipants(mAuth.currentUser!!.uid)
                .addOnSuccessListener {
                    if(!it.isEmpty){
                        screenings.clear()
                        it.documents.forEach {
                            participants.add(it.toObject(Participant::class.java))
                        }
                            //screening_swipe_layout.isRefreshing = false }
                    }else{
                        //show empty screen
                        //screening_swipe_layout.isRefreshing = false
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(activity,"Error While Fetching Data! Check Network Connection.", Toast.LENGTH_SHORT).show()
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
            R.id.action_sort -> {
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        activity!!.menuInflater.inflate(R.menu.sort_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        return when(item!!.itemId){
            R.id.sort_only_jst -> {
                screeningsContainer.filter { it.type == "jst" }
                        .map {
                            screenings.add(it)
                            lastAdapter.notifyDataSetChanged()
                        }
                true
            }
            R.id.sort_only_mst -> {
                screeningsContainer.filter { it.type == "mst" }
                        .map {
                            screenings.add(it)
                            lastAdapter.notifyDataSetChanged()
                        }
                true
            }
            else -> {
                super.onContextItemSelected(item)
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
        activity!!.title = "Screenings"
        Log.d("ParticipantList","Resumed")
    }
}