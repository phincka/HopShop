package pl.hincka.hopshop.data.model

data class UserModel (
    val uid: String,
    val name: String,
    val email: String,
    val isModalAlternativeEnable: Boolean,
)