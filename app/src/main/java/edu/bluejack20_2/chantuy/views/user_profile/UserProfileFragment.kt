package edu.bluejack20_2.chantuy.views.user_profile

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.auth.AuthUI
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import edu.bluejack20_2.chantuy.GlideApp
import edu.bluejack20_2.chantuy.R
import edu.bluejack20_2.chantuy.databinding.FragmentUserProfileBinding
import edu.bluejack20_2.chantuy.repositories.UserRepository
import edu.bluejack20_2.chantuy.utils.CurhatUtil
import edu.bluejack20_2.chantuy.utils.UserUtil
import edu.bluejack20_2.chantuy.views.ProfileCurhatPostedAdapter
import edu.bluejack20_2.chantuy.views.ProfileRepliedCurhatAdapter
import edu.bluejack20_2.chantuy.views.login.LoginActivity
import edu.bluejack20_2.chantuy.views.Text
import edu.bluejack20_2.chantuy.views.TextAdapter
import edu.bluejack20_2.chantuy.views.update_password.UpdatePasswordActivity
import edu.bluejack20_2.chantuy.views.update_profile.UpdateProfileActivity
import java.io.IOException
import java.lang.Exception
import java.util.*


class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false)

        val viewModel = UserProfileViewModel()

        try {
            val storageReference=FirebaseStorage.getInstance().getReferenceFromUrl(viewModel
                .currUser.photoUrl.toString())

            GlideApp.with(this)
                .load(storageReference)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.userProfileImageView)

        }catch(e: Exception) {

        }

        binding.swipeContainer.setOnRefreshListener(object: SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                viewModel.getRecentReplies()
                viewModel.getRecentCurhats()
                binding.swipeContainer.isRefreshing = false
            }
        })

        val curhatAdapter = ProfileCurhatPostedAdapter()
        binding.recentPostRview.adapter = curhatAdapter
        binding.recentPostRview.layoutManager = object : LinearLayoutManager(binding.root.context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        val replyAdapter = ProfileRepliedCurhatAdapter()
        binding.recentReplyRview.adapter = replyAdapter
        binding.recentReplyRview.layoutManager = object : LinearLayoutManager(binding.root.context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        UserRepository.getCurrentUser {
            binding.userProfileName.text = it?.name
            binding.userProfileEmail.text = it?.email
            binding.userProfileJoinedAt.text = UserUtil.formatDate(it?.joinedAt)
        }

        binding.userProfileImageView.setOnClickListener{
            launchGallery()
        }

        val totalPostObserver = Observer<Int>{totalPostCount->
            binding.totalPost.text = "${totalPostCount} ${if (totalPostCount == 1) " curhat posted" else " curhats posted"}"
        }

        val totalReplyObserver = Observer<Int>{totalReplyCount->
            binding.totalReply.text = "${totalReplyCount} ${if (totalReplyCount == 1) " reply posted" else " replies posted"}"
        }

        viewModel.curhatCount.observe(viewLifecycleOwner,totalPostObserver)
        viewModel.replyCount.observe(viewLifecycleOwner,totalReplyObserver)


        binding.userCpButton.setOnClickListener {
            val intent = Intent(this.activity, UpdatePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.userUprofBtn.setOnClickListener {
            val intent = Intent(this.activity, UpdateProfileActivity::class.java)
            startActivity(intent)
        }

        binding.logOutButton.setOnClickListener {
            AuthUI.getInstance()
                    .signOut(this.requireActivity())
                    .addOnCompleteListener {
                        val intent  = Intent(this.activity, LoginActivity::class.java)
                        startActivity(intent)
                        this.activity?.finish()
                    }
        }

        viewModel.recentCurhats.observe(viewLifecycleOwner, Observer {curhats ->
            if (curhats.isEmpty()) {
                binding.profileNoCurhatPosted.visibility = View.VISIBLE
            } else {
                binding.profileNoCurhatPosted.visibility = View.INVISIBLE
            }
            curhatAdapter.submitList(curhats)
        })

        viewModel.recentReplies.observe(viewLifecycleOwner, Observer {replies ->
            if (replies.isEmpty()) {
                binding.profileNoReplyPosted.visibility = View.VISIBLE
            } else {
                binding.profileNoReplyPosted.visibility = View.INVISIBLE
            }

            replyAdapter.submitList(replies)
        })

        return binding.root
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

                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, filePath)
                binding.userProfileImageView.setImageBitmap(bitmap)
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
                task.storage.downloadUrl.addOnSuccessListener {uri->
                    ref.downloadUrl.addOnSuccessListener {uri->
                        UserRepository.updateProfileImage(uri)
                    }
                }
                Toast
                    .makeText(
                        this.activity,
                        getString(R.string.toast_img_up),
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
            }

        }

    }

