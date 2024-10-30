package ma.ensa.projet.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsprojectpractice.R
import com.example.newsprojectpractice.databinding.FragmentArticlesBinding
import com.google.android.material.snackbar.Snackbar
import ma.ensa.projet.ui.NewsActivity
import ma.ensa.projet.ui.NewsViewModel


class ArticlesFragment : Fragment( R.layout.fragment_articles) {

    lateinit var  newsViewModel: NewsViewModel
    val args: ArticlesFragmentArgs by navArgs()
    lateinit var  binding: FragmentArticlesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticlesBinding.bind(view)

        newsViewModel = (activity as NewsActivity).newsViewModel

        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let {
                loadUrl(it)
            }
        }

        binding.fab.setOnClickListener{
            newsViewModel.addToFouvrites(article)
            Snackbar.make(view , "Added to favourites", Snackbar.LENGTH_LONG).show()

        }

    }
}