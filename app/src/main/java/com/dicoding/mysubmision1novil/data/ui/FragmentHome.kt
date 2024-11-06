package com.dicoding.mysubmision1novil.data.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mysubmision1novil.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var progressbar2 : ProgressBar
    private val eventViewModel: EventViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterUpcoming: EventAdapter1
    private lateinit var adapterFinished: EventAdapter2

    private var isSoonEventLoaded = false
    private var isPastEventLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.d("HomeFragment", "Oncrate : dipanggil")

        (activity as? AppCompatActivity)?.supportActionBar?.show()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView1 = binding.event1
        recyclerView2 = binding.event2
        progressBar = binding.progressBar
        progressbar2 = binding.progresBar2

        adapterUpcoming = EventAdapter1()
        recyclerView1.adapter = adapterUpcoming
        recyclerView1.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        adapterFinished = EventAdapter2()
        recyclerView2.adapter = adapterFinished
        recyclerView2.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        eventViewModel.soonEvent.observe(viewLifecycleOwner) { event ->
            Log.d("HomeFragment", "Jumlah item di soonevent :${event.size}")
            isSoonEventLoaded = true
            val limit = event.take(5)
            if (limit.isEmpty()){
                Log.d("homeFragment", "Data Soon Kosong")
            }
            adapterUpcoming.submitList(limit)
            chechDataLoaded()
        }

        eventViewModel.pastEvent.observe(viewLifecycleOwner) { event ->

            val limit = event.take(5)
            isPastEventLoaded = true
            if (limit.isEmpty()){
                Log.d("Home Fragment", "Data past Kosong")
            }
            adapterFinished.submitList(limit)
            chechDataLoaded()
        }


        progressBar.visibility = View.VISIBLE
        progressbar2.visibility=View.VISIBLE
        eventViewModel.loadSoonEvent()
        eventViewModel.loadPastEvent()

        return root
    }

    private fun chechDataLoaded(){
        if (isPastEventLoaded && isSoonEventLoaded) {
            progressBar.visibility= View.GONE
            progressbar2.visibility=View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
