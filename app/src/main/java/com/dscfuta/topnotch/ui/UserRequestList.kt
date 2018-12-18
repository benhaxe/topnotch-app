package com.dscfuta.topnotch.ui


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dscfuta.topnotch.R
import com.dscfuta.topnotch.adapter.UserRequestAdapter
import com.dscfuta.topnotch.data.RequestsViewModel
import com.dscfuta.topnotch.databinding.FragmentUserRequestListBinding
import com.dscfuta.topnotch.model.FullRequest
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.dscfuta.topnotch.helpers.drawableToBitmap


class UserRequestList : Fragment(){
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

        recyclerView = binding.itemUserFragRecyclerview
        val manager = LinearLayoutManager(context)

        /**
         * [apply]Calls the specified function [block] with `this` value as its receiver and returns `this` value.
         */
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = manager
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

            //CHeck if the List is empty
            if(it.isEmpty()){
                //Enables No request yet placeholder
                binding.noRequestsLayout.visibility = VISIBLE
                binding.progressItemUserFrag.visibility = GONE
            }else{
                //Displays Requests
                adapter = UserRequestAdapter(it, this@UserRequestList.activity!!)
                adapter.notifyDataSetChanged()

                //Set the adapter if it is not Empty
                recyclerView.adapter = adapter
                binding.progressItemUserFrag.visibility = GONE
            }
        })

        //Sets up swipe to delete
        ItemTouchHelper(RecyclerSwiper()).attachToRecyclerView(recyclerView)

        return binding.root

    }


    //Swipe to delete class
    inner class RecyclerSwiper : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            //When Swiped delete particular request through the id passed in to the itemView's tag
            //We pass in the path here
            viewModel.deleteRequest(viewHolder.itemView.tag.toString(), {
                Toast.makeText(context!!, "Request deleted successfully", Toast.LENGTH_LONG)
                        .show()
                adapter.notifyDataSetChanged()

            }, {

                Toast.makeText(context!!, "Error: $it", Toast.LENGTH_LONG)
                        .show()

            })

            adapter.notifyDataSetChanged()

        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                //Get recyclerView item from the viewHolder

                val itemView = viewHolder.itemView

                val paint = Paint()
                val icon : Bitmap

                if (dX > 0){
                    //For Left to Right swiping
                    icon = drawableToBitmap(drawable = resources.getDrawable(R.drawable.ic_delete))
                    paint.setARGB(255, 255, 0, 0)

                    c.drawRect(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat(), paint)
                    c.drawBitmap(
                            icon,
                            itemView.left.toFloat() + convertDpToPx(16),
                            itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top - icon.height) / 2,paint)
                }else{
                    //For Right to Left swiping
                    icon = drawableToBitmap(drawable = resources.getDrawable(R.drawable.ic_delete))
                    paint.setARGB(255, 255, 0, 0)

                    c.drawRect(
                            itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat(), paint)
                    c.drawBitmap(
                            icon,
                            itemView.right.toFloat() - convertDpToPx(16) - icon.width,
                            itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top - icon.height) / 2,paint)
                }

                val alpha = 1.0f - Math.abs(dX) / viewHolder.itemView.width.toFloat()
                viewHolder.itemView.alpha = alpha
                viewHolder.itemView.translationX = dX
            }else{
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
    }

    private fun convertDpToPx(dp : Int) : Int{
        return  Math.round(dp * (resources).displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)
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
