package com.dscfuta.topnotch.ui


import android.os.Bundle
import android.view.*
import android.view.View.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.dscfuta.topnotch.R
import com.dscfuta.topnotch.ReloadAdapterData
import com.dscfuta.topnotch.adapter.UserRequestAdapter
import com.dscfuta.topnotch.adapter.OnUserRequestItemClicListener
import com.dscfuta.topnotch.data.RequestsViewModel
import com.dscfuta.topnotch.databinding.FragmentUserRequestListBinding
import com.dscfuta.topnotch.model.FullRequest


class UserRequestList : Fragment(), OnUserRequestItemClicListener, ReloadAdapterData {
    override fun onUserRequestItemClick(v: View,
                                        name: String,
                                        phoneNumber: String,
                                        eventLocation: String,
                                        eventType: String,
                                        email: String,
                                        id: String /* The Id to be used in retrieving the document in the fullDetailsFragment*/
    ) {

        //Implementing the androidx navigation component -- Passing a buttload of parameters here
        //Todo: The service type list should be retrieved in the fullDetailsFragment with the id from the fireStore Database..
        Navigation.findNavController(v)
                .navigate(UserRequestListDirections.actionUserRequestListToFullDetails(name, phoneNumber, eventLocation, eventType, email, id))
    }


    lateinit var binding: FragmentUserRequestListBinding
    lateinit var adapter: UserRequestAdapter
    lateinit var viewModel: RequestsViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var originalUserRequestList: List<FullRequest>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_request_list, container, false)
        viewModel = ViewModelProviders.of(this).get(RequestsViewModel::class.java)
        setHasOptionsMenu(true)

        //Initializing

        recyclerView = binding.itemUserFragRecyclerview
        val manager = LinearLayoutManager(context)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = manager

            //Gives the fancy gray line between viewHolders
            addItemDecoration(DividerItemDecoration(context, manager.orientation))

        }

        binding.progressItemUserFrag.visibility = VISIBLE
        binding.noRequestsLayout.visibility = INVISIBLE

        //Converting and parsing through live data
        val requestsLiveData = viewModel.getRequests{
            Toast.makeText(context, it, Toast.LENGTH_LONG)
                    .show()
        }

        //Observing..
        requestsLiveData.observe(this, Observer<List<FullRequest>>{

            originalUserRequestList = it

            if(it.isEmpty()){

                //Enables No request yet placeholder
                binding.noRequestsLayout.visibility = VISIBLE
                binding.progressItemUserFrag.visibility = GONE


            }else{

                //Displays Requests
                 adapter = UserRequestAdapter(it, this@UserRequestList.activity!!)
                adapter.notifyDataSetChanged()

                recyclerView.adapter = adapter
                binding.progressItemUserFrag.visibility = GONE

            }

        })

        //Failed Swipe Mission :(

//        ItemTouchHelper(RecyclerSwiper())
//                .attachToRecyclerView(recyclerView)

        //Reload button's retry function..



        return binding.root

    }


    //That failed swipe mission's class
//    inner class RecyclerSwiper : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
//        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
//
//            return false
//        }
//
//        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//
//            //When Swiped delete particular request through the id passed in to the itemView's tag
//
//            viewHolder.itemView.visibility = GONE
//            viewModel.deleteRequest(viewHolder.itemView.tag.toString(), {
//                Toast.makeText(context!!, "Request deleted successfully", Toast.LENGTH_LONG)
//                        .show()
//
//
//                adapter.notifyDataSetChanged()
//            }, {
//                Toast.makeText(context!!, "Error: $it", Toast.LENGTH_LONG)
//                        .show()
//            })
//
//            adapter.notifyDataSetChanged()
//
//        }
//
//    }

     override fun reloadData(){

        binding.progressItemUserFrag.visibility = VISIBLE
        binding.noRequestsLayout.visibility = GONE

        //Converting and parsing through live data
        val requestsLiveData = viewModel.getRequests{
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
                adapter = UserRequestAdapter(it, context!!)
                adapter.notifyDataSetChanged()

                recyclerView.adapter = adapter
                binding.progressItemUserFrag.visibility = GONE

            }

        })



    }

    //Function that is supposed to refill the adapter as new text is typed into the search view..
//    fun onQueryTextSubmit( newText: String){
//
//        val searchedRequestList: MutableList<FullRequest> = mutableListOf()
//        for (request in returnNewList()){
//            val serviceListType = request.userRequest.service_type
//            for (serviceType in serviceListType){
//                if (serviceType.toLowerCase().contains(newText.toLowerCase())){
//                    searchedRequestList.add(request)
//                }
//            }
//        }
//
//        adapter = UserRequestAdapter(searchedRequestList, context!!)
//        recyclerView.adapter = adapter
//
//    }


}
