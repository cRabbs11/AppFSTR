package com.ekochkov.appfstr.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ekochkov.appfstr.databinding.FragmentHomeBinding
import com.ekochkov.appfstr.view.activity.MainActivity
import com.ekochkov.appfstr.viewModel.HomeFragmentViewModel

class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //var some = viewModel.some
        binding.addBtn.setOnClickListener {
            (activity as MainActivity).openAddMountainFragment()
        }
    }
}