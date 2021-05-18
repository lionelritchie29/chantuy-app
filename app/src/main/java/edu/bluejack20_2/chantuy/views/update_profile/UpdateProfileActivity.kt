package edu.bluejack20_2.chantuy.views.update_profile

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import edu.bluejack20_2.chantuy.GlideApp
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.repositories.UserRepository
import java.io.IOException
import java.util.*

class UpdateProfileActivity : AppCompatActivity() {
    val viewModel= UpdateProfileViewModel()
    lateinit var imageView: ImageView
    var cldr: Calendar = Calendar.getInstance()
    var day: Int = cldr.get(Calendar.DAY_OF_MONTH)
    var month: Int = cldr.get(Calendar.MONTH)
    var year: Int = cldr.get(Calendar.YEAR)


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        var picker : DatePickerDialog

        val userEditText: EditText = this.findViewById(R.id.update_user_name)

        val dobButton: TextView = this.findViewById(R.id.update_dob_submit)
        UserRepository.getUserById(FirebaseAuth.getInstance().currentUser.uid).get().addOnSuccessListener {
            viewModel.userName=it["name"].toString()
            viewModel.genderString=it["gender"].toString()
            viewModel.dob= it.getTimestamp("dateOfBirth")!!
            dobButton.setText(viewModel.dob.toDate().date.toString() + "/" + (viewModel.dob.toDate().month + 1).toString() + "/" + (viewModel.dob.toDate().year+1900).toString())
            userEditText.setText(viewModel.userName)

        }

        val submitButton: Button = this.findViewById(R.id.update_submit_btn)
        val genderSpinner: Spinner = this.findViewById(R.id.update_profile_spinner_gender)

        viewModel.dob= Timestamp.now()
        dobButton.setOnClickListener {
            picker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    dobButton.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                    this.year=year
                    this.month=monthOfYear
                    this.day=dayOfMonth

                },
                year,
                month,
                day
            )

            picker.show()





            viewModel.dob= Timestamp(Date(year-1900,month,day,0,0))


        }

        val options = arrayOf("Female","Male")



        setGenderSpinner(genderSpinner, options)

        submitButton.setOnClickListener{
            viewModel.dob= Timestamp(Date(year-1900,month,day,0,0))
            viewModel.userName=userEditText.text.toString()
            UserRepository.userUpdateProfile(viewModel.userName,viewModel.genderString,viewModel.dob)

            finish()

        }

    }

    private fun setGenderSpinner(
        genderSpinner: Spinner,
        options: Array<String>) {
        genderSpinner.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)
        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.genderString = "Male"

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.genderString = options.get(position)


            }
        }
    }
}