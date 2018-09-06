package org.unesco.mgiep.dali.Fragments

import android.arch.lifecycle.ViewModelProviders
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.Toast
import com.github.nitrico.lastadapter.LastAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_pendingscreenings.*
import org.unesco.mgiep.dali.BR
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.FirebaseScreening
import org.unesco.mgiep.dali.Data.Type
import org.unesco.mgiep.dali.Data.ViewModels.ScreeningViewModel
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.FirebaseRepository
import org.unesco.mgiep.dali.Utility.showFragment
import org.unesco.mgiep.dali.databinding.ItemScreeningBinding

class PendingScreenings: Fragment() {

    private val lastAdapter: LastAdapter by lazy { initLastAdapter() }
    private val screenings = ObservableArrayList<FirebaseScreening>()
    private lateinit var firebaseRepository: FirebaseRepository
    private lateinit var mAuth: FirebaseAuth
    private lateinit var screeningViewModel: ScreeningViewModel

    fun initLastAdapter(): LastAdapter {
        return LastAdapter(screenings, BR.item)
                .map<FirebaseScreening, ItemScreeningBinding>(R.layout.item_screening) {
                    onBind {
                        if(it.binding.item!!.type == Type.JST.toString() ){
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
                .into(pending_screening_recycler)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        mAuth = FirebaseAuth.getInstance()
        firebaseRepository = FirebaseRepository()
        screeningViewModel = ViewModelProviders.of(activity!!).get(ScreeningViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_pendingscreenings, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        pending_screening_recycler.adapter = lastAdapter
        pending_screening_recycler.layoutManager = LinearLayoutManager(activity)

        fetchPendingScreenings()


        /*pending_swipe_layout.setOnRefreshListener {
            fetchPendingScreenings()
        }*/


    }

    private fun fetchPendingScreenings(){
        firebaseRepository.fetchPendingUserScreenings(mAuth.currentUser!!.uid)
                .addOnCompleteListener {
                    if(!it.result.isEmpty){
                        screenings.clear()
                        it.result.documents.forEach {
                            screenings.add(it.toObject(FirebaseScreening::class.java))
                           // pending_swipe_layout.isRefreshing = false
                        }
                    }else{
                        //pending_swipe_layout.isRefreshing = false
                        //show empty screen
                    }
                }
                .addOnCanceledListener {
                   // pending_swipe_layout.isRefreshing = false
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

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        fragment.showFragment(container = R.id.fragment_container,
                fragmentManager = activity!!.supportFragmentManager,
                addToBackStack = addToBackStack)
    }
}