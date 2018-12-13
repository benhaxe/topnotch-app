package com.dscfuta.topnotch

import android.app.ProgressDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.dscfuta.topnotch.adapter.OnDeleteRequestButtonClickListener
import com.dscfuta.topnotch.adapter.OnUserRequestItemClicListener
import com.dscfuta.topnotch.data.RequestsViewModel
import com.dscfuta.topnotch.ui.UserRequestList
import com.dscfuta.topnotch.ui.UserRequestListDirections
import com.miguelcatalan.materialsearchview.MaterialSearchView

class MainActivity : AppCompatActivity(), OnUserRequestItemClicListener, OnDeleteRequestButtonClickListener {

    private lateinit var viewModel: RequestsViewModel
    lateinit var userRequestList: ReloadAdapterData
    lateinit var searchView: MaterialSearchView

    override fun onDeleteButtonClicked(id: String) {

        val dialog = AlertDialog.Builder(this)
        dialog.setMessage("Are you sure you want to delete this request")
        dialog.setPositiveButton("YES") {
            dialog__, which ->

            val dialog2 = ProgressDialog(this)
            dialog2.setMessage("Deleting..")
            dialog2.setCanceledOnTouchOutside(false)
            dialog2.show()


            viewModel.deleteRequest(id, {

                dialog2.dismiss()

            }, {
                dialog2.dismiss()
                Toast.makeText(this, "An error occurred. Please check your network connection and try again..",
                        Toast.LENGTH_LONG)
                        .show()
            })
        }

        dialog.setNegativeButton("NO"){
            dialog__, which ->

            dialog__.dismiss()
        }

        val alert = dialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()


    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(RequestsViewModel::class.java)
        userRequestList = UserRequestList()
        searchView = findViewById(R.id.searchView)
        searchView.showSuggestions()

        val userRequestList = UserRequestList()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //TODO: For every new query submitted in the search view.. The recycler view should be refilled
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if(!newText.isNullOrEmpty()){
                    //TODO: For every new query put in the search view.. The recycler view should be refilled
                }

                return true
            }

        })

        searchView.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener{
            override fun onSearchViewClosed() {
                //TODO: When the search view is closed, the recycler view should return to normal
            }

            override fun onSearchViewShown() {

            }

        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu!!)

        //Initializes the search view with the menu item
        searchView.setMenuItem(menu.findItem(R.id.main_menu_search))
        return super.onCreateOptionsMenu(menu)

    }

    override fun onBackPressed() {
        super.onBackPressed()

        if(searchView.isSearchOpen)searchView.closeSearch()
    }
}

interface ReloadAdapterData{
    fun reloadData()
}
