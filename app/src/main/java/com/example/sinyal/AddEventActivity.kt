package com.example.sinyal

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.UserPreferences
import com.example.sinyal.databinding.ActivityAddEventBinding
import com.example.sinyal.viewmodel.MainViewModel
import com.example.viewmodel.DataStoreViewModel
import com.example.viewmodel.ViewModelFactory
import com.google.android.gms.maps.model.LatLng
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEventBinding
    private lateinit var token: String

    // Properti untuk menyimpan file yang akan diunggah
    private var getFile: File? = null

    // Properti untuk menyimpan konteks berkas
    private lateinit var files: File

    private var latlng: LatLng? = null

    private val addStoryViewModel: MainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.post_users)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ifClicked()

        val preferences = UserPreferences.getInstance(dataStore)
        val dataStoreViewModel =
            ViewModelProvider(this, ViewModelFactory(preferences))[DataStoreViewModel::class.java]

        dataStoreViewModel.getToken().observe(this) {
            token = it
        }


        addStoryViewModel.message.observe(this) {
            showToast(it)
        }

        addStoryViewModel.isLoading.observe(this) {
            showLoading(it)
        }


    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    val address = data.getStringExtra("address")
                    val lat = data.getDoubleExtra("lat", 0.0)
                    val lng = data.getDoubleExtra("lng", 0.0)
                    latlng = LatLng(lat, lng)

                    binding.detailLocation.text = address
                }
            }
        }


    private fun ifClicked() {
        binding.btnPostStory.setOnClickListener {
            if (getFile == null) {
                showToast(resources.getString(R.string.warningAddImage))
                return@setOnClickListener
            }

            val name = binding.tvName.text.toString().trim()
            val des = binding.tvDesc.text.toString().trim()
            val date = binding.tvDate.text.toString().trim()

            val start_time = binding.sTime.text.toString().trim()
            val end_time = binding.eTime.text.toString().trim()
            val maxpar = binding.tvMaxpar.text.toString().trim()

            if ( name.isEmpty() && des.isEmpty()  && maxpar.isEmpty()) {
                binding.tvDesc.error = resources.getString(R.string.warningAddDescription)
                return@setOnClickListener
            }



           lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val file = getFile as File

                    var compressedFile: File? = null
                    var compressedFileSize = file.length()

                    // Compress the file until its size is less than or equal to 1MB
                    while (compressedFileSize > 1 * 1024 * 1024) {
                        compressedFile = withContext(Dispatchers.Default) {
                            Compressor.compress(applicationContext, file)
                        }
                        compressedFileSize = compressedFile.length()
                    }

                    files = compressedFile ?: file

                }

                // use the upload file to upload to server
                val requestImageFile =
                    files.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    files.name,
                    requestImageFile
                )

               val namePart = name.toRequestBody("text/plain".toMediaType())
               val desPart = des.toRequestBody("text/plain".toMediaType())
               val datePart = date.toRequestBody("text/plain".toMediaType())
               val startTimePart = start_time.toInt()
               val endTimePart = end_time.toInt()
               val maxParticipantPart = maxpar.toInt()

               Log.e("Apaan dah", token)
               addStoryViewModel.upload(
                   imageMultipart,
                   namePart,
                   desPart,
                   datePart,
                   startTimePart,
                   endTimePart,
                   maxParticipantPart,
                   latlng?.latitude,
                   latlng?.longitude,
                   token
               )
            }
        }

        binding.cameraButton.setOnClickListener {
            takePhoto()
        }

        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.llLocation.setOnClickListener {
            val intent = Intent(this, PickLocationActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    private fun showTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                // Format waktu sesuai kebutuhan Anda
                val selectedTime = String.format("%02d:%02d", hourOfDay, minute)

                // Validasi bahwa nilai tidak boleh kosong
                if (selectedTime.isNotBlank()) {
                    // Set nilai pada EditText
                    editText.setText(selectedTime)
                }
            },
            currentHour,
            currentMinute,
            true // true untuk format waktu 24 jam, false untuk format 12 jam dengan AM/PM
        )

        // Tampilkan TimePickerDialog
        timePickerDialog.show()
    }


    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                // Format tanggal sesuai kebutuhan Anda
                val selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(Date(year - 1900, month, day))

                // Set nilai pada EditText
                editText.setText(selectedDate)
            },
            currentYear,
            currentMonth,
            currentDay
        )

        // Tampilkan DatePickerDialog
        datePickerDialog.show()
    }

    private var anyPhoto = false
    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            anyPhoto = true
            binding.imageUpload.setImageBitmap(result)
            binding.tvDesc.requestFocus()
        }
    }

    private val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    private fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    // Mengambil foto melalui kamera
    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddEventActivity,
                getString(R.string.package_name),
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    // Mengubah URI menjadi berkas
    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver

        return try {
            val myFile = createCustomTempFile(context)

            contentResolver.openInputStream(selectedImg)?.use { inputStream ->
                FileOutputStream(myFile).use { outputStream ->
                    val buf = ByteArray(1024)
                    var len: Int
                    while (inputStream.read(buf).also { len = it } > 0) {
                        outputStream.write(buf, 0, len)
                    }
                }
            }

            myFile
        } catch (e: Exception) {
            // Menangani kesalahan konversi
            e.printStackTrace()
            throw e
        }
    }

    // Membuat launcher untuk intent galeri
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddEventActivity)
            getFile = myFile
            binding.imageUpload.setImageURI(selectedImg)
            binding.tvDesc.requestFocus()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        finish()
        return super.onSupportNavigateUp()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarAddStory.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    // Memulai aktivitas galeri
    private fun startGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"

        val chooserIntent = Intent.createChooser(intent, getString(R.string.choose_picture))

        // Pastikan ada aktivitas yang dapat menangani intent
        if (intent.resolveActivity(packageManager) != null) {
            launcherIntentGallery.launch(chooserIntent)
        } else {
            // Menangani jika tidak ada aplikasi galeri yang tersedia
            Toast.makeText(this, "Tidak ada aplikasi galeri yang tersedia", Toast.LENGTH_SHORT).show()
        }
    }

    // Menampilkan pesan Toast
    private fun showToast(msg: String) {
        Toast.makeText(
            this@AddEventActivity,
            StringBuilder(getString(R.string.message)).append(msg),
            Toast.LENGTH_SHORT
        ).show()

        if (msg == "Story created successfully") {
            startHomePageActivity()
        }
    }

    // Memulai aktivitas Beranda
    private fun startHomePageActivity() {
        val intent = Intent(this@AddEventActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    companion object {
        const val FILENAME_FORMAT = "MMddyyyy"
    }

}