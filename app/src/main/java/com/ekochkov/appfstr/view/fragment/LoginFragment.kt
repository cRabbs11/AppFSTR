package com.ekochkov.appfstr.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ekochkov.appfstr.data.entity.User
import com.ekochkov.appfstr.databinding.FragmentLoginBinding
import com.ekochkov.appfstr.view.activity.MainActivity
import com.ekochkov.appfstr.viewModel.LoginFragmentViewModel

class LoginFragment: Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginFragmentViewModel by viewModels()
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userLiveData.observe(viewLifecycleOwner) {
            user = it
        }

        binding.loginBtn.setOnClickListener {
            if (user!=null) {
                (activity as MainActivity).openHomeFragment()
            } else {
                (activity as MainActivity).openRegisterFragment()
            }
        }
    }
}