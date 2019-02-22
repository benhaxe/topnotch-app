package com.dscfuta.topnotch.ui


import android.app.SearchManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View.*
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
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import com.dscfuta.topnotch.helpers.drawableToBitmap
import com.google.firebase.auth.FirebaseAuth


class UserRequestList : Fragment(), SearchView.OnQueryTextListener{

    lateinit var binding: FragmentUserRequestListBinding
    lateinit var adapter: UserRequestAdapter
    lateinit var viewModel: RequestsViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var originalUserRequestList: List<FullRequest>


    //FOr search purpose
    lateinit var searchView: SearchView

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

    private fun signOut(){
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(activity, LoginActivity::class.java))
        activity!!.finish()
    }

    /*@RequiresApi(Build.VERSION_CODES.HONEYCOMB)*/
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.main_menu, menu!!)
        val searchItem = menu.findItem(R.id.main_menu_search)

        val searchManager : SearchManager= activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        if(searchItem != null){
            searchView = searchItem.actionView as SearchView
        }
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))

        searchView.setOnQueryTextListener(this)
        searchView.queryHint= "Search Requests..."
        searchView.isIconified = false

        //Remove the Search icon that shows inside the search edit text view
        val magId = resources.getIdentifier("android:id/search_mag_icon", null, null)
        val magImage = searchView.findViewById<ImageView>(magId)
        magImage.layoutParams = LinearLayout.LayoutParams(0, 0)

        magImage.visibility = GONE

        searchView.setIconifiedByDefault(false)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val itemId = item!!.itemId
        when(itemId){
            R.id.sign_out -> signOut()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.i(TAG, "onQueryTextSubmit + $query")
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.i(TAG, "onQueryTextChange + $newText")
        val myT = newText!!.toLowerCase()

        val searchedRequestList: MutableList<FullRequest> = mutableListOf()

        for ( userRequest in originalUserRequestList){
            val name = userRequest.userRequest.name.toLowerCase()

            if(name.contains(myT)){
                searchedRequestList.add(userRequest)
            }
            adapter = UserRequestAdapter(searchedRequestList, context!!)
            recyclerView.adapter = adapter
        }
        return true
    }
}
