package com.dscfuta.topnotch.ui


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dscfuta.topnotch.R
import com.dscfuta.topnotch.adapter.ServiceTypeAdapter
import com.dscfuta.topnotch.databinding.FragmentFullDetailsBinding
import net.alexandroid.shpref.ShPref


class FullDetails : Fragment(), View.OnClickListener {

    lateinit var docId : String
    lateinit var binding : FragmentFullDetailsBinding

    lateinit var name : String
    lateinit var phone : String
    lateinit var email: String
    lateinit var eventType : String
    lateinit var eventLocation : String
    lateinit var message : String

    //For Adapter
    lateinit var adapter : ServiceTypeAdapter
    lateinit var myList : List<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_full_details, container, false)

        getServiceType(this.context)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val safeArgs = FullDetailsArgs.fromBundle(arguments)

        name = safeArgs.name
        phone = safeArgs.phoneNumber
        email = safeArgs.email
        eventType = safeArgs.eventType
        eventLocation = safeArgs.eventLocation

        binding.apply {
            tvUserUserName.text = name
            tvUserPhone.text = phone
            tvUserMail.text = email
            tvEventType.text = eventType
            tvEventLocation.text = eventLocation
            tvEventDate.text = safeArgs.eventDate
        }

        binding.apply {
            actionIconCalls.setOnClickListener(this@FullDetails)
            actionIconSms.setOnClickListener(this@FullDetails)
            actionIconMail.setOnClickListener(this@FullDetails)
            actionIconWhatsapp.setOnClickListener(this@FullDetails)
        }

        message = "Good day $name," +
                "\n\n" +
                "Trust you are good." +
                "\n\n" +
                "You are getting this mail because you requested for some service(s) to be rendered by Top Notch"
    }

    private fun resolvePackageManager(intent: Intent, msg: String){
        if (intent.resolveActivity(context!!.packageManager) != null){
            startActivity(intent)
        }else{
            makeText(context, "$msg App not installed / configured", Toast.LENGTH_LONG).show()
        }
    }
    //Make Call
    private fun makeCall(number : String){
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$number")
        }
        resolvePackageManager(intent, "Phone")
    }

    //Send text Message
    private fun sendTextMessage(phoneNo : String){
        val intent = Intent(Intent.ACTION_SEND).apply {
            data = Uri.parse("smsto:$phoneNo")  // This ensures only SMS apps respond
            putExtra("sms_body", message)
        }
        resolvePackageManager(intent, "SMS")
    }

    //Send a mail
    private fun sendAMail(email : String){
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")

            putExtra(Intent.EXTRA_EMAIL, "")
            putExtra(Intent.EXTRA_SUBJECT, "[Urgent] Top Notch - Euphoria Response")
            putExtra(Intent.EXTRA_TEXT, message)
        }
        resolvePackageManager(intent, "Email")
    }

    //Send WhatsApp message
    private fun chatOnWhatsApp(phoneNo: String){
        var newNumber : String? = null
        if (phoneNo.startsWith("0")){
            newNumber = phoneNo.replaceFirst("0", "+234")
        }

        val uri: Uri = Uri.parse("https://api.whatsapp.com/send?phone=$newNumber&text=$message")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        if(context!!.packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA) != null){
            startActivity(intent)
        }else{
            makeText(context, "Opps! It seems WhatsApp is uninstalled", Toast.LENGTH_LONG).show()
        }
    }

    /**Perform clicking Options*/
    override fun onClick(v: View?) {
        val itemId = v!!.id
        when(itemId){
            R.id.action_icon_calls -> makeCall(phone)
            R.id.action_icon_sms -> sendTextMessage(phone)
            R.id.action_icon_mail -> sendAMail(email)
            R.id.action_icon_whatsapp -> chatOnWhatsApp(phone)
        }
    }

    private fun getServiceType(context: Context?){
        myList = ShPref.getListOfStrings("service_type")

        adapter = ServiceTypeAdapter(myList, context!!)

        val manager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
        manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

        binding.rvServiceType.setHasFixedSize(true)
        binding.rvServiceType.layoutManager = manager
        binding.rvServiceType.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        /*super.onCreateOptionsMenu(menu, inflater)*/
        menu!!.clear()
        inflater!!.inflate(R.menu.full_details_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when(id){
            R.id.menu_action_delete -> makeText(context, "Delete CLicked", Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }
}
