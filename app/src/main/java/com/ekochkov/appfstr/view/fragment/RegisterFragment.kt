package com.ekochkov.appfstr.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ekochkov.appfstr.data.entity.User
import com.ekochkov.appfstr.databinding.FragmentRegisterBinding
import com.ekochkov.appfstr.view.activity.MainActivity
import com.ekochkov.appfstr.viewModel.RegisterFragmentViewModel

class RegisterFragment: Fragment() {

    private val NAME_INCORRECT = "имя введено неверно"
    private val SECOND_NAME_INCORRECT = "фамилия введена неверно"
    private val FATHER_NAME_INCORRECT = "отчество введено неверно"
    private val EMAIL_INCORRECT = "почта введена неверно"
    private val PHONE_INCORRECT = "телефон введен неверно"

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel : RegisterFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userId.observe(viewLifecycleOwner) {
            if (it!=null) {
                (activity as MainActivity).openHomeFragment()
            }
        }

        binding.loginBtn.setOnClickListener {
            if (isInputDataCorrect()) {
                val user = User(
                    name = binding.nameEditText.editText?.text.toString(),
                    secondName = binding.secondNameEditText.editText?.text.toString(),
                    fatherName = binding.fatherNameEditText.editText?.text.toString(),
                    email = binding.emailEditText.editText?.text.toString(),
                    phone = binding.phoneEditText.editText?.text.toString()
                )
                viewModel.registerUser(user)
            }
        }
    }

    private fun isInputDataCorrect(): Boolean {
        if (!isNameCorrect(binding.nameEditText.editText?.text.toString())) {
            showToast(NAME_INCORRECT)
            return false
        } else if (!isNameCorrect(binding.secondNameEditText.editText?.text.toString())){
            showToast(SECOND_NAME_INCORRECT)
            return false
        } else if (!isNameCorrect(binding.fatherNameEditText.editText?.text.toString())) {
            showToast(FATHER_NAME_INCORRECT)
            return false
        } else if (!isEmailCorrect(binding.emailEditText.editText?.text.toString())) {
            showToast(EMAIL_INCORRECT)
            return false
        } else if (!isPhoneCorrect()) {
            showToast(PHONE_INCORRECT)
            return false
        }
        return true
    }

    private fun isNameCorrect(name: String): Boolean {
        return name.isNotEmpty()
    }

    private fun isEmailCorrect(name: String): Boolean {
        return name.isNotEmpty()
    }

    private fun isPhoneCorrect(): Boolean {
        return true
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}