// Use an integer for version numbers
version = 1

cloudstream {
    description = "TheNkiri.com provider for movies, TV series, and Asian dramas (Korean, Japanese, Chinese)"
    authors = listOf("YourName")

    /**
    * Status int as one of the following:
    * 0: Down
    * 1: Ok
    * 2: Slow
    * 3: Beta-only
    **/
    status = 1

    tvTypes = listOf(
        "TvSeries",
        "Movie",
        "AsianDrama",
    )

    language = "en"

    iconUrl = "https://thenkiri.com/wp-content/uploads/2023/09/cropped-nkiri-logo-1-32x32.png"
}