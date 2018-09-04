package org.unesco.mgiep.dali.Fragments

import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.github.nitrico.lastadapter.LastAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.item_screening.view.*
import org.unesco.mgiep.dali.BR
import org.unesco.mgiep.dali.Dagger.MyApplication
import org.unesco.mgiep.dali.Data.FirebaseScreening
import org.unesco.mgiep.dali.Data.Type
import org.unesco.mgiep.dali.R
import org.unesco.mgiep.dali.Repositories.FirebaseRepository
import org.unesco.mgiep.dali.Utility.showFragment
import org.unesco.mgiep.dali.databinding.ItemScreeningBinding

class Dashboard: Fragment() {

    private val lastAdapter: LastAdapter by lazy { initLastAdapter() }
    private val screenings = ObservableArrayList<FirebaseScreening>()
    private lateinit var firebaseRepository: FirebaseRepository
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!!.application as MyApplication).component.inject(this)
        setHasOptionsMenu(true)
        firebaseRepository = FirebaseRepository()
        mAuth = FirebaseAuth.getInstance()
    }

    fun initLastAdapter(): LastAdapter {
        return LastAdapter(screenings, BR.item)
                .map<FirebaseScreening, ItemScreeningBinding>(R.layout.item_screening) {
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
                }
                .into(screening_recycler)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?=
            inflater.inflate(R.layout.fragment_dashboard, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screening_recycler.adapter = lastAdapter
        screening_recycler.layoutManager = LinearLayoutManager(activity)

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