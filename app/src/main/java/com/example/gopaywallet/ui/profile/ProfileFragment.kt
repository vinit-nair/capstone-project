package com.example.gopaywallet.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gopaywallet.data.SessionManager
import com.example.gopaywallet.data.model.User
import com.example.gopaywallet.databinding.FragmentProfileManagementBinding
import com.example.gopaywallet.utils.showToast
import java.io.InputStream
import java.io.OutputStream
import android.net.Uri
import com.example.gopaywallet.data.model.ResetPasswordRequest
import com.example.gopaywallet.ui.auth.ForgotPasswordActivity
import com.example.gopaywallet.ui.auth.ResetPasswordActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileManagementBinding
    private val viewModel: ProfileViewModel by viewModels()  // Hilt automatically provides the ViewModel

    private var isEditMode: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUIForEditMode()
        loadProfilePicture()
        setupObservers()
        viewModel.loadUserDetails()

        binding.btnChangeProfilePicture.setOnClickListener {
            openImagePicker()
        }

//        binding.btnChangePassword.setOnClickListener {
//            // Navigate to ChangePasswordActivity
//            val intent = Intent(requireContext(), ForgotPasswordActivity::class.java)
//            startActivity(intent)
//        }

        // Handle button click for editing/saving
        binding.btnEditOrSave.setOnClickListener {
            if (isEditMode) {
                // Save details
                val updatedUser = User(
                    id = viewModel.user.value?.id ?: 0L,
                    fullName = binding.etFullName.text.toString(),
                    email = binding.etEmail.text.toString(),
                    phoneNumber = binding.etPhoneNumber.text.toString()
                )
                viewModel.saveUserDetails(updatedUser)
            }
            // Toggle edit mode
            isEditMode = !isEditMode
            updateUIForEditMode()
        }


    }

    private fun updateUIForEditMode() {
        if (isEditMode) {
            // Enable editing
            binding.etFullName.isEnabled = true
            binding.etEmail.isEnabled = true
            binding.etPhoneNumber.isEnabled = true
            binding.btnEditOrSave.text = "Save Details"
        } else {
            // Disable editing
            binding.etFullName.isEnabled = false
            binding.etEmail.isEnabled = false
            binding.etPhoneNumber.isEnabled = false
            binding.btnEditOrSave.text = "Edit Details"
        }
    }

    private fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.etFullName.setText(user?.fullName ?: "")
            binding.etEmail.setText(user?.email ?: "")
            binding.etPhoneNumber.setText(user?.phoneNumber ?: "")
        }

        viewModel.success.observe(viewLifecycleOwner) { successMessage ->
            successMessage?.let {
                requireContext().showToast(it)
                viewModel.clearSuccess()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                requireContext().showToast(it)
                viewModel.clearError()
            }
        }
    }

    private val IMAGE_REQUEST_CODE = 1001

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                saveProfilePicture(uri)
            }
        }
    }

    private fun saveProfilePicture(uri: Uri) {
        val fileName = "profile_picture.jpg"
        val outputStream: OutputStream? = requireContext().openFileOutput(fileName, Context.MODE_PRIVATE)

        try {
            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
            inputStream?.use { input ->
                outputStream?.use { output ->
                    input.copyTo(output)
                }
            }

            // Notify user and update UI
            requireContext().showToast("Profile picture saved successfully")
            binding.ivProfilePicture.setImageURI(uri)

            // Save file path for future use
            val filePath = requireContext().filesDir.absolutePath + "/" + fileName
            saveProfilePicturePath(filePath)
        } catch (e: Exception) {
            requireContext().showToast("Failed to save profile picture: ${e.message}")
        } finally {
            outputStream?.close()
        }
    }
    private fun saveProfilePicturePath(path: String) {
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("profile_picture_path", path).apply()
    }
    private fun loadProfilePicture() {
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val filePath = sharedPreferences.getString("profile_picture_path", null)
        if (filePath != null) {
            val fileUri = Uri.parse(filePath)
            binding.ivProfilePicture.setImageURI(fileUri)
        }
    }
}
