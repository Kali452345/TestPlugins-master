package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*
import org.jsoup.nodes.Element

class TheNkiriProvider : MainAPI() {
    override var mainUrl = "https://thenkiri.com"
    override var name = "TheNkiri"
    override val hasMainPage = true
    override val hasDownloadSupport = true
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.TvSeries,
        TvType.AsianDrama
    )

    override val mainPage = mainPageOf(
        "$mainUrl/" to "New Dramas",
        "$mainUrl/" to "New Movies", 
        "$mainUrl/" to "New Series",
        "$mainUrl/korean-drama-menu/" to "Korean Dramas",
        "$mainUrl/movies-menu/" to "Movies",
        "$mainUrl/tv-series-menu/" to "TV Series",
        "$mainUrl/japanese-drama-menu/" to "Japanese Drama",
        "$mainUrl/chinese-drama-menu/" to "Chinese Drama",
        "$mainUrl/bollywood-menu/" to "Bollywood"
    )

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val document = app.get(request.data + if (page > 1) "page/$page/" else "").document
        
        // Handle homepage sections differently
        val home = if (request.data == mainUrl || request.data == "$mainUrl/") {
            // Homepage - extract specific sections
            when (request.name) {
                "New Dramas" -> {
                    document.select("h3:contains(New Dramas Uploads), h3:contains(New Drama Uploads)")
                        .firstOrNull()?.parent()?.select("article") ?: listOf()
                }
                "New Movies" -> {
                    document.select("h3:contains(New Movie Uploads)")
                        .firstOrNull()?.parent()?.select("article") ?: listOf()
                }
                "New Series" -> {
                    document.select("h3:contains(New Series Uploads)")
                        .firstOrNull()?.parent()?.select("article") ?: listOf()
                }
                else -> document.select("article")
            }
        } else {
            // Category pages
            document.select("article")
        }
        
        val searchResults = home.mapNotNull { it.toSearchResponse() }
        return newHomePageResponse(request.name, searchResults, hasNext = searchResults.isNotEmpty())
    }

    private fun Element.toSearchResponse(): SearchResponse? {
        val title = this.selectFirst("h2.entry-title a")?.text()?.trim() ?: return null
        val href = fixUrlNull(this.selectFirst("h2.entry-title a")?.attr("href")) ?: return null
        val posterUrl = fixUrlNull(
            this.selectFirst("img")?.attr("src") 
            ?: this.selectFirst("img")?.attr("data-src")
        )
        
        val type = when {
            href.contains("korean-drama") -> TvType.AsianDrama
            href.contains("japanese-drama") -> TvType.AsianDrama
            href.contains("chinese-drama") -> TvType.AsianDrama
            href.contains("tv-series") -> TvType.TvSeries
            title.contains("S0", ignoreCase = true) -> TvType.TvSeries
            else -> TvType.Movie
        }

        return newMovieSearchResponse(title, href, type) {
            this.posterUrl = posterUrl
        }
    }

    override suspend fun search(query: String): List<SearchResponse> {
        val searchResponse = app.get("$mainUrl/?s=$query").document
        return searchResponse.select("article").mapNotNull {
            it.toSearchResponse()
        }
    }

    override suspend fun load(url: String): LoadResponse {
        val document = app.get(url).document

        val title = document.selectFirst("h1.entry-title")?.text()?.trim()
            ?: throw ErrorLoadingException("No Title Found")

        val posterUrl = fixUrlNull(
            document.selectFirst("article img")?.attr("src")
            ?: document.selectFirst("article img")?.attr("data-src")
        )

        val plot = document.select("div.entry-content > p").firstOrNull()?.text()?.trim()

        val tags = document.select("a[rel=tag]").map { it.text() }

        // Determine if it's a movie or series
        val isMovie = !title.contains(Regex("S\\d+", RegexOption.IGNORE_CASE)) 
            && !url.contains("tv-series") 
            && !url.contains("drama")

        // Extract episodes
        val episodes = mutableListOf<Episode>()
        
        // Look for download links in the page
        val episodeElements = document.select("h2:contains(Episode), h3:contains(Episode), h2:contains(Download)")
        
        if (episodeElements.isNotEmpty()) {
            // It's a series with episodes
            episodeElements.forEach { episodeHeader ->
                val episodeText = episodeHeader.text()
                val episodeNumber = Regex("Episode\\s*(\\d+)", RegexOption.IGNORE_CASE)
                    .find(episodeText)?.groupValues?.get(1)?.toIntOrNull()

                // Find the download link associated with this episode
                val downloadLink = episodeHeader.nextElementSibling()?.selectFirst("a[href]")?.attr("href")
                
                if (downloadLink != null && episodeNumber != null) {
                    episodes.add(
                        newEpisode(downloadLink) {
                            this.name = "Episode $episodeNumber"
                            this.episode = episodeNumber
                            this.season = 1 // Default season, can be enhanced
                        }
                    )
                }
            }
        }

        // Check for season information
        val seasonRegex = Regex("Season\\s*(\\d+)|S(\\d+)", RegexOption.IGNORE_CASE)
        val seasonMatch = seasonRegex.find(title)
        val season = seasonMatch?.groupValues?.firstOrNull { it.toIntOrNull() != null }?.toIntOrNull() ?: 1

        // Update season for all episodes
        episodes.forEach { it.season = season }

        return if (isMovie || episodes.isEmpty()) {
            // Try to find a single download link for movies
            val movieLink = document.select("a:contains(Download)").firstOrNull()?.attr("href")
            
            newMovieLoadResponse(title, url, TvType.Movie, movieLink ?: url) {
                this.posterUrl = posterUrl
                this.plot = plot
                this.tags = tags
            }
        } else {
            newTvSeriesLoadResponse(title, url, TvType.AsianDrama, episodes) {
                this.posterUrl = posterUrl
                this.plot = plot
                this.tags = tags
            }
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        // TODO: Implement video link extraction here
        // This is a placeholder that returns true to allow the extension to compile
        // See DEVELOPMENT_GUIDE.md Section 4 for implementation instructions
        
        // For now, just return true - you'll implement extraction when ready
        return true
    }
}
