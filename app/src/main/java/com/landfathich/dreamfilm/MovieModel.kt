package com.landfathich.dreamfilm

class MovieModel (
    val id :String = "movie0",
    val name : String = "Black List",
    val description : String = "Description of a Movie ",
    val imageUrl : String = "http://graven.yt/plante.jpg",
    var liked : Boolean = false,
    val original_language : String = "",
    val releaseDate : String = "" ,
    val popularity : Double = 0.0
)