package com.dscfuta.topnotch

import android.app.ProgressDialog
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.dscfuta.topnotch.data.RequestsViewModel
import com.dscfuta.topnotch.helpers.OnDeleteRequestButtonClickListener
import com.dscfuta.topnotch.helpers.OnUserRequestItemClickListener
import com.dscfuta.topnotch.ui.UserRequestListDirections
import com.google.firebase.iid.FirebaseInstanceId
import net.alexandroid.shpref.ShPref
import java.io.IOException
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.messaging.FirebaseMessaging



class MainActivity : AppCompatActivity(), OnUserRequestItemClickListener, OnDeleteRequestButtonClickListener{

    private lateinit var viewModel: RequestsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()

        viewModel = ViewModelProviders.of(this).get(RequestsViewModel::class.java)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun initView(){
        //Used to fetch token
        Thread(Runnable {
            try{
                makeText(this@MainActivity, "" + FirebaseInstanceId.getInstance().instanceId, Toast.LENGTH_LONG).show()
                Log.d(this@MainActivity.toString(), "This Fuck"+ FirebaseInstanceId.getInstance().instanceId)
            }catch (e: IOException){
                e.printStackTrace()
                Log.d(this@MainActivity.toString(), "This error: ${e.message}")
            }
        })

        /*FirebaseMessaging.getInstance().subscribeToTopic("Owner").addOnSuccessListener { Toast.makeText(applicationContext, "Success", Toast.LENGTH_LONG).show() }*/
    }

    override fun onUserRequestItemClick(v: View,
                                        name: String,
                                        phoneNumber: String,
                                        eventLocation: String,
                                        eventType: String,
                                        email: String,
                                        eventDate: String,
                                        mService: List<String>,
                                        id: String
    ) {

        //Implementing the androidx navigation component -- Passing a buttload of parameters here
        //Todo: The service type list should be retrieved in the fullDetailsFragment with the id from the fireStore Database..
        Navigation.findNavController(v)
                .navigate(UserRequestListDirections.actionUserRequestListToFullDetails(name, phoneNumber, eventLocation, eventType, email, eventDate, id))

        Log.d(ContentValues.TAG, "My Service Main: $mService")

        //Put the service list into this place
        ShPref.putList("service_type", mService)
    }

    //When the delete button is clicked
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
}
