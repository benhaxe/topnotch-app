package com.dscfuta.topnotch.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.dscfuta.topnotch.R
import com.dscfuta.topnotch.adapter.UserRequestAdapter
import com.dscfuta.topnotch.adapter.onUserRequestItemClicListener
import com.dscfuta.topnotch.data.DocumentLiveData
import com.dscfuta.topnotch.data.FireStoreRepo
import com.dscfuta.topnotch.databinding.FragmentUserRequestListBinding
import com.dscfuta.topnotch.model.FullRequest


class UserRequestList : Fragment(), onUserRequestItemClicListener {
    override fun onUserRequestItemClick(v: View,
                                        name: String,
                                        phoneNumber: String,
                                        eventLocation: String,
                                        eventType: String,
                                        email: String,
                                        serviceType: List<String>,
                                        id: String) {

        //Implementing the androidx navigation component -- Passing a buttload of parameters here
        //Todo: The service type list should be retrieved in the fullDetailsFragment with the id from the fireStore Database..
        Navigation.findNavController(v)
                .navigate(UserRequestListDirections.actionUserRequestListToFullDetails(name, phoneNumber, eventLocation, eventType, email, id))
    }


    lateinit var binding: FragmentUserRequestListBinding
    lateinit var adapter: UserRequestAdapter
    lateinit var fireStoreRepo: FireStoreRepo
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_request_list, container, false)

        //Initializing

        recyclerView = binding.itemUserFragRecyclerview
        val manager = LinearLayoutManager(context)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = manager
            addItemDecoration(DividerItemDecoration(context, manager.orientation))

        }

        //Still Initializing
        ItemTouchHelper(RecyclerSwiper())
                .attachToRecyclerView(recyclerView)

        //Real Work..
        loadRequests()

        //Reload button's retry function..
        binding.reloadRequests.setOnClickListener {
            loadRequests()
        }

        return binding.root

    }


    inner class RecyclerSwiper : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            //Do Nothing
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            //When Swiped delete particular request through the id passed in to the itemView's tag
            fireStoreRepo.deleteRequest(viewHolder.itemView.tag.toString(), {
                Toast.makeText(context!!, "Request deleteduh successfulay", Toast.LENGTH_LONG)
                        .show()
            }, {
                Toast.makeText(context!!, "Error: $it", Toast.LENGTH_LONG)
                        .show()
            })

        }

    }

    fun loadRequests(){

        binding.progressItemUserFrag.visibility = VISIBLE
        binding.noRequestsLayout.visibility = GONE

        //Retrieving the user requests collection from firestore
        fireStoreRepo = FireStoreRepo(context!!)
        val requestsCollection = fireStoreRepo.getUserRequestCollection()

        //Converting and parsing through live data
        val requestsLiveData = DocumentLiveData(requestsCollection){
            Toast.makeText(context, it, Toast.LENGTH_LONG)
                    .show()
        }

        //Observing..
        requestsLiveData.observe(this, Observer<List<FullRequest>>{

            if(it.isEmpty()){

                //Enables No request yet placeholder
                binding.noRequestsLayout.visibility = VISIBLE
                binding.progressItemUserFrag.visibility = GONE


            }else{
                //Displays Requests
                val adapter = UserRequestAdapter(it, context!!)
                recyclerView.adapter = adapter

                binding.progressItemUserFrag.visibility = GONE

            }

        })

    }

}
