package ma.ensa.projet.repository

import ma.ensa.projet.models.Article
import ma.ensa.projet.api.RetrofitInstance
import ma.ensa.projet.db.ArticleDatabase

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getHeadLines(countryCode : String , pageNumber :Int) =
        RetrofitInstance.api.getHeadlines(countryCode , pageNumber)


    suspend fun searchNews(serchQuery: String , pageNumber: Int)=
        RetrofitInstance.api.searchForNews(serchQuery ,pageNumber)


    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getFavoutitesNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)


}