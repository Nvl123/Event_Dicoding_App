package com.dicoding.mysubmision1novil.data.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mysubmision1novil.databinding.FragmentPastEventBinding


class PastEventFragment : Fragment() {

    private lateinit var adapter : EventAdapter1
    private val viewModel :EventViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var _binding : FragmentPastEventBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPastEventBinding.inflate(inflater, container, false)
        val  root : View = binding.root

        recyclerView = binding.rvPast
        progressBar = binding.progressPast

        with(binding){
            searchViewPast.setupWithSearchBar(searchBarPast)
            searchViewPast
                .editText
                .setOnEditorActionListener{_, _, _ ->
                    val q = searchViewPast.text.toString()
                    searchBarPast.setText(q)
                    searchViewPast.hide()

                    viewModel.searchPastEvent(q)
                    false
                }
        }

        adapter= EventAdapter1()
        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        viewModel.pastEvent.observe(viewLifecycleOwner){event ->

            progressBar.visibility=View.GONE
            adapter.submitList(event)
        }

        viewModel.event.observe(viewLifecycleOwner){event ->
            progressBar.visibility=View.GONE
            adapter.submitList(event)
        }

        viewModel.Message.observe(viewLifecycleOwner){ message ->
            message?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }
        progressBar.visibility=View.VISIBLE
        viewModel.loadPastEvent()

        return root
    }

    override fun onResume() {
        super.onResume()
        viewModel.resetMessage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }


}