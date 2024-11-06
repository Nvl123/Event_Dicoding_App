package com.dicoding.mysubmision1novil.data.ui

import ViewModelFavorit
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.mysubmision1novil.data.response.ListEventsItem
import com.dicoding.mysubmision1novil.databinding.FragmentFavoritBinding

class FavoritFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentFavoritBinding
    private lateinit var adapter1: EventAdapter1
    private lateinit var viewModelFavorit: ViewModelFavorit
    private lateinit var progressbar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.rvFavorite
        adapter1 = EventAdapter1()
        recyclerView.adapter = adapter1
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        // Inisialisasi ViewModel
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        viewModelFavorit = ViewModelProvider(this, factory)[ViewModelFavorit::class.java]

        // Mengatur pencarian
        with(binding) {
            searchViewFavorit.setupWithSearchBar(searchBarFavorit)
            searchViewFavorit.editText.setOnEditorActionListener { _, _, _ ->
                val q = searchViewFavorit.text.toString()
                searchBarFavorit.setText(q)
                searchViewFavorit.hide()

                // Panggil fungsi pencarian di ViewModel untuk mencari event favorit berdasarkan query `q`
                viewModelFavorit.searchFavoriteEvent(q)
                true
            }
        }

        progressbar = binding.progressFavorit


        // Mengamati hasil pencarian dari ViewModel
        viewModelFavorit.event.observe(viewLifecycleOwner) { eventList ->
            // Konversi dari EventFavorit ke ListEventsItem
            val items = eventList.map { favItem ->
                ListEventsItem(
                    id = favItem.id.toInt(),
                    name = favItem.name,
                    summary = favItem.summary,
                    mediaCover = favItem.mediaCover ?: "Media Default"
                )
            }

            adapter1.submitList(items)
        }

        // Mengamati pesan kesalahan dari ViewModel
        viewModelFavorit.message.observe(viewLifecycleOwner) { message ->
            // Tampilkan pesan jika tidak ada item yang ditemukan
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        viewModelFavorit.isLoading.observe(viewLifecycleOwner) {loading ->
            progressbar.visibility = if (loading) View.VISIBLE else View.GONE
        }


        // Menampilkan semua event favorit saat pertama kali dimuat
        viewModelFavorit.getEventFavorit()

        return root
    }
}

