package com.dscfuta.topnotch.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.dscfuta.topnotch.R
import com.dscfuta.topnotch.adapter.UserRequestAdapter
import com.dscfuta.topnotch.data.DocumentLiveData
import com.dscfuta.topnotch.data.FireStoreRepo
import com.dscfuta.topnotch.databinding.FragmentUserRequestListBinding
import com.dscfuta.topnotch.model.FullRequest


class UserRequestList : Fragment() {

    lateinit var binding: FragmentUserRequestListBinding
    lateinit var adapter: UserRequestAdapter
    lateinit var fireStoreRepo: FireStoreRepo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_request_list, container, false)

        //Initializing
        val recyclerView = binding.itemUserFragRecyclerview
        val manager = LinearLayoutManager(context)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = manager
            addItemDecoration(DividerItemDecoration(context, manager.orientation))

        }

        fireStoreRepo = FireStoreRepo(context!!)
        val requestsCollection = fireStoreRepo.getUserRequestCollection()

        val requestsLiveData = DocumentLiveData(requestsCollection){

            Toast.makeText(context, it, Toast.LENGTH_LONG)
                    .show()

        }

        requestsLiveData.observe(this, Observer<List<FullRequest>>{

            if(it.isEmpty()){

            }else{
                val adapter = UserRequestAdapter(it, context!!)
                recyclerView.adapter = adapter
                binding.progressItemUserFrag.visibility = GONE

            }

        })

        return binding.root

    }

    //Swipe to delete
    fun swipeToDelete(position : Int){
        //TODO Swipe to delete on Recyclerview taking in the item position as a parameter, fill free to pass in the best parameter to delete
    }

}
