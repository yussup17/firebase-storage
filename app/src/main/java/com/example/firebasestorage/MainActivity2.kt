package com.example.firebasestorage
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.squareup.picasso.Picasso


class MainActivity2 : AppCompatActivity() {
    private var mButtonChooseImage: Button? = null
    private var mButtonUpload: Button? = null
    private var mTextViewShowUploads: Button? = null
    private var mEditTextFileName: EditText? = null
    private var mImageView: ImageView? = null
    private var mProgressBar: ProgressBar? = null
    private var mImageUri: Uri? = null
    private var mStorageRef: StorageReference? = null
    private var mDatabaseRef: DatabaseReference? = null
    private var mUploadTask: StorageTask<*>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        mButtonChooseImage = findViewById(R.id.button_choose_image)
        mButtonUpload = findViewById(R.id.button_upload)
        mTextViewShowUploads = findViewById(R.id.btn_show_uploads)
        mEditTextFileName = findViewById(R.id.edit_text_file_name)
        mImageView = findViewById(R.id.imageView)
        mProgressBar = findViewById(R.id.progress_bar)
        mProgressBar = findViewById(R.id.progress_bar)
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads")
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads")


        //Chhosing an Image from Gallery
        mButtonChooseImage!!.setOnClickListener(View.OnClickListener { openFileChooser() })
        //Uploading an Image
        mButtonUpload!!.setOnClickListener(View.OnClickListener {
            if (mUploadTask != null && mUploadTask!!.isInProgress) {
                Toast.makeText(this@MainActivity2, "Upload in Progress", Toast.LENGTH_LONG).show()
            } else {
                uploadFile()
            }
        })
        //Showing uploaded images
        mTextViewShowUploads!!.setOnClickListener(View.OnClickListener { openImagesActivity() })
    }

    fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode ==
            RESULT_OK && data != null && data.data != null
        ) {
            mImageUri = data.data
            Picasso.get().load(mImageUri).into(mImageView)
            //You Can Also Use
            //mImageView.setImageURI(mImageUri);
        }
    }

    // Getting an extension from our file (eg. Jpeg,Png,Jpg etc)
    private fun getFileExtension(uri: Uri): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    private fun uploadFile() {
        if (mImageUri != null) {
            var time = System.currentTimeMillis().toString()
            val fileReference = mStorageRef!!.child(
                time+ "." + getFileExtension(mImageUri!!)
            )
            mUploadTask = fileReference.putFile(mImageUri!!).addOnSuccessListener { taskSnapshot ->

                //Success
                val delay: Thread = object : Thread() {
                    override fun run() {
                        try {
                            sleep(500)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }
                }
                delay.start()
                fileReference.downloadUrl.addOnSuccessListener { uri ->
                    var mDownloadUrl = uri.toString()
                    //val upload = Upload(mEditTextFileName!!.text.toString().trim(), "https://firebasestorage.googleapis.com/v0/b/fir-storage-app-5caa1.appspot.com/o/uploads%2F1643280913761.jpg?alt=media&token=5b6fd8e0-37d6-4c31-a444-a8c6b1b26357", time      )
                    val upload = Upload(mEditTextFileName!!.text.toString().trim(), uri.toString(), time      )
                    val uploadId = mDatabaseRef!!.push().key
                    mDatabaseRef!!.child(System.currentTimeMillis().toString() + "")
                        .setValue(upload)
                }
                Toast.makeText(this@MainActivity2, "Upload Successful", Toast.LENGTH_LONG).show()
                
            }.addOnFailureListener { e ->
                //Failure
                Toast.makeText(this@MainActivity2, e.message, Toast.LENGTH_SHORT).show()
            }.addOnProgressListener { taskSnapshot ->

                //Updating the Progress Bar
                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                mProgressBar!!.progress = progress.toInt()
            }
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openImagesActivity() {
        val intent = Intent(applicationContext, ProductsActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private val PICK_IMAGE_REQUEST = 1
    }
}

