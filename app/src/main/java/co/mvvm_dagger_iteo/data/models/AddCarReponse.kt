package co.mvvm_dagger_iteo.data.models

data class AddCarReponse(
    val _changed: String,
    val _changedby: String,
    val _created: String,
    val _createdby: String,
    val _id: String,
    val _keywords: List<String>,
    val _tags: String,
    val _version: Int,
    val brand: String,
    val color: String,
    val model: String,
    val registration: String,
    val year: String
)