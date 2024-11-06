package com.dicoding.mysubmision1novil.data.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mysubmision1novil.databinding.FragmentSoonEventBinding


class SoonEventFragment : Fragment() {
    private lateinit var adapter : EventAdapter2
    private val viewModel : EventViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var _binding :FragmentSoonEventBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.d("SoonFragment", "dimuat")

        _binding = FragmentSoonEventBinding.inflate(inflater, container, false)
        val root : View = binding.root

        recyclerView = binding.rvSoon
        progressBar =binding.progressSoon

        with(binding){
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener{_, _, _ ->
                    val q = searchView.text.toString()
                    searchBar.setText(q)
                    searchView.hide()

                    viewModel.searchSoonEvent(q)
                    false

                }
        }

        adapter = EventAdapter2()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        viewModel.soonEvent.observe(viewLifecycleOwner){ event ->

            progressBar.visibility= View.GONE
            adapter.submitList(event)
        }

        viewModel.event.observe(viewLifecycleOwner){ event ->
            progressBar.visibility=View.GONE
            adapter.submitList(event)
        }

        viewModel.Message.observe(viewLifecycleOwner){ message ->
            message?.let {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }

        progressBar.visibility = View.VISIBLE
        viewModel.loadSoonEvent()


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

