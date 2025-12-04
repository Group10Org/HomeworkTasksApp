package com.example.hwtasks

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hwtasks.ShortItem
import com.example.hwtasks.ShortsAdapter
import com.example.hwtasks.StreamViewModel
import com.example.hwtasks.StreamViewModelFactory
import kotlinx.coroutines.flow.collectLatest

class StreamFragment : Fragment() {

    private val apiKey = "AIzaSyBQhLjIQGU3KnwQos4SUUj4x2vAzZyKQ00" // Replace with your YouTube Data API key

    // ✅ Correct ViewModel instantiation with custom factory
    private val viewModel: StreamViewModel by viewModels {
        StreamViewModelFactory(apiKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_stream, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.shortsRecycler)
        val adapter = ShortsAdapter(mutableListOf()) { item ->
            openShort(item)
        }

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        // Infinite scroll listener
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lm = rv.layoutManager as LinearLayoutManager
                val lastVisible = lm.findLastVisibleItemPosition()

                if (lastVisible >= adapter.itemCount - 4) {
                    viewModel.loadMore()
                }
            }
        })

        // Observe ViewModel Shorts data
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.shorts.collectLatest { data ->
                // Only add new items
                adapter.addMore(data.takeLast(20))
            }
        }

        // Load the first page
        viewModel.loadMore()
    }

    // Opens YouTube Shorts in YouTube app or browser
    private fun openShort(item: ShortItem) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/shorts/${item.videoId}"))
        startActivity(intent)
    }
}

