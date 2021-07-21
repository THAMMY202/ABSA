package com.test.absa.model
import io.reactivex.Observable
import retrofit2.http.GET
import com.google.gson.annotations.SerializedName
import retrofit2.http.Path
import retrofit2.http.Query

//Creating Data Classes For country list
data class Country(
        @SerializedName("alpha2Code")
        val alpha2Code: String,
        @SerializedName("alpha3Code")
        val alpha3Code: String,
        @SerializedName("altSpellings")
        val altSpellings: List<String>,
        @SerializedName("area")
        val area: Double,
        @SerializedName("borders")
        val borders: List<String>,
        @SerializedName("callingCodes")
        val callingCodes: List<String>,
        @SerializedName("capital")
        val capital: String,
        @SerializedName("cioc")
        val cioc: String,
        @SerializedName("currencies")
        val currencies: List<Currency>,
        @SerializedName("demonym")
        val demonym: String,
        @SerializedName("flag")
        val flag: String,
        @SerializedName("gini")
        val gini: Double,
        @SerializedName("languages")
        val languages: List<Language>,
        @SerializedName("latlng")
        val latlng: List<Double>,
        @SerializedName("name")
        val name: String,
        @SerializedName("nativeName")
        val nativeName: String,
        @SerializedName("numericCode")
        val numericCode: String,
        @SerializedName("population")
        val population: Int,
        @SerializedName("region")
        val region: String,
        @SerializedName("regionalBlocs")
        val regionalBlocs: List<RegionalBloc>,
        @SerializedName("subregion")
        val subregion: String,
        @SerializedName("timezones")
        val timezones: List<String>,
        @SerializedName("topLevelDomain")
        val topLevelDomain: List<String>,
        @SerializedName("translations")
        val translations: Translations
)

data class RegionalBloc(
        @SerializedName("acronym")
        val acronym: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("otherAcronyms")
        val otherAcronyms: List<Any>,
        @SerializedName("otherNames")
        val otherNames: List<Any>
)

data class Currency(
        @SerializedName("code")
        val code: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("symbol")
        val symbol: String
)

data class Language(
        @SerializedName("iso639_1")
        val iso6391: String,
        @SerializedName("iso639_2")
        val iso6392: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("nativeName")
        val nativeName: String
)

data class Translations(
        @SerializedName("it")
        val `it`: String,
        @SerializedName("br")
        val br: String,
        @SerializedName("de")
        val de: String,
        @SerializedName("es")
        val es: String,
        @SerializedName("fa")
        val fa: String,
        @SerializedName("fr")
        val fr: String,
        @SerializedName("hr")
        val hr: String,
        @SerializedName("ja")
        val ja: String,
        @SerializedName("nl")
        val nl: String,
        @SerializedName("pt")
        val pt: String
)

// To get all Country names
interface IHadleNetworkAPI {
    @GET("all/")
    fun getAllCountries(): Observable<List<Country>>
}

//To get Country Detail Search By name and Giving Query fullText=True to Search on basis of full name
interface IHadleNetworkAPI_detail {

    @GET("{name}")
    fun getAllCountryByName(@Path("name") name: String,
                            @Query("fullText") fullText:String): Observable<List<Country>>
}