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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsprojectpractice.R
import com.example.newsprojectpractice.databinding.FragmentHeadlinesBinding
import ma.ensa.projet.adapters.NewsAdapter
import ma.ensa.projet.ui.NewsActivity
import ma.ensa.projet.ui.NewsViewModel
import ma.ensa.projet.util.Constants
import ma.ensa.projet.util.Resource


class HeadlinesFragment : Fragment(R.layout.fragment_headlines) {

    lateinit var newsViewModel : NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var retryButton: Button
    lateinit var errorText : TextView
    lateinit var itemHeadlinesError : CardView
    lateinit var binding : FragmentHeadlinesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHeadlinesBinding.bind(view)

        itemHeadlinesError = view.findViewById(R.id.itemHeadlinesError)

        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view : View = inflater.inflate(R.layout.item_error , null)

        retryButton = view.findViewById(R.id.retryButton)
        errorText = view.findViewById(R.id.errorText)

        newsViewModel = (activity as NewsActivity).newsViewModel
        setupHeadlinesRecycler()

        newsAdapter.setOnclickListenner {
            val bundle = Bundle().apply {
                putSerializable("article" , it)
            }

            findNavController().navigate(R.id.action_headlinesFragment_to_articlesFragment,bundle)

        }

        newsViewModel.headlines.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success<*> -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / Constants.QUERY_PAGE_SIZE
                        isLastPage = newsViewModel.headlinesPages == totalPages

                        if (isLastPage) {
                            binding.recyclerHeadlines.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error<*> -> {
                    hideProgressBar()
                    response.message?.let {message ->
                        Toast.makeText(activity , "Sorry error : $message" ,Toast.LENGTH_LONG ).show()
                        showErrorMessage(message)
                    }

                }

                is Resource.Loading<*> -> {
                    showProgressBar()

                }
            }
        }
        retryButton.setOnClickListener{
            newsViewModel.getHeadlines("us")
        }

    }

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isSrolling = false

    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage(){
        itemHeadlinesError.visibility = View.INVISIBLE
        isError= false
    }

    private fun showErrorMessage(message : String){
        itemHeadlinesError.visibility = View.VISIBLE
        errorText.text = message
        isError= true
    }

    val scrollListener = object  : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginnig = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isTotalMoreThanVisible && isNotAtBeginnig && isSrolling

            if (shouldPaginate) {
                newsViewModel.getHeadlines("us")
                isSrolling = false
            }


        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isSrolling = true
            }
        }
    }

        private fun setupHeadlinesRecycler(){
            newsAdapter =  NewsAdapter()
            binding.recyclerHeadlines.apply {
                adapter = newsAdapter
                layoutManager = LinearLayoutManager(activity)
                addOnScrollListener(this@HeadlinesFragment.scrollListener)
            }
        }
    }



