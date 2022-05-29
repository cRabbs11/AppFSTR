package com.ekochkov.appfstr.view.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekochkov.appfstr.R
import com.ekochkov.appfstr.data.entity.Mountain
import com.ekochkov.appfstr.databinding.FragmentHomeBinding
import com.ekochkov.appfstr.view.activity.MainActivity
import com.ekochkov.appfstr.view.adapters.MountainsAdapter
import com.ekochkov.appfstr.view.viewHolders.MountainHolder
import com.ekochkov.appfstr.viewModel.HomeFragmentViewModel
import com.google.android.material.divider.MaterialDividerItemDecoration

class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: MountainsAdapter

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

        adapter = MountainsAdapter(object: MountainHolder.OnMountainClickListener {
            override fun onCheckBoxClick(mountain: Mountain, isChecked: Boolean) {
                if (isChecked) {
                    viewModel.addToSelectedMountains(mountain)
                } else {
                    viewModel.removeFromSelectedMountains(mountain)
                }
            }

            override fun onPhotoBtnClick(mountain: Mountain) {
                println("onPhoto click")
            }

            override fun onEditBtnClick(mountain: Mountain) {
                (activity as MainActivity).openAddMountainFragment(mountain.cashedID)
            }

        })
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)

        viewModel.userLiveData.observe(viewLifecycleOwner) {
            if (it==null) {
                (activity as MainActivity).supportFragmentManager.popBackStack()
            } else {
                val actionBarText = "${it.secondName} ${it.name}"
                (activity as MainActivity).supportActionBar?.title = actionBarText

            }
        }

        viewModel.toastEventLiveData.observe(viewLifecycleOwner) {
            showToast(it)
        }

        viewModel.selectedMountainsLiveData.observe(viewLifecycleOwner) {
            println("!!! selected: ${it.size}")
            if (it.isNotEmpty()) {
                binding.selectedActionsLayout.visibility = View.VISIBLE
                val btnText = "${getString(R.string.send)} (${it.size})"
                binding.sendBtn.text = btnText
            } else {
                binding.mountainRecyclerView.setAdapter(null)
                binding.mountainRecyclerView.setLayoutManager(null)
                binding.mountainRecyclerView.setAdapter(adapter)
                binding.mountainRecyclerView.setLayoutManager(LinearLayoutManager(requireContext()))
                adapter.notifyDataSetChanged()

                binding.selectedActionsLayout.visibility = View.GONE
                binding.sendBtn.text = getString(R.string.send)
            }
        }

        binding.declineBtn.setOnClickListener {
            viewModel.clearSelectedMountains()
        }

        binding.sendBtn.setOnClickListener {
            viewModel.sendSelectedMountainsOnServer()
        }

        binding.mountainRecyclerView.addItemDecoration(divider)

        binding.mountainRecyclerView.adapter = adapter

        viewModel.mountainsLiveData.observe(viewLifecycleOwner) {

            if (it.isEmpty()) {
                binding.emptyListImage.visibility = View.VISIBLE
                binding.emptyListText.visibility = View.VISIBLE
            } else {
                binding.emptyListImage.visibility = View.GONE
                binding.emptyListText.visibility = View.GONE
            }

            adapter.mountainList.clear()
            adapter.mountainList.addAll(it)
            adapter.notifyDataSetChanged()
        }

        binding.addBtn.setOnClickListener {
            (activity as MainActivity).openAddMountainFragment()
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.log_out -> {
                println("!!! logout")
                viewModel.deleteUser()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}