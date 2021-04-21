package edu.bluejack20_2.chantuy.views.user_profile

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.todolist.Text
import com.example.todolist.TextAdapter
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import edu.bluejack20_2.chantuy.GlideApp
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.repositories.UserRepository
import edu.bluejack20_2.chantuy.views.login.LoginActivity
import java.io.IOException
import java.lang.Exception


class UserProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var imageView: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_user_profile, container, false)

        val viewModel = UserProfileViewModel()

//        val recyclerView: RecyclerView = rootView.findViewById(R.id.curhat_recycler_view)

        //downloadImage


        imageView= rootView.findViewById(R.id.user_profile_image_view)

        //vv will be fixed
        try {
            val storageReference=FirebaseStorage.getInstance().getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/chantuy-app.appspot.com/o/imageUploads%2FYWlGIoLwwnhR1hUDtP2wilrMERu1?alt=media&token=186b600a-8b65-48da-8462-e6e48131a448")

            GlideApp.with(this).load(storageReference).into(imageView)

        }catch(e: Exception) {
        }

        Log.i("Testing","aaa"+viewModel.userPictureUrl)
//        Glide.with(this).load(storageReference).into(imageView)
        val curhatAdapter = TextAdapter(mutableListOf())
        val curhatRecyclerView: RecyclerView = rootView.findViewById(R.id.recent_post_rview)
        curhatRecyclerView.adapter = curhatAdapter
        curhatRecyclerView.layoutManager=LinearLayoutManager(this.activity)
        val replyAdapter = TextAdapter(mutableListOf())
        val replyRecyclerView: RecyclerView = rootView.findViewById(R.id.recent_reply_rview)
        replyRecyclerView.adapter = replyAdapter
        replyRecyclerView.layoutManager=LinearLayoutManager(this.activity)



        val nameView : TextView = rootView.findViewById(R.id.user_profile_name)
        val emailView : TextView = rootView.findViewById(R.id.user_profile_email)
        nameView.setText(viewModel.userName)
        emailView.setText(viewModel.userEmail)
        imageView.setOnClickListener{
            launchGallery()
        }
//        nameView.setOnTouchListener(View.OnTouchListener { v, motionEvent ->
//            when (motionEvent?.action){
//                MotionEvent.ACTION_UP -> {
//                    //view.performClick()
//
//                    val photoPickerIntent = Intent(Intent.ACTION_PICK)
//                    photoPickerIntent.type = "image/*"
//                    photoPickerIntent.action = Intent.ACTION_GET_CONTENT
//                    startActivityForResult(Intent.createChooser(photoPickerIntent,"Select Picture"), 1)
//
//                }
//            }
//            return@OnTouchListener true
//        })


//        Log.i("Testing Info", )

        val curhatCountView : TextView = rootView.findViewById(R.id.total_post)
        val totalPostObserver = Observer<Int>{totalPostCount->
            if(totalPostCount==1){
                curhatCountView.setText(""+ totalPostCount + " curhat posted")
            }else{
                curhatCountView.setText(""+ totalPostCount + " curhats posted")
            }
        }

        val replyCountView : TextView = rootView.findViewById(R.id.total_reply)

        val totalReplyObserver = Observer<Int>{totalReplyCount->
            if(totalReplyCount==1){
                replyCountView.setText(""+ totalReplyCount + " reply posted")
            }else{
                replyCountView.setText(""+ totalReplyCount + " replies posted")
            }
        }


        viewModel.curhatCount.observe(this,totalPostObserver)
        viewModel.replyCount.observe(this,totalReplyObserver)




        val logOutButton: Button = rootView.findViewById(R.id.log_out_button)
        logOutButton.setOnClickListener {
            AuthUI.getInstance()
                    .signOut(this.requireActivity())
                    .addOnCompleteListener {
                        val intent  = Intent(this.activity, LoginActivity::class.java)
                        startActivity(intent)
                        this.activity?.finish()
                    }
        }

        viewModel.recentCurhats.observe(viewLifecycleOwner, Observer {curhats ->

            if(!curhats.isEmpty()){
                curhatAdapter.clearAllText()
            }
            for (curhat in curhats){
                curhatAdapter.addText(Text(curhat.content))
            }

        })

        viewModel.recentReplies.observe(viewLifecycleOwner, Observer {replies ->
            if(!replies.isEmpty()){
                replyAdapter.clearAllText()
            }
            for (reply in replies){
                replyAdapter.addText(Text(reply.content))
            }

        })




        return rootView

    }


    val PICK_IMAGE_REQUEST = 71
    var filePath :Uri?=null
    var firebaseStore: FirebaseStorage? = FirebaseStorage.getInstance()
    var storageReference: StorageReference? = FirebaseStorage.getInstance().reference

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }
            filePath = data.data
            try {
                Log.i("Testing","Sampe sini")
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, filePath)
                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            uploadImage()
        }
    }
    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)

    }
    private fun uploadImage(){
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            val progressDialog = ProgressDialog(activity)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            val ref = storageReference?.child(
                    "imageUploads/"
                            + FirebaseAuth.getInstance().currentUser.uid
                )





            val temp=ref?.putFile(filePath!!)?.addOnSuccessListener { task->
                progressDialog.dismiss()
                Toast
                    .makeText(
                        this.activity,
                        "Image Uploaded!!",
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
                ?.addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast
                        .makeText(
                            this.activity,
                            "Failed " + e.message,
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }?.addOnProgressListener(OnProgressListener<UploadTask.TaskSnapshot?>() {
                    fun onProgress(
                        taskSnapshot: UploadTask.TaskSnapshot
                    ) {
                        val progress = (100.0
                                * taskSnapshot.bytesTransferred
                                / taskSnapshot.totalByteCount)
                        progressDialog.setMessage(
                            "Uploaded "
                                    + progress.toInt() + "%"
                        )

                    }
                })
                temp?.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    Log.i("Testing",""+ref.downloadUrl)
                    UserRepository.updateProfileImage(ref.downloadUrl.toString())
                    ref.downloadUrl
                }
            }

        }

    }

