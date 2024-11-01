package ma.ensa.projet.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsprojectpractice.R
import com.example.newsprojectpractice.databinding.FragmentSearchBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ma.ensa.projet.adapters.NewsAdapter
import ma.ensa.projet.ui.NewsActivity
import ma.ensa.projet.ui.NewsViewModel
import ma.ensa.projet.util.Constants
import ma.ensa.projet.util.Constants.Companion.SEARCH_NEWS_TIME_DALAY
import ma.ensa.projet.util.Resource

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var retryButton: Button
    private lateinit var errorText: TextView
    private lateinit var itemSearchError: CardView
    private lateinit var binding: FragmentSearchBinding

    private var isError = false
    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        itemSearchError = view.findViewById(R.id.itemSearchError)
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // Inflate error view and bind buttons and texts.
        val errorView: View = inflater.inflate(R.layout.item_error, null)
        retryButton = errorView.findViewById(R.id.retryButton)
        errorText = errorView.findViewById(R.id.errorText)

        newsViewModel = (activity as NewsActivity).newsViewModel
        setupSearchRecycler()

        newsAdapter.setOnclickListenner { article ->
            val bundle = Bundle().apply {
                putSerializable("article", article)
            }
            findNavController().navigate(R.id.action_searchFragment_to_articlesFragment, bundle)
        }

        var job: Job? = null

        // Listen for text changes in the search EditText.
        binding.searchEdit.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DALAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        // Clear previous results before searching.
                        newsAdapter.differ.submitList(emptyList())
                        newsViewModel.searchNews(editable.toString())
                    } else {
                        // Clear results if search field is empty.
                        newsAdapter.differ.submitList(emptyList())
                    }
                }
            }
        }

        // Observe search results from the ViewModel.
        newsViewModel.searchNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success<*> -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                        // Submit new list to adapter.
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE
                        isLastPage = newsViewModel.searchNewsPage == totalPages

                        if (isLastPage) {
                            binding.recyclerSearch.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error<*> -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "Sorry error: $message", Toast.LENGTH_LONG).show()
                        showErrorMessage(message)
                    }
                }

                is Resource.Loading<*> -> {
                    showProgressBar()
                }
            }
        }

        retryButton.setOnClickListener {
            if (binding.searchEdit.text.toString().isNotEmpty()) {
                newsViewModel.searchNews(binding.searchEdit.text.toString())
            } else {
                hideErrorMessage()
            }
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage() {
        itemSearchError.visibility = View.INVISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String) {
        itemSearchError.visibility = View.VISIBLE
        errorText.text = message
        isError = true
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage

            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount

            val isNotAtBeginning = firstVisibleItemPosition >= 0

            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE

            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem &&
                        isTotalMoreThanVisible && isNotAtBeginning && isScrolling

            if (shouldPaginate) {
                newsViewModel.searchNews(binding.searchEdit.text.toString())
                isScrolling = false // Reset scrolling state after pagination.
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true // Set scrolling state when user scrolls.
            }
        }
    }

    private fun setupSearchRecycler() {
        newsAdapter = NewsAdapter()

        binding.recyclerSearch.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(scrollListener)
        }
    }
}