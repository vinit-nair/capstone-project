//package com.example.gopaywallet.ui.profile
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.gopaywallet.data.SessionManager
//import com.example.gopaywallet.data.repository.UserRepositoryImpl
//import com.example.gopaywallet.di.NetworkModule
//import com.example.gopaywallet.ui.profile.ProfileViewModel
//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject
//
//@HiltViewModel
//class ProfileViewModelFactory @Inject constructor(
//    private val userRepository: UserRepositoryImpl
//) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return ProfileViewModel(
//                UserRepositoryImpl(userRepository.authApi, userRepository.sessionManager)
//            ) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
