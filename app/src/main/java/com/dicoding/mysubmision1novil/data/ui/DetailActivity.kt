package com.dicoding.mysubmision1novil.data.ui

import DetailViewModel
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dicoding.mysubmision1novil.R
import com.dicoding.mysubmision1novil.data.database.EventFavorit
import com.dicoding.mysubmision1novil.data.response.ListEventsItem
import com.dicoding.mysubmision1novil.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val eventId = intent.getStringExtra("IdEvent") ?: ""
        if (eventId.isNotEmpty()) {
            detailViewModel.detailData(eventId)
            observeFavoriteStatus(eventId)
        } else {
            Log.e("DetailActivity", "Event ID is missing")
            finish()
        }

        setupObservers()
        setupFavoriteButton()
        setupRegisterButton()
        handleInsets()
    }

    private fun observeFavoriteStatus(eventId: String) {
        detailViewModel.getfavoritId(eventId).observe(this) { favorit ->
            isFavorite = favorit != null
            binding.FabFavorite.setImageResource(
                if (isFavorite) R.drawable.favorite else R.drawable.favorite_border
            )
        }
    }

    private fun setupObservers() {
        detailViewModel.isLoading.observe(this) { loading ->
            binding.progressDetail.visibility = if (loading) View.VISIBLE else View.GONE
        }

        detailViewModel.event.observe(this) { event ->
            Log.d("DetailActivity", "Event data received: $event")
            bindDetailEvent(event)
        }
    }

    private fun setupFavoriteButton() {
        binding.FabFavorite.setOnClickListener {
            detailViewModel.event.value?.let { event ->
                val favoriteEvent = EventFavorit(
                    id = event.id.toString(),
                    name = event.name,
                    summary = event.summary,
                    mediaCover = event.mediaCover
                )
                if (isFavorite) {
                    detailViewModel.delete(favoriteEvent)
                    Toast.makeText(this, "Unfavorite", Toast.LENGTH_SHORT).show()
                } else {
                    detailViewModel.insert(favoriteEvent)
                    Toast.makeText(this, "Favorite!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupRegisterButton() {
        binding.btnDaftar.setOnClickListener {
            detailViewModel.event.value?.link?.let { link ->
                val browseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(browseIntent)
            }
        }
    }

    private fun handleInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun bindDetailEvent(event: ListEventsItem?) {
        try {
            event?.let {
                val context = binding.root.context
                with(binding) {
                    titleEvent.text = event.name
                    startTime.text = event.beginTime
                    endTime.text = event.endTime
                    jmlKuota.text = (event.quota - event.registrants).toString()
                    penyelenggara.text = event.ownerName
                    descriptionEvent.text = event.description.let { html ->
                        Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT, GlideImageGetter(descriptionEvent), null)
                    } ?: context.getString(R.string.default_event_description)
                    descriptionEvent.movementMethod = LinkMovementMethod.getInstance()
                }
                Glide.with(this@DetailActivity)
                    .load(event.mediaCover)
                    .error(R.drawable.error)
                    .into(binding.imgCover)
            }
        } catch (e: Exception) {
            Log.e("DetailEvent", "Error loading event: ${e.message}", e)
            showErrorPlaceholder()
        }
    }

    private fun showErrorPlaceholder() {
        val context = binding.root.context
        with(binding) {
            titleEvent.text = context.getString(R.string.error_event_name)
            startTime.text = context.getString(R.string.deafult_event_time)
            endTime.text = context.getString(R.string.deafult_event_time)
            jmlKuota.text = context.getString(R.string.default_event_quota)
            penyelenggara.text = context.getString(R.string.default_owner_name)
            descriptionEvent.text = context.getString(R.string.default_event_description)
            imgCover.setImageResource(R.drawable.error)
        }
    }

    class GlideImageGetter(private val textView: TextView) : Html.ImageGetter {
        override fun getDrawable(source: String?): Drawable {
            val placeholder = ColorDrawable(Color.TRANSPARENT).apply {
                setBounds(0, 0, 1, 1)
            }
            source?.let {
                Glide.with(textView.context)
                    .load(it)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            resource.setBounds(0, 0, resource.intrinsicWidth, resource.intrinsicHeight)
                            textView.text = textView.text
                            textView.invalidate()
                        }
                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
            }
            return placeholder
        }
    }
}
